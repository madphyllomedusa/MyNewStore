package etu.nic.store.service.impl;

import etu.nic.store.exceptionhandler.BadRequestException;
import etu.nic.store.exceptionhandler.NotFoundException;
import etu.nic.store.model.dto.ProductDto;
import etu.nic.store.model.entity.Product;
import etu.nic.store.model.mapper.ProductMapper;
import etu.nic.store.repository.ProductRepository;
import etu.nic.store.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductDto validProductDto;
    private Product validProduct;

    @BeforeEach
    void setUp() {
        validProduct = new Product();
        validProduct.setId(1L);
        validProduct.setName("Valid Product");
        validProduct.setDescription("Valid Description");
        validProduct.setPrice(BigDecimal.valueOf(100));

        validProductDto = new ProductDto();
        validProductDto.setId(1L);
        validProductDto.setName("Valid Product");
        validProductDto.setDescription("Valid Description");
        validProductDto.setPrice(BigDecimal.valueOf(100));
    }


    @ParameterizedTest
    @MethodSource("provideTestDataForAddProduct")
    void testAddProduct(ProductDto inputDto, boolean shouldThrowException, Class<? extends Exception> expectedException) {
        if (!shouldThrowException) {
            when(productMapper.toEntity(inputDto)).thenReturn(validProduct);
            when(productRepository.save(validProduct)).thenReturn(validProduct);
            when(productMapper.toDto(validProduct)).thenReturn(validProductDto);

            ProductDto result = productService.addProduct(inputDto);

            assertNotNull(result);
            assertEquals(validProductDto.getId(), result.getId());
            verify(productRepository, times(1)).save(validProduct);
        } else {
            assertThrows(expectedException, () -> productService.addProduct(inputDto));
            verify(productRepository, never()).save(any());
        }
    }

    @ParameterizedTest
    @MethodSource("provideTestDataForGetProductById")
    void testGetProductById(Long productId, boolean shouldThrowException, Class<? extends Exception> expectedException) {
        if (!shouldThrowException) {
            when(productRepository.findById(productId)).thenReturn(Optional.of(validProduct));
            when(productMapper.toDto(validProduct)).thenReturn(validProductDto);

            ProductDto result = productService.getProductById(productId);

            assertNotNull(result);
            assertEquals(validProductDto.getId(), result.getId());
            verify(productRepository, times(1)).findById(productId);
        } else {
            when(productRepository.findById(productId)).thenReturn(Optional.empty());

            assertThrows(expectedException, () -> productService.getProductById(productId));
            verify(productRepository, times(1)).findById(productId);
        }
    }

    @ParameterizedTest
    @MethodSource("provideTestGetProductsByCategoryAndSubcategories")
    void testGetProductsByCategoryAndSubcategories(Long categoryId, String sortBy, boolean shouldThrowException, Class<? extends Exception> expectedException) {
        Pageable pageable = PageRequest.of(0, 10);
        if (!shouldThrowException) {
            when(categoryService.getCategoryAndSubcategoryIds(categoryId))
                    .thenReturn(Collections.singletonList(categoryId));

            Page<Product> mockPage = new PageImpl<>(Collections.singletonList(validProduct));
            when(productRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(mockPage);

            when(productMapper.toDto(validProduct)).thenReturn(validProductDto);

            Page<ProductDto> result = productService.getProductsByCategoryAndSubcategories(categoryId, sortBy, pageable);

            assertNotNull(result);
            assertEquals(1, result.getContent().size());
            assertEquals(validProductDto.getId(), result.getContent().get(0).getId());

            verify(productRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
            verify(categoryService, times(1)).getCategoryAndSubcategoryIds(categoryId);
        } else {
            assertThrows(expectedException, () -> productService.getProductsByCategoryAndSubcategories(categoryId, sortBy, pageable));
            verifyNoInteractions(productRepository);
        }
    }


    static Stream<Object[]> provideTestDataForAddProduct() {
        return Stream.of(
                new Object[]{
                        new ProductDto(
                                1L,
                                "Valid Product",
                                "Description",
                                BigDecimal.valueOf(100),
                                10,
                                "In Stock",
                                Collections.singleton(1L),
                                Collections.singletonMap("key", "value"),
                                Collections.singleton("image1.png")
                        ),
                        false,
                        null
                },
                new Object[]{
                        new ProductDto(
                                null,
                                null,
                                "Description",
                                BigDecimal.valueOf(100),
                                10,
                                "In Stock",
                                Collections.singleton(1L),
                                Collections.singletonMap("key", "value"),
                                Collections.singleton("image1.png")
                        ),
                        true,
                        BadRequestException.class
                }
        );
    }

    static Stream<Object[]> provideTestDataForGetProductById() {
        return Stream.of(
                new Object[]{1L, false, null},
                new Object[]{2L, true, NotFoundException.class}
        );
    }

    static Stream<Object[]> provideTestGetProductsByCategoryAndSubcategories() {
        return Stream.of(
                new Object[]{1L, "priceAsc", false, null},
                new Object[]{0L, "priceAsc", true, BadRequestException.class},
                new Object[]{-1L, "priceAsc", true, BadRequestException.class}
        );
    }
}
