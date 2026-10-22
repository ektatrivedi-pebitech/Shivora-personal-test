
package com.shivora.cart_service.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.shivora.cart_service.entity.Cart;
import com.shivora.cart_service.entity.CartItem;
import com.shivora.cart_service.repository.CartRepository;
import com.shivora.cart_service.service.CartService;

import jakarta.validation.Valid;
import com.shivora.cart_service.dto.AddCartItemRequest;
import com.shivora.cart_service.dto.UpdateCartItemRequest;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {

    private final CartService cartService;
    private final CartRepository cartRepository;

    public CartController(
            CartService cartService,
            CartRepository cartRepository) {
        this.cartService = cartService;
        this.cartRepository = cartRepository;
    }

    // Create a cart or retrieve the user's active cart
    @PostMapping("/create")
    public ResponseEntity<Cart> createCart(
            @RequestParam Long userId) {

        Cart cart = cartService.getOrCreateCart(userId);
        return ResponseEntity.ok(cart);
    }

    // Retrieve a cart by ID
    @PostMapping("/details")
    public ResponseEntity<?> getCartDetails(
            @RequestParam Long cartId) {

        return cartRepository.findById(cartId)
                .filter(cart -> !cart.isDeleted())
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Cart not found"));
    }

    // Retrieve items in a cart
    @PostMapping("/items")
    public ResponseEntity<?> getCartItems(
            @RequestParam Long cartId) {

        var cart = cartRepository.findById(cartId);

        if (cart.isEmpty() || cart.get().isDeleted()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cart not found");
        }

        List<CartItem> items =
                cartService.getCartItems(cartId);

        return ResponseEntity.ok(items);
    }

    // Calculate the cart total
    @PostMapping("/total")
    public ResponseEntity<?> getCartTotal(
            @RequestParam Long cartId) {

        var cart = cartRepository.findById(cartId);

        if (cart.isEmpty() || cart.get().isDeleted()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cart not found");
        }

        BigDecimal total =
                cartService.calculateTotal(cartId);

        return ResponseEntity.ok(total);
    }
    

@PostMapping("/items/add")
public ResponseEntity<?> addItem(
        @RequestParam Long cartId,
        @Valid @RequestBody AddCartItemRequest request) {
    try {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cartService.addItem(cartId, request));
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    } catch (IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }
}

@PostMapping("/items/update")
public ResponseEntity<?> updateItem(
        @Valid @RequestBody UpdateCartItemRequest request) {
    try {
        return ResponseEntity.ok(
                cartService.updateItemQuantity(request));
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    } catch (IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }
}

@PostMapping("/items/remove")
public ResponseEntity<?> removeItem(
        @RequestParam Long cartId,
        @RequestParam Long itemId) {
    try {
        cartService.removeItem(cartId, itemId);
        return ResponseEntity.ok("Cart item removed successfully");
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    } catch (IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }
}

}
