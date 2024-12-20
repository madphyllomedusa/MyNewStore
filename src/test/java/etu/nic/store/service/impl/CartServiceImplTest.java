package etu.nic.store.service.impl;

import etu.nic.store.exceptionhandler.NotFoundException;
import etu.nic.store.model.dto.CartDto;
import etu.nic.store.model.entity.Cart;
import etu.nic.store.model.entity.CartItem;
import etu.nic.store.model.entity.Product;
import etu.nic.store.model.mapper.CartMapper;
import etu.nic.store.repository.CartRepository;
import etu.nic.store.service.ProductService;
import etu.nic.store.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartMapper cartMapper;

    @Mock
    private ProductService productService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CartServiceImpl cartService;

    private Cart cart;
    private Product product;

    private static final Long USER_ID = 1L;
    private static final String SESSION_ID = "session123";
    private static final Long PRODUCT_ID = 101L;
    private static final Integer QUANTITY = 2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cart = new Cart();
        cart.setUserId(USER_ID);
        cart.setSessionId(SESSION_ID);
        cart.setItems(new HashSet<>());

        product = new Product();
        product.setId(PRODUCT_ID);
    }

    @ParameterizedTest
    @MethodSource("provideDataForGetCart")
    void testGetCart(Long userId, String sessionId, boolean createNewCart) {
        CartDto cartDto = new CartDto();

        if (createNewCart) {
            when(cartRepository.findBySessionId(sessionId)).thenReturn(Optional.empty());
            when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        } else {
            when(cartRepository.findByUserIdOrSessionId(userId, sessionId)).thenReturn(Optional.of(cart));
        }

        when(cartMapper.toDto(any(Cart.class))).thenReturn(cartDto);

        CartDto result = cartService.getCart(userId, sessionId);

        assertNotNull(result);
        if (createNewCart) {
            verify(cartRepository).save(any(Cart.class));
        } else {
            verify(cartRepository, never()).save(any(Cart.class));
        }
    }

    @ParameterizedTest
    @MethodSource("provideDataForAddProductToCart")
    void testAddProductToCart(Long userId, String sessionId, Long productId, Integer quantity, boolean existingItem) {
        if (existingItem) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(QUANTITY);
            cart.getItems().add(cartItem);
        }

        when(cartRepository.findByUserIdOrSessionId(userId, sessionId)).thenReturn(Optional.of(cart));

        when(productService.getProductEntityById(productId)).thenReturn(product);

        cartService.addProductToCart(userId, sessionId, productId, quantity);

        assertTrue(cart.getItems().stream().anyMatch(item -> item.getProduct().getId().equals(productId)));

        verify(cartRepository).save(cart);
    }

    @ParameterizedTest
    @MethodSource("provideDataForRemoveProductFromCart")
    void testRemoveProductFromCart(Long userId, String sessionId, Long productId, boolean shouldThrowException) {
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cart.getItems().add(cartItem);

        if (shouldThrowException) {
            Cart emptyCart = new Cart();
            emptyCart.setSessionId(sessionId);
            emptyCart.setItems(new HashSet<>());

            when(cartRepository.findByUserIdOrSessionId(userId, sessionId)).thenReturn(Optional.of(emptyCart));

            assertThrows(NotFoundException.class, () -> cartService.removeProductFromCart(userId, sessionId, productId));
        } else {
            when(cartRepository.findByUserIdOrSessionId(userId, sessionId)).thenReturn(Optional.of(cart));

            cartService.removeProductFromCart(userId, sessionId, productId);

            assertTrue(cart.getItems().isEmpty());
            verify(cartRepository).save(cart);
        }
    }

    static Stream<Object[]> provideDataForGetCart() {
        return Stream.of(
                new Object[]{USER_ID, SESSION_ID, false},
                new Object[]{null, "session456", true}
        );
    }

    static Stream<Object[]> provideDataForAddProductToCart() {
        return Stream.of(
                new Object[]{USER_ID, SESSION_ID, PRODUCT_ID, 1, false},
                new Object[]{USER_ID, SESSION_ID, PRODUCT_ID, 1, true}
        );
    }

    static Stream<Arguments> provideDataForRemoveProductFromCart() {
        return Stream.of(
                Arguments.of(1L, "session123", 101L, false),
                Arguments.of(1L, "session123", 999L, true)
        );
    }
}
