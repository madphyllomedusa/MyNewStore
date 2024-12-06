package etu.nic.store.model.mapper;

import etu.nic.store.model.dto.CartDto;
import etu.nic.store.model.entity.Cart;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {
    public CartDto toDto(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setId(cart.getId());
        cartDto.setUserId(cart.getUserId());
        cartDto.setSessionId(cart.getSessionId());
        cartDto.setItems(cart.getItems().stream().map());
    }
}
