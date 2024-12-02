package etu.nic.store.service;

import etu.nic.store.model.dto.CategoryDto;


import java.util.List;

public interface CategoryService {
    CategoryDto addCategory(CategoryDto categoryDto);
    CategoryDto updateCategory(Long id,CategoryDto categoryDto);
    void deleteCategory(Long id);
    List<CategoryDto> getCategoryChildren(Long id);
}
