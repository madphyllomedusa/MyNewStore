package etu.nic.store.model.mapper;

import etu.nic.store.model.dto.ProductDto;
import etu.nic.store.model.entity.Category;
import etu.nic.store.model.entity.Parameter;
import etu.nic.store.model.entity.Product;
import etu.nic.store.model.entity.ProductImage;
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
        product.setQuantity(productDto.getQuantity());
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

        if (productDto.getImageUrls() != null) {
            Set<ProductImage> images = productDto.getImageUrls().stream()
                    .map(imageUrl -> {
                        ProductImage productImage = new ProductImage();
                        productImage.setImageUrl(imageUrl);
                        productImage.setProduct(product);
                        return productImage;
                    }).collect(Collectors.toSet());
            product.setImages(images);
        }

        return product;
    }

    public ProductDto toDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setQuantity(product.getQuantity());
        productDto.setAvailabilityStatus(product.getAvailabilityStatus());
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

        if (product.getImages() != null) {
            Set<String> imageUrls = product.getImages().stream()
                    .map(ProductImage::getImageUrl)
                    .collect(Collectors.toSet());
            productDto.setImageUrls(imageUrls);
        }

        return productDto;
    }
}
