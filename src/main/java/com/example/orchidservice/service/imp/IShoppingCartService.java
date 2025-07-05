package com.example.orchidservice.service.imp;

import com.example.orchidservice.dto.CartItemDTO;
import com.example.orchidservice.dto.ShoppingCartDTO;

public interface IShoppingCartService {
    ShoppingCartDTO getCart(String sessionId);
    ShoppingCartDTO addToCart(String sessionId, String orchidId, String quantity);
    ShoppingCartDTO updateCartItem(String sessionId, String orchidId, String quantity);
    ShoppingCartDTO removeFromCart(String sessionId, String orchidId);
    void clearCart(String sessionId);
}
