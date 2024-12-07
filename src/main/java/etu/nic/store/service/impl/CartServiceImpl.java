package etu.nic.store.service.impl;

import etu.nic.store.exceptionhandler.NotFoundException;
import etu.nic.store.model.dto.CartDto;
import etu.nic.store.model.entity.Cart;
import etu.nic.store.model.entity.CartItem;
import etu.nic.store.model.entity.Product;
import etu.nic.store.model.mapper.CartMapper;
import etu.nic.store.model.mapper.ProductMapper;
import etu.nic.store.repository.CartRepository;
import etu.nic.store.service.CartService;
import etu.nic.store.service.ProductService;
import etu.nic.store.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final UserService userService;

    @Override
    public CartDto getCart(Long userId, String sessionId) {
        userId = resolveUserId(userId);

        logger.info("Getting cart for user {} with session {}", userId, sessionId);

        Cart cart = findOrCreateCart(userId, sessionId);
        logger.info("Found or created cart for user {} with session {}", userId, sessionId);

        return cartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public void addProductToCart(Long userId, String sessionId, Long productId, Integer quantity) {
        userId = resolveUserId(userId);

        Cart cart = findOrCreateCart(userId, sessionId);
        Product product = productMapper.toEntity(productService.getProductById(productId));

        logger.info("Trying to add product {} to user {} with session {}", productId, userId, sessionId);

        CartItem existingItem = findCartItem(cart, productId);
        if (existingItem == null) {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
        } else {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        }

        logger.info("Product {} added to cart {}", productId, cart);
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void removeProductFromCart(Long userId, String sessionId, Long productId) {
        userId = resolveUserId(userId);

        logger.info("Removing product {} from cart for session {}", productId, sessionId);

        Cart cart = findOrCreateCart(userId, sessionId);
        CartItem item = findCartItem(cart, productId);
        if (item == null) {
            logger.error("Product {} not fond in cart {}", productId, cart.getId());
            throw new NotFoundException("Продукт " + productId + " не найден в корзине");
        }
        cart.getItems().remove(item);

        logger.info("Product {} removed from cart {}", productId, cart);
        cartRepository.save(cart);
    }


    private Cart findOrCreateCart(Long userId, String sessionId) {
        if (userId == null || userId == 0L) {
            return cartRepository.findBySessionId(sessionId)
                    .orElseGet(() -> {
                        logger.info("Creating new cart for session {}", sessionId);
                        Cart cart = new Cart();
                        cart.setSessionId(sessionId);
                        cart.setItems(new HashSet<>());
                        logger.info("Cart for unauthorized user created");
                        return cartRepository.save(cart);
                    });
        }

        return cartRepository.findByUserIdOrSessionId(userId, sessionId)
                .orElseGet(() -> {
                    logger.info("Creating new cart for user {} with session {}", userId, sessionId);
                    Cart cart = new Cart();
                    cart.setUserId(userId);
                    cart.setSessionId(sessionId);
                    cart.setItems(new HashSet<>());
                    logger.info("Cart for authorized user created");
                    return cartRepository.save(cart);
                });
    }

    private CartItem findCartItem(Cart cart, Long productId) {
        return cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    private Long resolveUserId(Long userId) {
        if (userId == null) {
            return userService.extractUserIdFromContext();
        }
        return userId;
    }

}
