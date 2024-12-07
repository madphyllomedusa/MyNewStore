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
        applyDtoToEntity(productDto, product);
        return product;
    }

    public void updateEntity(ProductDto productDto, Product product) {
        applyDtoToEntity(productDto, product);
    }

    public ProductDto toDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setQuantity(product.getQuantity());
        productDto.setQuantityStatus(product.getQuantityStatus());

        if (product.getCategories() != null) {
            Set<Long> categoryIds = product.getCategories().stream()
                    .map(Category::getId).collect(Collectors.toSet());
            productDto.setCategoryIds(categoryIds);
        }

        if (product.getParameters() != null) {
            productDto.setParameters(product.getParameters().stream()
                    .collect(Collectors.toMap(Parameter::getName, Parameter::getValue)));
        }

        if (product.getImages() != null) {
            Set<String> imageUrls = product.getImages().stream()
                    .map(ProductImage::getImageUrl)
                    .collect(Collectors.toSet());
            productDto.setImageUrls(imageUrls);
        }

        return productDto;
    }

    private void applyDtoToEntity(ProductDto productDto, Product product) {
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
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
            Set<Parameter> parameters = productDto.getParameters().entrySet().stream()
                    .map(entry -> {
                        Parameter parameter = new Parameter();
                        parameter.setName(entry.getKey());
                        parameter.setValue(entry.getValue());
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
    }
}

