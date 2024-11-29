package etu.nic.store.model.mapper;

import etu.nic.store.model.dto.ProductDto;
import etu.nic.store.model.entity.Category;
import etu.nic.store.model.entity.Parameter;
import etu.nic.store.model.entity.Product;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public Product toEntity(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setCreatedTime(productDto.getCreatedTime());
        product.setUpdatedTime(productDto.getUpdatedTime());
        product.setDeletedTime(productDto.getDeletedTime());

        if (productDto.getCategoryIds() != null) {
            Set<Category> categories = productDto.getCategoryIds().stream()
                    .map(categoryId -> {
                        Category category = new Category();
                        category.setId(categoryId);
                        return category;
                    }).collect(Collectors.toSet());
            product.setCategories(categories);
        }

        if (productDto.getParameters() != null) {
            Set<Parameter> parameters = productDto.getParameters().keySet().stream()
                    .map(s -> {
                        Parameter parameter = new Parameter();
                        parameter.setName(s);
                        return parameter;
                    }).collect(Collectors.toSet());
            product.setParameters(parameters);
        }

        return product;
    }

    public ProductDto toDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setCreatedTime(product.getCreatedTime());
        productDto.setUpdatedTime(product.getUpdatedTime());
        productDto.setDeletedTime(product.getDeletedTime());

        if (product.getCategories() != null) {
            Set<Long> categoryIds = product.getCategories().stream()
                    .map(Category::getId).collect(Collectors.toSet());
            productDto.setCategoryIds(categoryIds);
        }

        if (product.getParameters() != null) {
            productDto.setParameters(product.getParameters().stream()
                    .collect(Collectors.toMap(Parameter::getName, parameter -> "значение")));
        }

        return productDto;
    }
}
