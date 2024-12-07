package etu.nic.store.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CartItemDto {
    @NotNull(message = "Id товара не может быть пустым")
    private Long productId;
    @NotNull(message = "Имя товара не должно быть пустым")
    private String productName;
    @Positive(message = "Цена должна быть больше 0")
    private BigDecimal productPrice;
    private Integer quantity;
}
