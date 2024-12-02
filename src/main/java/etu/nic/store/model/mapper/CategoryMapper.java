package etu.nic.store.model.mapper;

import etu.nic.store.model.dto.CategoryDto;
import etu.nic.store.model.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public Category toEntity(CategoryDto categoryDto) {
        Category category = new Category();
        applyDtoToEntity(categoryDto, category);
        return category;
    }

    public void updateEntity(CategoryDto categoryDto, Category category) {
        applyDtoToEntity(categoryDto, category);
    }

    private void applyDtoToEntity(CategoryDto categoryDto, Category category) {
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        if (categoryDto.getParentId() != null) {
            Category parentCategory = new Category();
            parentCategory.setId(categoryDto.getParentId());
            category.setParent(parentCategory);
        }
        category.setDescription(categoryDto.getDescription());
        category.setCreatedTime(categoryDto.getCreatedTime());
        category.setUpdatedTime(categoryDto.getUpdatedTime());
        category.setDeletedTime(categoryDto.getDeletedTime());
    }

    public CategoryDto toDto(Category category) {
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
