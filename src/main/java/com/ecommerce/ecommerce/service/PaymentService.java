package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.entity.Order;
import com.ecommerce.ecommerce.entity.Payment;
import com.ecommerce.ecommerce.repository.OrderRepository;
import com.ecommerce.ecommerce.repository.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository) {

        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    public Payment makePayment(Payment payment) {

        // Check order exists
        Order order = orderRepository.findById(payment.getOrderId())
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        // Check payment amount
        if (payment.getAmount() != order.getTotalAmount()) {
            throw new RuntimeException("Payment amount does not match order total");
        }

        // Set payment status
        payment.setStatus("SUCCESS");

        // Update order status
        order.setStatus("PAID");
        orderRepository.save(order);

        return paymentRepository.save(payment);
    }
}