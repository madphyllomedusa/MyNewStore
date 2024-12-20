package etu.nic.store.model.mapper;

import etu.nic.store.model.dto.CartDto;
import etu.nic.store.model.dto.CartItemDto;
import etu.nic.store.model.entity.Cart;
import etu.nic.store.model.entity.CartItem;
import etu.nic.store.model.entity.Product;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class CartMapper {

    public CartDto toDto(Cart cart) {
        CartDto dto = new CartDto();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUserId());
        dto.setSessionId(cart.getSessionId());
        if (cart.getItems() == null) {
            dto.setItems(Collections.emptyList());
        } else {
            dto.setItems(cart.getItems().stream()
                    .map(this::mapItemToDto)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    private CartItemDto mapItemToDto(CartItem item) {
        CartItemDto dto = new CartItemDto();
        Product product = item.getProduct();
        dto.setProductId(product.getId());
        dto.setProductName(product.getName());
        dto.setProductPrice(product.getPrice());
        dto.setQuantity(item.getQuantity());
        return dto;
    }
}
