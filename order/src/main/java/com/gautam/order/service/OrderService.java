package com.gautam.order.service;


import com.gautam.order.dao.OrderDAO;
import com.gautam.order.dto.*;
import com.gautam.order.event.OrderCancelEvent;
import com.gautam.order.event.OrderCreatedEvent;
import com.gautam.order.event.OrderItemEvent;
import com.gautam.order.event.OrderPaidEvent;
import com.gautam.order.exceptions.InsufficientStockResource;
import com.gautam.order.exceptions.ResourceNotFoundException;
import com.gautam.order.exceptions.ValidationException;
import com.gautam.order.feign.InventoryClient;
import com.gautam.order.feign.ProductClient;
import com.gautam.order.model.Address;
import com.gautam.order.model.Order;
import com.gautam.order.model.OrderItems;
import com.gautam.order.model.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {
    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    KafkaTemplate<String,Object> kafkaTemplate;

    @Autowired  private ProductClient productClient;

    @Autowired private InventoryClient inventoryClient;
// 1. Create Order
    @CachePut(cacheNames = "order",key = "#result.id")
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request){

        //validate product -> exist and active


        //check inventory -> no of items available or not
        validateInventory(request.getItems());

        // create order record
        Order order = buildOrder(request);
        //deduct inventory -> deduct inventory before payment
        reserveInventory(request.getItems());

        Order savedOrder = orderDAO.save(order);
        OrderCreatedEvent event = new OrderCreatedEvent(savedOrder.getOrderId(),
                savedOrder.getCustomerId(),
                savedOrder.getTotalAmount());

        kafkaTemplate.send("order-created",event);
log.info("Order created successfully : orderCode ={},customerId={}",savedOrder.getOrderCode(),savedOrder.getCustomerId());
        return mapToOrderResponse(savedOrder);

    }
// 2. Check Payment Status
    @Transactional
    public OrderResponse markOrderPaid(Long orderId){

        Order order = getOrder(orderId);

        if(!OrderStatus.PAYMENT_PENDING.equals(order.getStatus())){
            throw new IllegalStateException("Order not in pending state");
        }

        order.setStatus(OrderStatus.PAID);
        order.setUpdatedAt(LocalDateTime.now());

        Order saved = orderDAO.save(order);
        OrderPaidEvent event = new OrderPaidEvent(
                order.getOrderId(),
                order.getCustomerId(),
                order.getTotalAmount()
        );

        kafkaTemplate.send("order-paid",event);

        log.info("Order {} marked as PAID", orderId);

        return mapToOrderResponse(saved);
    }


// 3. Cancel Order

    // 3. CANCEL ORDER → Release inventory
    @CacheEvict(cacheNames = "order" , key = "#orderId")
    @Transactional
    public void cancelOrder(Long orderId, String reason) {
        Order order = getOrder(orderId);

        if (OrderStatus.DELIVERED.equals(order.getStatus())) {//OrderStatus.PAID.equals
            throw new IllegalStateException("Cannot cancel delivered order");
        }

        // Release inventory
        releaseInventory(order.getItems());

        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        orderDAO.save(order);


        List<OrderItemEvent> items = order.getItems()
                .stream()
                .map(orderItems -> new OrderItemEvent(
                        orderItems.getProductId(),
                        orderItems.getQuantity()))
                .toList();
        OrderCancelEvent event = new OrderCancelEvent(orderId,items);

        kafkaTemplate.send("order-cancelled", event);

        log.info("Cancelled order {}: {}", orderId, reason);
    }

    // 4. UPDATE STATUS (shipped, delivered)
    public void updateOrderStatus(Long orderId, String newStatus) {
        Order order = getOrder(orderId);
        order.setStatus(OrderStatus.valueOf(newStatus));
        order.setUpdatedAt(LocalDateTime.now());
        orderDAO.save(order);
    }




    // ------------------------ VALIDATE PRODUCTS --------------------------------


    private void validateInventory(List<OrderitemRequest> items){
        for(OrderitemRequest item :items){
            InventoryResponse response = inventoryClient.getStock(item.getProductId());

            Integer availableQuantity = response.getAvailableQuantity();
            if(availableQuantity<item.getQuantity()){
                throw new InsufficientStockResource("Insufficient stock for product"+ item.getProductId());
            }
        }
    }



    // ---------------------  INVENTORY -------------------------

    private void reserveInventory(List<OrderitemRequest> items){
        for(OrderitemRequest item : items){
            inventoryClient.deductStock(item.getProductId(), item.getQuantity());
        }
    }


    private void releaseInventory(List<OrderItems>items){
        for(OrderItems item : items){
            inventoryClient.addBackStock(item.getProductId(),item.getQuantity());
        }
    }





  // -----------------------     HELPERS  ---------------------------------

    public  Order getOrder(Long orderId){
       // return orderDAO.findByID(id).
            return orderDAO.findByOrderId(orderId).orElseThrow(()-> new ResourceNotFoundException("Order not found:" + orderId));
     }

    private OrderResponse mapToOrderResponse(Order order){
        return new OrderResponse(
                order.getOrderId(),
                order.getCustomerId(),
                order.getOrderCode(),
                order.getTotalAmount(),
                order.getStatus()

        );
    }
    private Address mapAddress(AddressRequest request){
        Address address = new Address();
        address.setFullName(request.getFullName());
        address.setPhone(request.getPhone());
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());
        return address;

    }



    private Order buildOrder(CreateOrderRequest request){
        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setOrderCode("ORD-"+System.currentTimeMillis());
        order.setStatus(OrderStatus.PAYMENT_PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(mapAddress(request.getShippingAddress()));


        BigDecimal total = BigDecimal.ZERO;

        for (OrderitemRequest itemRequest : request.getItems()) {

            ProductResponse product =
                    productClient.getProduct(itemRequest.getProductId());
            if(!product.getIsActive()){
                throw new ValidationException("Product "+itemRequest.getProductId()+"is inactive");
            }


            OrderItems item = new OrderItems();

            item.setProductId(product.getProductId());
            item.setProductName(product.getProductName());
            item.setPrice(product.getPrice());
            item.setQuantity(itemRequest.getQuantity());

            order.addItem(item);

            BigDecimal subTotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            total = total.add(subTotal);
        }

        order.setTotalAmount(total);
        return order;
    }



//    //get order details to fetch by payment service
    @Cacheable(cacheNames = "order",key = "#orderId")
    public  OrderResponse getOrderDetails(Long orderId){
        Order order = orderDAO.findByOrderId(orderId).orElseThrow(()->new ResourceNotFoundException("order is not found with this orderId"+orderId));
        return mapToOrderResponse(order);
    }
}
