package com.shop.ecommerce.controller;

import com.shop.ecommerce.dto.AddItemRequest;
import com.shop.ecommerce.dto.CartResponse;
import com.shop.ecommerce.service.CartManagerService;
import com.shop.ecommerce.service.ShoppingCartService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {
    CartManagerService cartManager;

    @GetMapping(ApiPaths.CART_BASE)
    public CartResponse getCart(@PathVariable final String userId) {
        val cart = cartManager.getOrCreateCart(userId);

        return toResponse(userId, cart);
    }

    @PostMapping(ApiPaths.CART_ITEMS)
    public ResponseEntity<?> addItem(
        @PathVariable final String userId,
        @Valid @RequestBody final AddItemRequest request
    ) {
        val cart = cartManager.getOrCreateCart(userId);
        boolean added = cart.addItem(request.productId(), request.quantity());
        if (!added) {

            return ResponseEntity.badRequest()
                .body(Map.of("error", "Недостаточно товара на складе: %s".formatted(request.productId())));
        }

        return ResponseEntity.ok(toResponse(userId, cart));
    }

    @DeleteMapping(ApiPaths.CART_ITEM_BY_ID)
    public ResponseEntity<?> removeItem(
        @PathVariable final String userId,
        @PathVariable final String productId,
        @RequestParam(required = false) final Integer quantity
    ) {
        val cart = cartManager.getOrCreateCart(userId);
        boolean removed = (quantity == null)
            ? cart.removeItem(productId)
            : cart.removeItem(productId, quantity);

        if (!removed) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Товар не найден в корзине: %s".formatted(productId)));
        }

        return ResponseEntity.ok(toResponse(userId, cart));
    }

    @PostMapping(ApiPaths.CART_PROMO)
    public ResponseEntity<?> applyPromo(
        @PathVariable final String userId,
        @RequestParam final String code
    ) {
        val cart = cartManager.getOrCreateCart(userId);
        if (!cart.applyPromoCode(code)) {

            return ResponseEntity.badRequest()
                .body(Map.of("error", "Неверный промокод: %s".formatted(code)));
        }

        return ResponseEntity.ok(toResponse(userId, cart));
    }

    private CartResponse toResponse(final String userId, final ShoppingCartService cart) {
        return CartResponse.builder()
            .userId(userId)
            .items(cart.getItems())
            .discountPercent(cart.getDiscountPercent())
            .total(cart.getTotal())
            .build();
    }
}
