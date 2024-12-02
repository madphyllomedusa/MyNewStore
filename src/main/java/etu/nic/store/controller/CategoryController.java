package etu.nic.store.controller;

import etu.nic.store.model.dto.CategoryDto;
import etu.nic.store.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<List<CategoryDto>> addCategories(@Valid @RequestBody List<CategoryDto> categoryDtos) {
        List<CategoryDto> categories = categoryDtos.stream()
                .map(categoryService::addCategory)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED).body(categories);
    }

}
