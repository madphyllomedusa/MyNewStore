package etu.nic.store.service.impl;

import etu.nic.store.exceptionhandler.BadRequestException;
import etu.nic.store.exceptionhandler.NotFoundException;
import etu.nic.store.model.dto.CategoryDto;
import etu.nic.store.model.entity.Category;
import etu.nic.store.model.mapper.CategoryMapper;
import etu.nic.store.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @ParameterizedTest
    @MethodSource("provideAddCategoryData")
    void testAddCategory(CategoryDto input, boolean shouldThrowException) {
        if (shouldThrowException) {
            assertThrows(BadRequestException.class, () -> categoryService.addCategory(input));
            verify(categoryRepository, never()).save(any());
        } else {
            Category category = new Category(1L, input.getName(), input.getDescription(), null, null, null, null, null);
            when(categoryMapper.toEntity(input)).thenReturn(category);
            when(categoryRepository.save(category)).thenReturn(category);
            when(categoryMapper.toDto(category)).thenReturn(input);

            CategoryDto result = categoryService.addCategory(input);

            assertNotNull(result);
            assertEquals(input.getName(), result.getName());
            verify(categoryRepository, times(1)).save(category);
        }
    }

    static List<Object[]> provideAddCategoryData() {
        return Arrays.asList(
                new Object[]{new CategoryDto(null, "Valid Category", "Description", null), false},
                new Object[]{new CategoryDto(null, "", "Description", null), true}, // Invalid name
                new Object[]{null, true} // Null DTO
        );
    }

    @ParameterizedTest
    @MethodSource("provideUpdateCategoryData")
    void testUpdateCategory(Long id, CategoryDto input, boolean shouldThrowException) {
        if (shouldThrowException) {
            if (id == null || id < 0) {
                assertThrows(BadRequestException.class, () -> categoryService.updateCategory(id, input));
            } else if (input == null) {
                assertThrows(BadRequestException.class, () -> categoryService.updateCategory(id, input));
            } else {
                when(categoryRepository.findById(id)).thenReturn(Optional.empty());
                assertThrows(NotFoundException.class, () -> categoryService.updateCategory(id, input));
            }
            verify(categoryRepository, never()).save(any());
        } else {
            Category category = new Category(id, "Old Category", "Old Description", null, null, null, null, null);
            when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
            when(categoryMapper.toDto(category)).thenReturn(input);

            CategoryDto result = categoryService.updateCategory(id, input);

            assertNotNull(result);
            assertEquals(input.getName(), result.getName());
            verify(categoryRepository, times(1)).save(category);
        }
    }

    static List<Object[]> provideUpdateCategoryData() {
        return Arrays.asList(
                new Object[]{1L, new CategoryDto(1L, "Updated Category", "Updated Description", null), false},
                new Object[]{1L, null, true},
                new Object[]{null, new CategoryDto(1L, "Category", "Description", null), true},
                new Object[]{-1L, new CategoryDto(1L, "Category", "Description", null), true}
        );
    }

    @ParameterizedTest
    @MethodSource("provideDeleteCategoryData")
    void testDeleteCategory(Long id, boolean shouldThrowException) {
        if (shouldThrowException) {
            if (id == null || id < 0) {
                assertThrows(BadRequestException.class, () -> categoryService.deleteCategory(id));
            } else {
                when(categoryRepository.findById(id)).thenReturn(Optional.empty());
                assertThrows(NotFoundException.class, () -> categoryService.deleteCategory(id));
            }
        } else {
            Category category = new Category(id, "Category", "Description", null, null, null, null, null);
            when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

            categoryService.deleteCategory(id);

            assertNotNull(category.getDeletedTime());
            verify(categoryRepository, times(1)).save(category);
        }
    }

    static List<Object[]> provideDeleteCategoryData() {
        return Arrays.asList(
                new Object[]{1L, false},
                new Object[]{null, true},
                new Object[]{-1L, true},
                new Object[]{2L, true}
        );
    }

    @ParameterizedTest
    @MethodSource("provideGetCategoryChildrenData")
    void testGetCategoryChildren(Long id, boolean shouldThrowException) {
        if (shouldThrowException) {
            assertThrows(BadRequestException.class, () -> categoryService.getCategoryChildren(id));
        } else {
            Category parent = new Category(id, "Parent", "Parent Description", null, null, null, null, null);
            Category child = new Category(2L, "Child", "Child Description", parent, null, null, null, null);
            when(categoryRepository.findById(id)).thenReturn(Optional.of(parent));
            when(categoryRepository.findByParent_Id(id)).thenReturn(Collections.singletonList(child));
            when(categoryMapper.toDto(child)).thenReturn(new CategoryDto(2L, "Child", "Child Description", id));

            List<CategoryDto> result = categoryService.getCategoryChildren(id);

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Child", result.get(0).getName());
        }
    }

    static List<Object[]> provideGetCategoryChildrenData() {
        return Arrays.asList(
                new Object[]{1L, false},
                new Object[]{null, true},
                new Object[]{-1L, true}
        );
    }

    @ParameterizedTest
    @MethodSource("provideGetCategoryAndSubcategoryIdsData")
    void testGetCategoryAndSubcategoryIds(Long id, List<Long> expectedIds, boolean shouldThrowException) {
        if (shouldThrowException) {
            assertThrows(BadRequestException.class, () -> categoryService.getCategoryAndSubcategoryIds(id));
        } else {
            Category category = new Category(id, "Category", "Description", null, null, null, null, null);
            Category subCategory = new Category(2L, "SubCategory", "SubDescription", category, null, null, null, null);
            when(categoryRepository.findAllSubcategories(id)).thenReturn(Arrays.asList(category, subCategory));

            List<Long> result = categoryService.getCategoryAndSubcategoryIds(id);

            assertNotNull(result);
            assertEquals(expectedIds, result);
        }
    }

    static List<Object[]> provideGetCategoryAndSubcategoryIdsData() {
        return Arrays.asList(
                new Object[]{1L, Arrays.asList(1L, 2L), false},
                new Object[]{null, null, true},
                new Object[]{-1L, null, true}
        );
    }
}
