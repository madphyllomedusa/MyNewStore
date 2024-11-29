package etu.nic.store.model.mapper;

import etu.nic.store.model.dto.CategoryDto;
import etu.nic.store.model.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    Category toEntity(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        category.setCreatedTime(categoryDto.getCreatedTime());
        category.setUpdatedTime(categoryDto.getUpdatedTime());
        category.setDeletedTime(categoryDto.getDeletedTime());
        return category;
    }

    CategoryDto toDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setParentId(category.getParent() != null ?
                category.getParent().getId() : null);
        categoryDto.setDescription(category.getDescription());
        categoryDto.setCreatedTime(category.getCreatedTime());
        categoryDto.setUpdatedTime(category.getUpdatedTime());
        categoryDto.setDeletedTime(category.getDeletedTime());
        return categoryDto;
    }
}
