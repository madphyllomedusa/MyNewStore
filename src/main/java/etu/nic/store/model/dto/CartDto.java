package etu.nic.store.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CartDto {
    private Long id;
    private Long userId;
    private String sessionId;
    private List<CartItemDto> items;
}

