package com.example.orchidservice.service;

import com.example.orchidservice.dto.CartItemDTO;
import com.example.orchidservice.dto.ShoppingCartDTO;
import com.example.orchidservice.pojo.Orchid;
import com.example.orchidservice.repository.OrchidRepository;
import com.example.orchidservice.service.imp.IShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ShoppingCartService implements IShoppingCartService {

    @Autowired
    private OrchidRepository orchidRepository;

    // In-memory storage for shopping carts (in production, use Redis or database)
    private final Map<String, Map<String, CartItemDTO>> carts = new ConcurrentHashMap<>();

    @Override
    public ShoppingCartDTO getCart(String sessionId) {
        Map<String, CartItemDTO> cart = carts.get(sessionId);
        if (cart == null || cart.isEmpty()) {
            return new ShoppingCartDTO(new ArrayList<>(), "0.0", "0");
        }
        return buildCartDTO(cart);
    }

    @Override
    public ShoppingCartDTO addToCart(String sessionId, String orchidId, String quantity) {
        Orchid orchid = orchidRepository.findById(orchidId)
                .orElseThrow(() -> new RuntimeException("Orchid not found: " + orchidId));

        Map<String, CartItemDTO> cart = carts.computeIfAbsent(sessionId, k -> new HashMap<>());

        CartItemDTO existingItem = cart.get(orchidId);
        if (existingItem != null) {
            int newQuantity = Integer.parseInt(existingItem.getQuantity()) + Integer.parseInt(quantity);
            existingItem.setQuantity(String.valueOf(newQuantity));
            double price = Double.parseDouble(existingItem.getPrice());
            existingItem.setSubtotal(String.valueOf(price * newQuantity));
        } else {
            CartItemDTO newItem = new CartItemDTO();
            newItem.setOrchidId(orchid.getOrchidId());
            newItem.setOrchidName(orchid.getOrchidName());
            newItem.setPrice(orchid.getPrice());
            newItem.setQuantity(quantity);
            double price = Double.parseDouble(orchid.getPrice());
            int qty = Integer.parseInt(quantity);
            newItem.setSubtotal(String.valueOf(price * qty));
            newItem.setOrchidUrl(orchid.getOrchidUrl());
            cart.put(orchidId, newItem);
        }

        carts.put(sessionId, cart);
        return buildCartDTO(cart);
    }

    @Override
    public ShoppingCartDTO updateCartItem(String sessionId, String orchidId, String quantity) {
        Map<String, CartItemDTO> cart = carts.get(sessionId);
        if (cart != null && cart.containsKey(orchidId)) {
            int qty = Integer.parseInt(quantity);
            if (qty <= 0) {
                cart.remove(orchidId);
            } else {
                CartItemDTO item = cart.get(orchidId);
                item.setQuantity(quantity);
                double price = Double.parseDouble(item.getPrice());
                item.setSubtotal(String.valueOf(price * qty));
            }
        }
        return buildCartDTO(cart != null ? cart : new HashMap<>());
    }

    @Override
    public ShoppingCartDTO removeFromCart(String sessionId, String orchidId) {
        Map<String, CartItemDTO> cart = carts.get(sessionId);
        if (cart != null) {
            cart.remove(orchidId);
        }
        return buildCartDTO(cart != null ? cart : new HashMap<>());
    }

    @Override
    public void clearCart(String sessionId) {
        carts.remove(sessionId);
    }

    private ShoppingCartDTO buildCartDTO(Map<String, CartItemDTO> cart) {
        if (cart == null || cart.isEmpty()) {
            return new ShoppingCartDTO(new ArrayList<>(), "0.0", "0");
        }

        List<CartItemDTO> items = new ArrayList<>(cart.values());
        double totalAmount = items.stream()
                .mapToDouble(item -> Double.parseDouble(item.getSubtotal()))
                .sum();
        int totalItems = items.stream()
                .mapToInt(item -> Integer.parseInt(item.getQuantity()))
                .sum();

        return new ShoppingCartDTO(items, String.valueOf(totalAmount), String.valueOf(totalItems));
    }
}
