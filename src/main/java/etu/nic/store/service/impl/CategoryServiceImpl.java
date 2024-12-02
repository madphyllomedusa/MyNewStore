package etu.nic.store.service.impl;

import etu.nic.store.exceptionhandler.BadRequestException;
import etu.nic.store.model.dto.CategoryDto;
import etu.nic.store.model.entity.Category;
import etu.nic.store.model.mapper.CategoryMapper;
import etu.nic.store.repository.CategoryRepository;
import etu.nic.store.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        logger.info("Trying to add category {}", categoryDto);
        validateCategory(categoryDto);
        Category category = categoryRepository.save(categoryMapper.toEntity(categoryDto));
        return categoryMapper.toDto(category);
    }

    private void validateCategory(CategoryDto categoryDto) {
        if (categoryDto == null) {
            logger.error("Category DTO is null");
            throw new BadRequestException("Категория пустая");
        }
        if (categoryDto.getName() == null || categoryDto.getName().isEmpty()) {
            logger.error("Category name is null or empty");
            throw new BadRequestException("Имя категории не заполнено");
        }
    }
}
