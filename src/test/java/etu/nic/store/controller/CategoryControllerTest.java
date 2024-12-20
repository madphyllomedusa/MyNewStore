package etu.nic.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import etu.nic.store.config.JwtService;
import etu.nic.store.config.SecurityConfig;
import etu.nic.store.exceptionhandler.BadRequestException;
import etu.nic.store.exceptionhandler.NotFoundException;
import etu.nic.store.model.dto.CategoryDto;
import etu.nic.store.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CategoryController.class)
@Import(SecurityConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @MethodSource("provideAddCategoryData")
    void testAddCategory(CategoryDto categoryDto, int expectedStatus) throws Exception {
        if (expectedStatus == 201) {
            when(categoryService.addCategory(any(CategoryDto.class))).thenReturn(categoryDto);
        } else {
            when(categoryService.addCategory(any(CategoryDto.class))).thenThrow(new RuntimeException("Error"));
        }

        mockMvc.perform(post("/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().is(expectedStatus));
    }

    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @MethodSource("provideAddCategoriesBulkData")
    void testAddCategoriesBulk(List<CategoryDto> categoryDtos, int expectedStatus) throws Exception {
        if (expectedStatus == 201) {
            when(categoryService.addCategory(any(CategoryDto.class))).thenAnswer(invocation -> invocation.getArgument(0));
        } else {
            when(categoryService.addCategory(any(CategoryDto.class))).thenThrow(new RuntimeException("Error"));
        }

        mockMvc.perform(post("/category/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDtos)))
                .andExpect(status().is(expectedStatus));
    }

    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @MethodSource("provideUpdateCategoryData")
    void testUpdateCategory(Long id, CategoryDto categoryDto, int expectedStatus) throws Exception {
        if (expectedStatus == 200) {
            when(categoryService.updateCategory(id, categoryDto)).thenReturn(categoryDto);
        } else {
            when(categoryService.updateCategory(id, categoryDto)).thenThrow(new RuntimeException("Error"));
        }

        mockMvc.perform(put("/category/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().is(expectedStatus));
    }

    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @MethodSource("provideDeleteCategoryData")
    void testDeleteCategory(Long id, int expectedStatus) throws Exception {
        if (expectedStatus == 204) {
            doNothing().when(categoryService).deleteCategory(id);
        } else if (expectedStatus == 400) {
            doThrow(new BadRequestException("Неверный id категории"))
                    .when(categoryService).deleteCategory(id);
        } else if (expectedStatus == 404) {
            doThrow(new NotFoundException("Категория не найдена"))
                    .when(categoryService).deleteCategory(id);
        }

        mockMvc.perform(delete("/category/{id}", id))
                .andExpect(status().is(expectedStatus));
    }


    @ParameterizedTest
    @MethodSource("provideGetCategoryChildrenData")
    void testGetCategoryChildren(Long parentId, List<CategoryDto> categories, int expectedStatus) throws Exception {
        when(categoryService.getCategoryChildren(parentId)).thenReturn(categories);

        mockMvc.perform(get("/category/{parentId}", parentId))
                .andExpect(status().is(expectedStatus))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    // Test Data Providers
    private static Stream<Object[]> provideAddCategoryData() {
        return Stream.of(
                new Object[]{new CategoryDto(1L, "Category 1", "Description", null), 201},
                new Object[]{new CategoryDto(null, null, "Description", null), 400}
        );
    }

    private static Stream<Object[]> provideAddCategoriesBulkData() {
        return Stream.of(
                new Object[]{Collections.singletonList(new CategoryDto(1L, "Category 1", "Description", null)), 201},
                new Object[]{Collections.singletonList(new CategoryDto(1L, "Category 1", "Description", null)), 201}
        );
    }

    private static Stream<Object[]> provideUpdateCategoryData() {
        return Stream.of(
                new Object[]{1L, new CategoryDto(1L, "Updated Category", "Updated Description", null), 200},
                new Object[]{999L, new CategoryDto(null, null, "Invalid", null), 400}
        );
    }

    private static Stream<Object[]> provideDeleteCategoryData() {
        return Stream.of(
                new Object[]{1L, 204},
                new Object[]{-1L, 400},
                new Object[]{999L, 404}
        );
    }


    private static Stream<Object[]> provideGetCategoryChildrenData() {
        return Stream.of(
                new Object[]{1L, Collections.singletonList(new CategoryDto(2L, "Child Category", "Child Desc", 1L)), 200},
                new Object[]{999L, Collections.emptyList(), 200}
        );
    }
}
