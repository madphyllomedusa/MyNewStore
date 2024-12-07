package etu.nic.store.service.impl;

import etu.nic.store.exceptionhandler.BadRequestException;
import etu.nic.store.exceptionhandler.NotFoundException;
import etu.nic.store.model.dto.CategoryDto;
import etu.nic.store.model.entity.Category;
import etu.nic.store.model.mapper.CategoryMapper;
import etu.nic.store.repository.CategoryRepository;
import etu.nic.store.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDto addCategory(CategoryDto categoryDto) {
        logger.info("Trying to add category {}", categoryDto);
        validateCategory(categoryDto);
        Category category = categoryRepository.save(categoryMapper.toEntity(categoryDto));
        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        logger.info("Trying to update category with id {}", id);
        validateId(id);
        validateCategory(categoryDto);
        Category category = findCategoryById(id);
        categoryMapper.updateEntity(categoryDto, category);
        categoryRepository.save(category);
        logger.info("Successfully updated category with id {}", id);
        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        logger.info("Trying to delete category with id {}", id);
        validateId(id);
        Category category = findCategoryById(id);
        category.setDeletedTime(OffsetDateTime.now());
        categoryRepository.save(category);
        logger.info("Successfully deleted category with id {}", id);
    }

    @Override
    public List<CategoryDto> getCategoryChildren(Long id) {
        logger.info("Trying to get category children with id {}", id);
        validateId(id);
        Category category = findCategoryById(id);
        List<Category> childCategories = categoryRepository.findByParent_Id(category.getId());
        return childCategories.stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getCategoryAndSubcategoryIds(Long id) {
        logger.info("Fetching category and subcategories for category {}", id);
        validateId(id);
        return categoryRepository.findAllSubcategories(id).stream()
                .map(Category::getId)
                .collect(Collectors.toList());
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

    private void validateId(Long id) {
        if (id == null || id < 0) {
            logger.error("Invalid category id");
            throw new BadRequestException("Неверный id категории");
        }
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));
    }
}
