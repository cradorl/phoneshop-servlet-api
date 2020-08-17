package com.es.phoneshop.web.servlets;

import com.es.phoneshop.model.cart.service.CartService;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.recentlyviewedproducts.service.RecentlyViewedProductsService;
import com.es.phoneshop.model.cart.service.DefaultCartService;
import com.es.phoneshop.model.recentlyviewedproducts.service.DefaultRecentlyViewedProductsService;
import com.es.phoneshop.model.exceptions.OutOfStockException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.recentlyviewedproducts.RecentlyViewedProducts;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class ProductDetailsPageServlet extends HttpServlet {

    private ProductDao productDao;
    private CartService cartService;
    private RecentlyViewedProductsService recentlyViewedProductsService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        recentlyViewedProductsService = DefaultRecentlyViewedProductsService.getInstance();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Long productId = parseProductId(request);
            RecentlyViewedProducts recentlyViewedProducts =
                    recentlyViewedProductsService.getRecentlyViewedProducts(request);
            recentlyViewedProductsService.addProduct(productId, recentlyViewedProducts);
            request.setAttribute("recentProducts", recentlyViewedProducts.getRecentlyViewedProducts());
            request.setAttribute("product", productDao.get(productId));
            request.setAttribute("cart", cartService.getCart(request.getSession()));
            request.getRequestDispatcher("/WEB-INF/pages/productDetails.jsp").forward(request, response);
        } catch (NoSuchElementException | NumberFormatException e) {
            response.sendError(404);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] productsIds = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");
        Map<Long, String> errors = new HashMap<>();

        for (int i = 0; i < productsIds.length; i++) {
            Long productId = Long.valueOf(productsIds[i]);

            int quantity;
            try {
                quantity = Integer.parseInt(quantities[i]);
                cartService.update(cartService.getCart(request.getSession()), productId, quantity);
            } catch (NumberFormatException | OutOfStockException e) {
                handleError(errors, productId, e);
            }
        }
        if (errors.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart?message=Cart updated successfully");
        } else {
            request.setAttribute("errors", errors);
            doGet(request, response);
        }
    }

    private void handleError(Map<Long, String> errors, Long productId, Exception e) {
        if (e.getClass().equals(NumberFormatException.class)) {
            errors.put(productId, "Not a number");
        } else {
            if (((OutOfStockException) e).getStockRequested() <= 0) {
                errors.put(productId, "Can't be negative or zero");
            } else {
                errors.put(productId, "Out of stock, max available" + ((OutOfStockException) e).getStockAvailable());
            }
        }
    }

    private Long parseProductId(HttpServletRequest request) {
        return Long.valueOf(request.getPathInfo().substring(1));
    }
}

