package etu.nic.store.service.impl;

import etu.nic.store.exceptionhandler.BadRequestException;
import etu.nic.store.model.dto.ProductDto;
import etu.nic.store.model.entity.Product;
import etu.nic.store.model.mapper.ProductMapper;
import etu.nic.store.repository.ProductRepository;
import etu.nic.store.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductDto addProduct(ProductDto productDto) {
        logger.info("Trying to add product {}", productDto);
        if(productDto == null) {
            logger.error("Product is null");
            throw new BadRequestException("Продукт пустой");
        }
        Product product = productRepository.save(productMapper.toEntity(productDto));
        logger.info("Successfully added product {}", product);
        return productMapper.toDto(product);
    }
}
