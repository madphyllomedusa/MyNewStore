package etu.nic.store.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
public class CategoryDto {
    private Long id;
    @NotNull(message = "Название категории не должно быть пустым")
    private String name;
    private String description;
    private Long parentId;

    private OffsetDateTime createdTime;

    private OffsetDateTime updatedTime;

    private OffsetDateTime deletedTime;
}
