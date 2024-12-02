package etu.nic.store.controller;

import etu.nic.store.model.dto.CategoryDto;
import etu.nic.store.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<List<CategoryDto>> addCategories(@Valid @RequestBody List<CategoryDto> categoryDto) {
        List<CategoryDto> categories = categoryDto.stream()
                .map(categoryService::addCategory)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED).body(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto category = categoryService.updateCategory(id, categoryDto);
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryDto> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{parentId}")
    public ResponseEntity<List<CategoryDto>> getCategoryChildren(@PathVariable Long parentId) {
        List<CategoryDto> categories = categoryService.getCategoryChildren(parentId);
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

}
