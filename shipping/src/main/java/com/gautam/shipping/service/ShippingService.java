package com.gautam.shipping.service;

import com.gautam.shipping.dao.ShippingDAO;
import com.gautam.shipping.dto.OrderSummary;
import com.gautam.shipping.dto.ShippingResponse;
import com.gautam.shipping.event.OrderShippedEvent;
import com.gautam.shipping.feign.OrderClient;
import com.gautam.shipping.model.ShippingEntity;
import com.gautam.shipping.model.ShippingStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.kafka.core.KafkaTemplate;


import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class ShippingService {
    @Autowired private ShippingDAO shippingDAO;

    @Autowired private OrderClient orderClient;

    @Autowired private KafkaTemplate<String,Object> kafkaTemplate;



    @CacheEvict(value = "shipments",key = "orderId")
    @Transactional
    public ShippingResponse createShipment(Long orderId){
       // OrderSummary order = orderClient.getOrder(orderId);
        Optional<ShippingEntity> orderexist = shippingDAO.findByOrderId(orderId);

//        if(!"PENDING".equals(order.getStatus())){
//            throw new IllegalStateException("Only PAID orders can be shipped");
//        }
        if(orderexist.isPresent()){
            return mapToShippingResponse(orderexist.get());
        }
        ShippingEntity shipment = new ShippingEntity();
        shipment.setOrderId(orderId);
        shipment.setStatus(ShippingStatus.PENDING);
        shipment.setUpdatedAt(LocalDateTime.now());

        ShippingEntity saved = shippingDAO.save(shipment);
log.info("created shipment for orderId : {}",orderId);
        return mapToShippingResponse(saved);
    }
//READY TO SHIP
    @CacheEvict(value = "shipments" ,key = "#id")
    @Transactional
    public  ShippingResponse markReadyToShip(Long id){
        ShippingEntity shipment  = getShipment(id);
        if(shipment.getStatus() != ShippingStatus.PENDING){
            throw new IllegalStateException("Shipment not in Pending state");
        }
        shipment.setStatus((ShippingStatus.READY_TO_SHIP) );
        shipment.setUpdatedAt(LocalDateTime.now());
        shipment.setTrackingNumber("TRCNo-"+System.currentTimeMillis());
        ShippingEntity saved = shippingDAO.save((shipment));

        log.info("mark order as ready to ship");

        return mapToShippingResponse(saved);
    }
    //mark as shipped, set tracking number
    @Transactional
    public ShippingResponse markShipped(Long id, String trackingNumber){
        ShippingEntity shipment = getShipment(id);
        if(shipment.getStatus() != ShippingStatus.READY_TO_SHIP){
            throw new IllegalStateException("Shipment not Ready to ship");

        }
        shipment.setStatus(ShippingStatus.SHIPPED);
        shipment.setTrackingNumber(trackingNumber);
        shipment.setUpdatedAt(LocalDateTime.now());
        ShippingEntity saved=shippingDAO.save(shipment);

        kafkaTemplate.send("order-shipped" , new OrderShippedEvent(saved.getOrderId()));

        log.info("order is shipped");
        return mapToShippingResponse(saved);

    }

    //mark delivered\

    @CacheEvict(value = "shipments",key = "#id")
    @Transactional
public ShippingResponse markDelivered(Long id){
        ShippingEntity shipment = getShipment(id);
        if(shipment.getStatus() != ShippingStatus.SHIPPED){
            throw new IllegalStateException("Shipment not shipped");

        }
        shipment.setStatus(ShippingStatus.DELIVERED);
        shipment.setUpdatedAt(LocalDateTime.now());
        ShippingEntity saved= shippingDAO.save(shipment);
       log.info("delivered");
       return mapToShippingResponse(saved);
}

//cancel

    @CacheEvict(value = "shipments",key = "#id")
    @Transactional
public ShippingResponse cancelShipment(Long id) {
    ShippingEntity shipment = getShipment(id);
    if (shipment.getStatus() == ShippingStatus.DELIVERED) {
        throw new IllegalStateException("Cannot cancel delivered shipment");
    }
    shipment.setStatus(ShippingStatus.CANCELLED);
    shipment.setUpdatedAt(LocalDateTime.now());
        ShippingEntity saved=shippingDAO.save(shipment);
        log.info("Shipment cancelled");

    return mapToShippingResponse(saved);
}

    // 6) Query methods
    @Cacheable(value = "shipments",key = "#id")
    public ShippingResponse getShipmentById(Long id) {
        return mapToShippingResponse(getShipment(id));
    }

    @Cacheable(value = "shipped",key = "#orderId")
    public ShippingResponse getShipmentByOrderId(Long orderId) {
        ShippingEntity shipping= shippingDAO.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Shipment not found for order " + orderId));

        return mapToShippingResponse(shipping);
    }

    // Helper

    @Cacheable(value = "shipments",key = "#id")
    private ShippingEntity getShipment(Long id) {
        return shippingDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipment not found: " + id));
    }



    private ShippingResponse mapToShippingResponse(ShippingEntity shipping){
        return new  ShippingResponse(
                shipping.getId(),
                shipping.getOrderId(),
                shipping.getTrackingNumber(),
                shipping.getUpdatedAt(),
                shipping.getStatus()

        );
    }

}
