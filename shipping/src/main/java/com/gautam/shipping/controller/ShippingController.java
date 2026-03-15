package com.gautam.shipping.controller;

import com.gautam.shipping.dto.ShippingResponse;
import com.gautam.shipping.service.ShippingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shipments")
@Tag(name = "Shipping",description = "APIs for managing shipments")
public class ShippingController {
    @Autowired private ShippingService service;

    @Operation(summary = "Create shipment API")
    @PostMapping("/create/{orderId}")
    public ResponseEntity<ShippingResponse> createShipment(@PathVariable Long orderId){
        return ResponseEntity.ok(service.createShipment(orderId));
    }

    @Operation(summary = "Ready to ship shipment API")
    @PutMapping("/{id}/ready")
    public ResponseEntity<ShippingResponse> readyToShip(@PathVariable Long id ){
        return ResponseEntity.ok(service.markReadyToShip(id));
    }

    @Operation(summary = "mark Shipment ready to ship shipment API")
    @PutMapping("/{id}/ship")
    public ResponseEntity<ShippingResponse> markShipped(
            @PathVariable Long id,
            @RequestParam String trackingNumber){
        return ResponseEntity.ok(service.markShipped(id, trackingNumber));
    }

    @Operation(summary = "Mark shipment delivered API")
    @PutMapping("/{id}/deliver")
    public ResponseEntity<ShippingResponse> markDelivered(@PathVariable Long id){
        return ResponseEntity.ok(service.markDelivered(id));
    }


    @Operation(summary = "Cancel shipment API")
    @PutMapping("/{id}/cancel")
    public ResponseEntity<ShippingResponse> cancelShipment(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancelShipment(id));
    }

    @Operation(summary = "Get shipment API")
    @GetMapping("/{id}")
    public ResponseEntity<ShippingResponse> getShipmentById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getShipmentById(id));
    }

    //  Get Shipment By Order ID
    @Operation(summary = "Get shipment by orderId API")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<ShippingResponse> getShipmentByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(service.getShipmentByOrderId(orderId));
    }
}


