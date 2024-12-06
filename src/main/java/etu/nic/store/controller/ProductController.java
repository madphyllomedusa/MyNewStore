package etu.nic.store.controller;

import etu.nic.store.model.dto.ProductDto;
import etu.nic.store.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@Valid @RequestBody ProductDto productDto) {
        ProductDto product = productService.addProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto productDto) {
        ProductDto product = productService.updateProduct(id, productDto);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable Long id) {
        ProductDto product = productService.deleteProductById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(product);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        ProductDto product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/category/{categoryId}")
        public ResponseEntity<Page<ProductDto>> getProductsForCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "priceAsc") String sortBy,
            Pageable pageable) {
        Page<ProductDto> products = productService.getProductsByCategoryAndSubcategories(categoryId, sortBy, pageable);
        return ResponseEntity.ok(products);
    }
}
