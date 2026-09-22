package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.entity.Payment;
import com.ecommerce.ecommerce.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Payment> makePayment(
            @RequestBody Payment payment) {

        return ResponseEntity.ok(
                paymentService.makePayment(payment)
        );
    }
}
