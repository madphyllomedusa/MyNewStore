package etu.nic.store.service.impl;

import etu.nic.store.exceptionhandler.BadRequestException;
import etu.nic.store.exceptionhandler.NotFoundException;
import etu.nic.store.model.dto.ProductDto;
import etu.nic.store.model.entity.Product;
import etu.nic.store.model.mapper.ProductMapper;
import etu.nic.store.repository.ProductRepository;
import etu.nic.store.specification.ProductSpecifications;
import etu.nic.store.service.CategoryService;
import etu.nic.store.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryService categoryService;

    @Override
    @Transactional
    public ProductDto addProduct(ProductDto productDto) {
        logger.info("Trying to add product {}", productDto);
        validateProductDto(productDto);
        Product product = productRepository.save(productMapper.toEntity(productDto));
        logger.info("Successfully added product {}", product);
        return productMapper.toDto(product);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        logger.info("Trying to update product with id {}", id);
        validateProductDto(productDto);
        Product product = findProductById(id);
        productMapper.updateEntity(productDto, product);
        productRepository.save(product);
        logger.info("Успешно обновлен продукт с ID {}", id);
        return productMapper.toDto(product);
    }

    @Override
    @Transactional
    public ProductDto deleteProductById(Long id) {
        logger.info("Trying to delete product with ID {}", id);
        Product product = findProductById(id);
        product.setDeletedTime(OffsetDateTime.now());
        productRepository.save(product);
        logger.info("Successfully deleted product with ID {}", id);
        return productMapper.toDto(product);
    }

    @Override
    public ProductDto getProductById(Long id) {
        logger.info("Fetching product with ID {}", id);
        Product product = findProductById(id);
        return productMapper.toDto(product);
    }

    @Override
    public Product getProductEntityById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Продукт с id " + productId + " не найден"));
    }

    @Override
    public Page<ProductDto> getProductsByCategoryAndSubcategories(Long categoryId, String sortBy, Pageable pageable) {
        logger.info("Fetching products for category {} and its subcategories with sort {}", categoryId, sortBy);
        if(categoryId == null || categoryId < 1) {
            logger.error("Invalid category id {}", categoryId);
            throw new BadRequestException("Неверный id категории");
        }
        List<Long> categoryIds = categoryService.getCategoryAndSubcategoryIds(categoryId);

        Specification<Product> specification = ProductSpecifications.belongsToCategories(categoryIds);

        Sort sort = Sort.by(sortBy.equals("priceAsc") ? Sort.Direction.ASC : Sort.Direction.DESC, "price");
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        Page<Product> products = productRepository.findAll(specification, pageRequest);

        return products.map(productMapper::toDto);
    }

    private void validateProductDto(ProductDto productDto) {
        if (productDto == null) {
            logger.error("Product DTO is null");
            throw new BadRequestException("Продукт пустой");
        }
        if (productDto.getName() == null || productDto.getName().isEmpty()) {
            throw new BadRequestException("Имя товара не должно быть пустым");
        }
        if (productDto.getDescription() == null || productDto.getDescription().isEmpty()) {
            throw new BadRequestException("Описание товара не должно быть пустым");
        }
        if (productDto.getPrice() == null || productDto.getPrice().compareTo(new BigDecimal("0")) <= 0) {
            throw new BadRequestException("Цена должна быть больше 0");
        }
        if (productDto.getCategoryIds() == null || productDto.getCategoryIds().isEmpty()) {
            throw new BadRequestException("Продукт должен быть привязан к категории");
        }
        if (productDto.getParameters() == null || productDto.getParameters().isEmpty()) {
            throw new BadRequestException("Продукт должен иметь параметры");
        }
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Продукт с ID " + id + " не найден"));
    }
}
