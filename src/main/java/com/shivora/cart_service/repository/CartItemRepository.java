
package com.shivora.cart_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shivora.cart_service.entity.CartItem;

@Repository
public interface CartItemRepository
        extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCart_IdAndDeletedFalse(Long cartId);

    Optional<CartItem> findByIdAndCart_IdAndDeletedFalse(
            Long id, Long cartId);
    
    Optional<CartItem> findByCart_IdAndProductIdAndVariantIdAndDeletedFalse(
            Long cartId, Long productId, Long variantId);
    
    
}
