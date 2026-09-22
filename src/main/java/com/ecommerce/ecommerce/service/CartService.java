package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.entity.Cart;
import com.ecommerce.ecommerce.entity.Product;
import com.ecommerce.ecommerce.repository.CartRepository;
import com.ecommerce.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(
            CartRepository cartRepository,
            ProductRepository productRepository) {

        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public Cart addToCart(Cart cart) {

        Product product = productRepository.findById(cart.getProductId())
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));

        // Validate quantity
        if (cart.getQuantity() == null || cart.getQuantity() <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        // Check available stock
        if (cart.getQuantity() > product.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        // Get price directly from Product table
        cart.setPrice(product.getPrice());

        var existingCart = cartRepository
                .findByUserIdAndProductId(
                        cart.getUserId(),
                        cart.getProductId()
                );

        if (existingCart.isPresent()) {

            Cart existing = existingCart.get();

            int newQuantity =
                    existing.getQuantity() + cart.getQuantity();

            // Check stock for combined quantity
            if (newQuantity > product.getQuantity()) {
                throw new RuntimeException("Insufficient stock");
            }

            existing.setQuantity(newQuantity);
            existing.setPrice(product.getPrice());

            return cartRepository.save(existing);
        }

        return cartRepository.save(cart);
    }

    public List<Cart> getCartByUserId(Long userId) {

        return cartRepository.findByUserId(userId);
    }

    public Double getCartTotal(Long userId) {

        List<Cart> cartItems =
                cartRepository.findByUserId(userId);

        return cartItems.stream()
                .filter(cart -> cart.getPrice() != null)
                .mapToDouble(cart ->
                        cart.getPrice() * cart.getQuantity())
                .sum();
    }

    public Cart updateCart(Long id, Integer quantity) {

        Cart cart = cartRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Cart item not found"));

        cart.setQuantity(quantity);

        return cartRepository.save(cart);
    }

    public void removeFromCart(Long id) {

        cartRepository.deleteById(id);
    }
}