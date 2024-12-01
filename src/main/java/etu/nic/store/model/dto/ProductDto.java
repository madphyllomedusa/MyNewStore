package etu.nic.store.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
public class ProductDto {
    @PositiveOrZero(message = "id не может быть меньше нуля")
    private Long id;

    @NotNull(message = "Имя товара не должно быть пустым")
    private String name;

    @NotNull(message = "Описание товара не должно быть пустым")
    private String description;

    @Positive(message = "Цена должна быть больше 0")
    private BigDecimal price;

    @PositiveOrZero(message = "Количество не может быть меньше нуля")
    private Integer quantity;

    @NotNull
    private String availabilityStatus;

    @NotEmpty(message = "Продукт должен быть привязан к категории")
    private Set<Long> categoryIds;

    @NotEmpty(message = "Продукт должен иметь параметры")
    private Map<String, String> parameters;

    @NotEmpty(message = "У продукта должны быть изображения")
    private Set<String> imageUrls;

    private OffsetDateTime createdTime;
    private OffsetDateTime updatedTime;
    private OffsetDateTime deletedTime;
}
