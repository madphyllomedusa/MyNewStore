package etu.nic.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import etu.nic.store.config.JwtService;
import etu.nic.store.config.SecurityConfig;
import etu.nic.store.exceptionhandler.BadRequestException;
import etu.nic.store.model.dto.ProductDto;
import etu.nic.store.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
@Import(SecurityConfig.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @MethodSource("provideAddProductTestData")
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    void testAddProduct(ProductDto productDto, int expectedStatus) throws Exception {
        if (expectedStatus == 201) {
            when(productService.addProduct(any(ProductDto.class))).thenReturn(productDto);
        } else {
            when(productService.addProduct(any(ProductDto.class))).thenThrow(new BadRequestException("Invalid product data"));
        }

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().is(expectedStatus));
    }

    @ParameterizedTest
    @MethodSource("provideBulkProductTestData")
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    void testAddProductBulk(List<ProductDto> productDtos, int expectedStatus) throws Exception {
        if (expectedStatus == 201) {
            when(productService.addProduct(any(ProductDto.class))).thenAnswer(invocation -> invocation.getArgument(0));
        } else {
            when(productService.addProduct(any(ProductDto.class))).thenThrow(new BadRequestException("Invalid product data"));
        }

        mockMvc.perform(post("/product/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDtos)))
                .andExpect(status().is(expectedStatus));
    }


    @ParameterizedTest
    @MethodSource("provideUpdateProductTestData")
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    void testUpdateProduct(Long productId, ProductDto productDto, int expectedStatus) throws Exception {
        if (expectedStatus == 200) {
            when(productService.updateProduct(productId, productDto)).thenReturn(productDto);
        } else {
            when(productService.updateProduct(productId, productDto)).thenThrow(new BadRequestException("Invalid product data"));
        }

        mockMvc.perform(put("/product/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().is(expectedStatus));
    }

    @ParameterizedTest
    @MethodSource("provideDeleteProductTestData")
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    void testDeleteProduct(Long productId, int expectedStatus) throws Exception {
        if (expectedStatus == 204) {
            when(productService.deleteProductById(productId)).thenReturn(new ProductDto());
        } else {
            when(productService.deleteProductById(productId)).thenThrow(new BadRequestException("Product not found"));
        }

        mockMvc.perform(delete("/product/{id}", productId))
                .andExpect(status().is(expectedStatus));
    }

    @ParameterizedTest
    @MethodSource("provideGetProductTestData")
    void testGetProductById(Long productId, int expectedStatus) throws Exception {
        if (expectedStatus == 200) {
            when(productService.getProductById(productId)).thenReturn(new ProductDto(
                    productId, "Product", "Description", BigDecimal.valueOf(10), 10, "In Stock",
                    new HashSet<>(Collections.singletonList(1L)), Collections.singletonMap("Color", "Red"), new HashSet<>(Collections.singletonList("url"))
            ));
        } else {
            when(productService.getProductById(productId)).thenThrow(new BadRequestException("Product not found"));
        }

        mockMvc.perform(get("/product/{id}", productId))
                .andExpect(status().is(expectedStatus));
    }

    private static Stream<Object[]> provideAddProductTestData() {
        return Stream.of(
                new Object[]{new ProductDto(null, "Product A", "Description A", BigDecimal.valueOf(100), 10, "In Stock",
                        new HashSet<>(Collections.singletonList(1L)), Collections.singletonMap("Color", "Blue"), new HashSet<>(Collections.singletonList("url1"))), 201},
                new Object[]{new ProductDto(null, "", "Description A", BigDecimal.valueOf(100), 10, "In Stock",
                        new HashSet<>(Collections.singletonList(1L)), Collections.singletonMap("Color", "Blue"), new HashSet<>(Collections.singletonList("url1"))), 400},
                new Object[]{new ProductDto(null, "Product A", "Description A", BigDecimal.valueOf(-1), 10, "In Stock",
                        new HashSet<>(Collections.singletonList(1L)), Collections.singletonMap("Color", "Blue"), new HashSet<>(Collections.singletonList("url1"))), 400}
        );
    }

    private static Stream<Arguments> provideBulkProductTestData() {
        ProductDto validProduct1 = new ProductDto(
                1L, "Product1", "Description1", BigDecimal.valueOf(100), 10,
                "In Stock", new HashSet<>(Collections.singletonList(1L)),
                new HashMap<>(Collections.singletonMap("Color", "Red")),
                new HashSet<>(Collections.singletonList("image1.jpg"))
        );

        ProductDto validProduct2 = new ProductDto(
                2L, "Product2", "Description2", BigDecimal.valueOf(200), 5,
                "In Stock", new HashSet<>(Collections.singletonList(2L)),
                new HashMap<>(Collections.singletonMap("Size", "L")),
                new HashSet<>(Collections.singletonList("image2.jpg"))
        );

        ProductDto invalidProduct = new ProductDto(
                null, "", "", BigDecimal.valueOf(-10), -5,
                "Out of Stock", Collections.emptySet(),
                Collections.emptyMap(), Collections.emptySet()
        );

        return Stream.of(
                Arguments.of(Arrays.asList(validProduct1, validProduct2), 201),
                Arguments.of(Arrays.asList(validProduct1, invalidProduct), 400)
        );
    }


    private static Stream<Object[]> provideUpdateProductTestData() {
        return Stream.of(
                new Object[]{1L, new ProductDto(1L, "Updated Product", "Updated Description", BigDecimal.valueOf(150), 5, "Low Stock",
                        new HashSet<>(Collections.singletonList(1L)), Collections.singletonMap("Size", "L"), new HashSet<>(Collections.singletonList("url2"))), 200},
                new Object[]{1L, new ProductDto(1L, "", "Updated Description", BigDecimal.valueOf(150), 5, "Low Stock",
                        new HashSet<>(Collections.singletonList(1L)), Collections.singletonMap("Size", "L"), new HashSet<>(Collections.singletonList("url2"))), 400}
        );
    }

    private static Stream<Object[]> provideDeleteProductTestData() {
        return Stream.of(
                new Object[]{1L, 204},
                new Object[]{999L, 400}
        );
    }

    private static Stream<Object[]> provideGetProductTestData() {
        return Stream.of(
                new Object[]{1L, 200},
                new Object[]{999L, 400}
        );
    }
}
