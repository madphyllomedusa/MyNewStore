package etu.nic.store.controller;

import etu.nic.store.config.JwtService;
import etu.nic.store.config.SecurityConfig;
import etu.nic.store.exceptionhandler.NotFoundException;
import etu.nic.store.model.dto.CartDto;
import etu.nic.store.service.CartService;
import etu.nic.store.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(CartController.class)
@Import(SecurityConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CartService cartService;

    @MockBean
    private UserService userService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(userService.extractUserIdFromContext()).thenReturn(1L);
    }

    @ParameterizedTest
    @MethodSource("provideGetCartTestData")
    void testGetCart(Long userId, String sessionId, CartDto cartDto, int expectedStatus) throws Exception {
        if (cartDto != null) {
            when(cartService.getCart(eq(userId), eq(sessionId))).thenReturn(cartDto);
        } else {
            doThrow(new NotFoundException("Cart not found")).when(cartService).getCart(anyLong(), anyString());
        }

        mockMvc.perform(get("/cart")
                        .sessionAttr("sessionId", sessionId))
                .andExpect(status().is(expectedStatus));
    }

    private static Stream<Arguments> provideGetCartTestData() {
        return Stream.of(
                Arguments.of(1L, "test-session-id", new CartDto(1L, 1L, "test-session-id", new ArrayList<>()), 200),
                Arguments.of(null, "test-session-id", null, 404)
        );
    }


    @ParameterizedTest
    @MethodSource("provideAddProductToCartTestData")
    void testAddProductToCart(Long productId, Integer quantity, int expectedStatus) throws Exception {
        if (productId == null || quantity == null || quantity <= 0) {
            doThrow(new NotFoundException("Invalid input")).when(cartService)
                    .addProductToCart(anyLong(), anyString(), eq(productId), eq(quantity));
        } else {
            doNothing().when(cartService).addProductToCart(anyLong(), anyString(), eq(productId), eq(quantity));
        }

        mockMvc.perform(post("/cart/item")
                        .param("productId", productId != null ? productId.toString() : "")
                        .param("quantity", quantity != null ? quantity.toString() : "")
                        .sessionAttr("sessionId", "test-session-id"))
                .andExpect(status().is(expectedStatus));
    }

    private static Stream<Arguments> provideAddProductToCartTestData() {
        return Stream.of(
                Arguments.of(101L, 2, 200),
                Arguments.of(null, 2, 400),
                Arguments.of(101L, -1, 404),
                Arguments.of(101L, null, 400)
        );
    }


    @ParameterizedTest
    @MethodSource("provideRemoveProductFromCartTestData")
    void testRemoveProductFromCart(Long productId, int expectedStatus) throws Exception {
        if (productId == null || productId <= 0) {
            doThrow(new NotFoundException("Product not found")).when(cartService)
                    .removeProductFromCart(anyLong(), anyString(), eq(productId));
        } else {
            doNothing().when(cartService).removeProductFromCart(anyLong(), anyString(), eq(productId));
        }

        mockMvc.perform(delete("/cart/{productId}", productId)
                        .sessionAttr("sessionId", "test-session-id"))
                .andExpect(status().is(expectedStatus));
    }

    private static Stream<Arguments> provideRemoveProductFromCartTestData() {
        return Stream.of(
                Arguments.of(101L, 200),
                Arguments.of(-1L, 404)
        );
    }

}
