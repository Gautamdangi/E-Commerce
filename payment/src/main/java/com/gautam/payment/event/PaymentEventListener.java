package com.gautam.payment.event;

import com.gautam.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentEventListener {
@Autowired
    PaymentService paymentService;
    @KafkaListener(topics = "order-created", groupId = "payment-group")
    public void handleOrderCreated(OrderCreatedEvent event){

        paymentService.processPayment(event.getOrderId());
    }}
