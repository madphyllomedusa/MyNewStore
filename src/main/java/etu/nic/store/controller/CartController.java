package etu.nic.store.controller;

import etu.nic.store.model.dto.CartDto;
import etu.nic.store.service.CartService;
import etu.nic.store.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<CartDto> getCart(HttpServletRequest request) {
        Long userId = userService.extractUserIdFromContext();
        String sessionId = request.getSession().getId();
        return ResponseEntity.ok(cartService.getCart(userId, sessionId));
    }

    @PostMapping("/item")
    public ResponseEntity<Void> addProductToCart(
            @RequestParam @Valid Long productId,
            @RequestParam @Valid Integer quantity,
            HttpServletRequest request) {
        Long userId = userService.extractUserIdFromContext();
        String sessionId = request.getSession().getId();
        cartService.addProductToCart(userId, sessionId, productId, quantity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeProductFromCart(
            @PathVariable @Valid Long productId,
            HttpServletRequest request) {
        Long userId = userService.extractUserIdFromContext();
        String sessionId = request.getSession().getId();
        cartService.removeProductFromCart(userId, sessionId, productId);
        return ResponseEntity.ok().build();
    }
}
