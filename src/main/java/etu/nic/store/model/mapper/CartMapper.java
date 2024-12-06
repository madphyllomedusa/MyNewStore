package etu.nic.store.model.mapper;

import etu.nic.store.model.dto.CartDto;
import etu.nic.store.model.dto.CartItemDto;
import etu.nic.store.model.entity.Cart;
import etu.nic.store.model.entity.CartItem;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CartMapper {
    public CartDto toDto(Cart cart) {
        CartDto dto = new CartDto();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUserId());
        dto.setSessionId(cart.getSessionId());
        dto.setItems(cart.getItems().stream()
                .map(this::mapItemToDto)
                .collect(Collectors.toList()));
        return dto;
    }

    private CartItemDto mapItemToDto(CartItem item) {
        CartItemDto dto = new CartItemDto();
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setQuantity(item.getQuantity());
        dto.setProductPrice(item.getProduct().getPrice());
        return dto;
    }
}
