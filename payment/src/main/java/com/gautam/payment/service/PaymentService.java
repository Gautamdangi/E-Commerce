package com.gautam.payment.service;

import com.gautam.payment.dao.PaymentDAO;
import com.gautam.payment.dto.CreatePaymentDto;
import com.gautam.payment.dto.OrderSummary;
import com.gautam.payment.dto.PaymentResponse;
import com.gautam.payment.event.OrderPaidEvent;
import com.gautam.payment.event.PaymentFailedEvent;
import com.gautam.payment.exceptions.ResourceNotFoundException;
import com.gautam.payment.feign.OrderClient;
import com.gautam.payment.model.Payment;
import com.gautam.payment.model.PaymentMethod;
import com.gautam.payment.model.PaymentStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Slf4j
public class PaymentService {

@Autowired private PaymentDAO paymentDAO;
@Autowired private OrderClient orderClient;
@Autowired private KafkaTemplate<String,Object> kafkaTemplate;


//1. payment initiate for an  order
@Transactional
    public PaymentResponse initiatePayment(Long orderId, PaymentMethod method){
    // first we get order from order service
        OrderSummary order =orderClient.getOrder(orderId);
        if(!"PAYMENT_PENDING".equals(order.getStatus())) {
            throw new IllegalStateException("Order is not completed yet");
        }
Payment existing = paymentDAO.findByOrderId(orderId).orElse(null);
if(existing != null){
    return mapToPaymentResponse(existing);
}
        var payment = new Payment();
        payment.setOrderId(orderId);
        payment.setTotalAmount(order.getTotalAmount());
        payment.setPaymentMethod(method);
        payment.setStatus(PaymentStatus.PENDING);

        //generate reference
        payment.setPaymentReference(generatePaymentRef());

        Payment saved = paymentDAO.save(payment);
        log.info("Payment initiated for order {} : {}", orderId, saved.getPaymentReference());

        return mapToPaymentResponse(saved);
    }

//2. confirm payment
@Transactional
    public PaymentResponse confirmPayment(Long paymentId,Boolean success,String transactionId){
        Payment payment = paymentDAO.findById(paymentId).orElseThrow(()->new ResourceNotFoundException("Payment not found"));

        if(!PaymentStatus.PENDING.equals(payment.getStatus())){
            throw new IllegalStateException("payment is not in pending state");
        }
        if(success){
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setTransactionId(transactionId);

            kafkaTemplate.send("order-paid",new OrderPaidEvent(payment.getOrderId()));

//            contact to order to tell payment is succeeded
//            orderClient.markOrderPaid(payment.getOrderId());
            log.info("Payment {} confirmed for order {}", paymentId, payment.getOrderId());

        }
        else {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason("Payment failed");

// Publish payment failed event so inventory can be released
            kafkaTemplate.send("payment-failed", new PaymentFailedEvent(
                    payment.getOrderId(),
                    payment.getId()));

            log.warn("Payment {} failed for order {}", paymentId, payment.getOrderId());

        }
        Payment saved = paymentDAO.save(payment);
        return mapToPaymentResponse(saved);


    }

    //3. REFUND PAYMENT FOR RETURN/CANCELLATION

    @Transactional
    public PaymentResponse refundPayment(Long paymentId, BigDecimal amount){
        Payment payment = paymentDAO.findById(paymentId).orElseThrow(()-> new ResourceNotFoundException("Payment not found for this id"));


        if(!PaymentStatus.SUCCESS.equals(payment.getStatus())){
            throw new IllegalStateException("Only successful payment can be refunded");

        }
        // Validate refund amount
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Refund amount must be greater than zero");
        }

        if (amount.compareTo(payment.getTotalAmount()) > 0) {
            throw new IllegalArgumentException(
                    "Refund amount (" + amount + ") cannot exceed payment amount ("
                            + payment.getTotalAmount() + ")");
        }
        payment.setRefundAmount(amount);
        payment.setStatus(PaymentStatus.REFUNDED);
        Payment saved = paymentDAO.save(payment);
        return mapToPaymentResponse(saved);

    }




    // 4. GET PAYMENT DETAILS
    public PaymentResponse getPayment(Long paymentId){
        Payment payment = paymentDAO.findById(paymentId).orElseThrow(()->new ResourceNotFoundException("Payment not found"));

        return  mapToPaymentResponse(payment);

    }
    public PaymentResponse getPaymentByOrderId(Long orderId) {
        Payment payment = paymentDAO.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payment not found for order: " + orderId));

        return mapToPaymentResponse(payment);
    }

    private PaymentResponse mapToPaymentResponse(Payment paymentDetails){
        return new PaymentResponse(
                paymentDetails.getId(),
                paymentDetails.getPaymentReference(),
                paymentDetails.getTotalAmount(),
                paymentDetails.getStatus()
        );
    }

    private String generatePaymentRef(){
    return "PAY-" + System.currentTimeMillis();
    }
    public void processPayment(Long orderId){

        initiatePayment(orderId, PaymentMethod.CARD);
    }
}
