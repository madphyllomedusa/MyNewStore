package etu.nic.store.service;

import etu.nic.store.model.dto.CartDto;

public interface CartService {
    CartDto getCart(Long userId, String sessionId);
    void addProductToCart(Long userId, String sessionId, Long productId, Integer quantity);
    void removeProductFromCart(Long userId, String sessionId, Long productId);
}
