package com.ecommerce.ecommerce.service;

import  com.ecommerce.ecommerce.entity.Product;
import com.ecommerce.ecommerce.repository.ProductRepository;
import com.ecommerce.ecommerce.entity.Cart;
import com.ecommerce.ecommerce.entity.Order;
import com.ecommerce.ecommerce.repository.CartRepository;
import com.ecommerce.ecommerce.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public OrderService(
            OrderRepository orderRepository,
            CartRepository cartRepository,
            ProductRepository productRepository ) {

        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    // Place normal order
    public Order placeOrder(Order order) {

        if (order.getStatus() == null ||
                order.getStatus().isBlank()) {

            order.setStatus("PLACED");
        }

        return orderRepository.save(order);
    }

    // Get all orders of a user
    public List<Order> getUserOrders(Long userId) {

        return orderRepository.findByUserId(userId);
    }

    // Get order by ID
    public Order getOrderById(Long id) {

        return orderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));
    }

    // Update order status
    public Order updateOrderStatus(Long id, String status) {

        Order order = getOrderById(id);

        order.setStatus(status);

        return orderRepository.save(order);
    }

    // Checkout cart and create order
    public Order checkout(Long userId) {

        List<Cart> cartItems =
                cartRepository.findByUserId(userId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        for (Cart cart : cartItems) {

            Product product = productRepository.findById(cart.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getQuantity() < cart.getQuantity()) {
                throw new RuntimeException(
                        "Insufficient stock for product: " + product.getName()
                );
            }

            product.setQuantity(
                    product.getQuantity() - cart.getQuantity()
            );

            productRepository.save(product);
        }

        double totalAmount = cartItems.stream()
                .filter(cart -> cart.getPrice() != null)
                .mapToDouble(cart ->
                        cart.getPrice() * cart.getQuantity())
                .sum();

        Order order = new Order();

        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setStatus("PLACED");

        Order savedOrder = orderRepository.save(order);

        for (Cart cart : cartItems) {

            Product product = productRepository.findById(cart.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            product.setQuantity(
                    product.getQuantity() - cart.getQuantity()
            );
            productRepository.save(product);
        }

        // Clear cart after successful order
        cartRepository.deleteAll(cartItems);

        return savedOrder;
    }
}