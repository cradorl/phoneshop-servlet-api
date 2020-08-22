package com.es.phoneshop.model.cart.service;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.exceptions.OutOfStockException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class DefaultCartService implements CartService {
    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";

    private ProductDao productDao;

    private DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    private static class Holder {
        private static final DefaultCartService instance = new DefaultCartService();
    }

    public static DefaultCartService getInstance() {
        return DefaultCartService.Holder.instance;
    }

    @Override
    public synchronized Cart getCart(HttpServletRequest request) {
        Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
        if (cart == null) {
            request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
        }
        return cart;
    }

    @Override
    public synchronized void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
        Product product = productDao.get(productId);
        if (product.getStock() < quantity) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }

        Optional<CartItem> optionalCartItem = findItemInCart(cart, productId);
        if (optionalCartItem.isPresent()) {
            CartItem cartItem = optionalCartItem.get();
            if (product.getStock() >= cartItem.getQuantity() + quantity) {
                cartItem.increaseQuantity(quantity);
            } else {
                throw new OutOfStockException(product, quantity, product.getStock());
            }
        } else {
            cart.getItems().add(new CartItem(product, quantity));
        }
    }

    private synchronized Optional<CartItem> findItemInCart(Cart cart, Long productId) {
        return cart
                .getItems()
                .stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
                .findFirst();
    }

}
