package com.es.phoneshop.model.cart.service;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.exceptions.OutOfStockException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpSession;
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
    public synchronized Cart getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute(CART_SESSION_ATTRIBUTE);
        if (cart == null) {
            cart = new Cart();
            session.setAttribute(CART_SESSION_ATTRIBUTE, cart);
        }
        return cart;
    }

    @Override
    public synchronized void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
       checkQuantity(quantity);

        Product product = productDao.get(productId);
        Optional<CartItem> optionalCartItem = findItemInCart(cart, productId);
        int productsAmount = optionalCartItem.map(CartItem::getQuantity).orElse(0);

        if (product.getStock() < quantity + productsAmount) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }
        if (optionalCartItem.isPresent()) {
            optionalCartItem.get().setQuantity(productsAmount + quantity);
        } else {
            cart.getItems().add(new CartItem(product, quantity));
        }
    }

    private synchronized Optional<CartItem> findItemInCart(Cart cart, Long productId) {
        return cart
                .getItems()
                .stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
                .findAny();
    }

    private synchronized void checkQuantity(int quantity) throws OutOfStockException {
        if (quantity <= 0) {
            throw new OutOfStockException(null, quantity, 0);
        }
    }

    @Override
    public void update(Cart cart, Long productId, int quantity) throws OutOfStockException, OutOfMemoryError {
        checkQuantity(quantity);

        Product product = productDao.get(productId);
        Optional<CartItem> optionalCartItem = findItemInCart(cart, productId);
        int productsAmount = optionalCartItem.map(CartItem::getQuantity).orElse(0);

        if (product.getStock() < quantity + productsAmount) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }
        if (optionalCartItem.isPresent()) {
            optionalCartItem.get().setQuantity(quantity);
        } else {
            cart.getItems().add(new CartItem(product, quantity));
        }
    }
}
