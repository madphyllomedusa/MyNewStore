package etu.nic.store.service;

import etu.nic.store.model.dto.CartDto;

public interface CartService {
    CartDto getCart(Long userId, String sessionId);
    void addToCart(Long userId, String sessionId, Long productId, Integer quantity);
    void removeFromCart(Long userId, String sessionId, Long productId);
}
