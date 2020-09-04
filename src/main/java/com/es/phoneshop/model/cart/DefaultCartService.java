package com.es.phoneshop.model.cart;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.exceptions.OutOfStockException;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Optional;

;

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

        checkStock(product, quantity, productsAmount);

        if (optionalCartItem.isPresent()) {
            optionalCartItem.get().setQuantity(productsAmount + quantity);
        } else {
            cart.getItems().add(new CartItem(product, quantity));
        }
        recalculateCart(cart);
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

    private synchronized void checkStock(Product product, int quantity, int currentQuantity) throws OutOfStockException {
        if (product.getStock() < quantity + currentQuantity) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }
    }

    @Override
    public void update(Cart cart, Long productId, int quantity) throws OutOfStockException, OutOfMemoryError {
        checkQuantity(quantity);

        Product product = productDao.get(productId);
        Optional<CartItem> optionalCartItem = findItemInCart(cart, productId);
        int productsAmount = optionalCartItem.map(CartItem::getQuantity).orElse(0);

        checkStock(product, quantity, productsAmount);

        if (optionalCartItem.isPresent()) {
            optionalCartItem.get().setQuantity(quantity);
        } else {
            cart.getItems().add(new CartItem(product, quantity));
        }
        recalculateCart(cart);
    }

    @Override
    public synchronized void delete(Cart cart, Long productId) {
        cart.getItems().removeIf(cartItem ->
                productId.equals(cartItem.getProduct().getId()));
        recalculateCart(cart);
    }

    private synchronized void recalculateCart(Cart cart) {
        recalculatePrice(cart);
        recalculateQuantity(cart);
    }

    private synchronized void recalculateQuantity(Cart cart) {
        cart.setTotalQuantity(cart.getItems().stream()
                .map(CartItem::getQuantity).mapToInt(q -> q).sum());
    }

    private synchronized void recalculatePrice(Cart cart) {
        BigDecimal totalPrice = cart.getItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalCost(totalPrice);
    }

    @Override
    public void clear(Cart cart) {
        cart.getItems().clear();
        recalculateCart(cart);
    }
}
