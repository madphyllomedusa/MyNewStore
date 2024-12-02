package etu.nic.store.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    @CreationTimestamp
    private OffsetDateTime createdTime;
    @UpdateTimestamp
    private OffsetDateTime updatedTime;

    private OffsetDateTime deletedTime;
}
