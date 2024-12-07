package etu.nic.store.service;

import etu.nic.store.model.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductDto addProduct(ProductDto productDto);

    ProductDto updateProduct(Long id, ProductDto productDto);

    ProductDto getProductById(Long id);

    ProductDto deleteProductById(Long id);

    Page<ProductDto> getProductsByCategoryAndSubcategories(Long categoryId, String sortBy, Pageable pageable);
}
