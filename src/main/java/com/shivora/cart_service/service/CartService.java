
package com.shivora.cart_service.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shivora.cart_service.entity.Cart;
import com.shivora.cart_service.entity.CartItem;
import com.shivora.cart_service.repository.CartItemRepository;
import com.shivora.cart_service.repository.CartRepository;


import com.shivora.cart_service.dto.AddCartItemRequest;
import com.shivora.cart_service.dto.UpdateCartItemRequest;


@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public Cart getOrCreateCart(Long userId) {

        return cartRepository
                .findByUserIdAndStatusAndDeletedFalse(userId, "ACTIVE")
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUserId(userId);
                    cart.setStatus("ACTIVE");
                    cart.setCurrency("INR");

                    return cartRepository.save(cart);
                });
    }

    @Transactional(readOnly = true)
    public List<CartItem> getCartItems(Long cartId) {
        return cartItemRepository.findByCart_IdAndDeletedFalse(cartId);
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateTotal(Long cartId) {

        return getCartItems(cartId).stream()
                .map(item -> item.getUnitPriceSnapshot()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    

@Transactional
public CartItem addItem(Long cartId, AddCartItemRequest request) {

    Cart cart = cartRepository.findById(cartId)
            .filter(c -> !c.isDeleted())
            .orElseThrow(() ->
                    new IllegalArgumentException("Cart not found"));

    if (!"ACTIVE".equals(cart.getStatus())) {
        throw new IllegalStateException("Cart is not active");
    }

    Long variantId = request.getVariantId() == null
            ? 0L : request.getVariantId();

    CartItem item = cartItemRepository
            .findByCart_IdAndProductIdAndVariantIdAndDeletedFalse(
                    cartId, request.getProductId(), variantId)
            .orElse(null);

    if (item != null) {
        item.setQuantity(item.getQuantity() + request.getQuantity());
        return cartItemRepository.save(item);
    }

    item = new CartItem();
    item.setCart(cart);
    item.setProductId(request.getProductId());
    item.setVariantId(variantId);
    item.setSku(request.getSku());
    item.setProductName(request.getProductName());
    item.setQuantity(request.getQuantity());
    item.setUnitPriceSnapshot(request.getUnitPriceSnapshot());

    return cartItemRepository.save(item);
}

@Transactional
public CartItem updateItemQuantity(UpdateCartItemRequest request) {

    CartItem item = cartItemRepository
            .findByIdAndCart_IdAndDeletedFalse(
                    request.getItemId(), request.getCartId())
            .orElseThrow(() ->
                    new IllegalArgumentException("Cart item not found"));

    Cart cart = cartRepository.findById(request.getCartId())
            .filter(c -> !c.isDeleted())
            .orElseThrow(() ->
                    new IllegalArgumentException("Cart not found"));

    if (!"ACTIVE".equals(cart.getStatus())) {
        throw new IllegalStateException("Cart is not active");
    }

    item.setQuantity(request.getQuantity());
    return cartItemRepository.save(item);
}

@Transactional
public void removeItem(Long cartId, Long itemId) {

    Cart cart = cartRepository.findById(cartId)
            .filter(c -> !c.isDeleted())
            .orElseThrow(() ->
                    new IllegalArgumentException("Cart not found"));

    if (!"ACTIVE".equals(cart.getStatus())) {
        throw new IllegalStateException("Cart is not active");
    }

    CartItem item = cartItemRepository
            .findByIdAndCart_IdAndDeletedFalse(itemId, cartId)
            .orElseThrow(() ->
                    new IllegalArgumentException("Cart item not found"));

    item.setDeleted(true);
    cartItemRepository.save(item);
}

}
