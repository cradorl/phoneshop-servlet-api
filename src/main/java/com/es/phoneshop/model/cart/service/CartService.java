package com.es.phoneshop.model.cart.service;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.exceptions.OutOfStockException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface CartService {
    Cart getCart(HttpSession session);
    void add(Cart cart, Long productId, int quantity) throws OutOfStockException, OutOfMemoryError;
}
