package com.es.phoneshop.web.servlets;

import com.es.phoneshop.model.cart.service.CartService;
import com.es.phoneshop.model.cart.service.DefaultCartService;
import com.es.phoneshop.model.enums.SortField;
import com.es.phoneshop.model.enums.SortOrder;
import com.es.phoneshop.model.exceptions.OutOfStockException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.ProductDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class ProductListPageServlet extends HttpServlet {

    private ProductDao productDao;
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        String sortField = request.getParameter("sort");
        String sortOrder = request.getParameter("order");
        request.setAttribute("products", productDao.findProducts(query,
                Optional.ofNullable(sortField).map(SortField::valueOf).orElse(null),
                Optional.ofNullable(sortOrder).map(SortOrder::valueOf).orElse(null)
        ));
        request.setAttribute("cart", cartService.getCart(request.getSession()));
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] productsIds = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");
        Map<Long, String> errors = new HashMap<>();

        for (int i = 0; i < productsIds.length; i++) {
            Long productId = Long.valueOf(productsIds[i]);

            int quantity;
            try {
                quantity = Integer.parseInt(quantities[i]);
                cartService.add(cartService.getCart(request.getSession()), productId, quantity);
            } catch (NumberFormatException | OutOfStockException e) {
                handleError(errors, productId, e);
            }
        }
        if (errors.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/products?"
                    + getQueryString(request) + "message=Product " + " added to cart");
        } else {
            request.setAttribute("errors", errors);
            doGet(request, response);
        }
    }

    private String getQueryString(HttpServletRequest request) {
        return !(request.getQueryString() == null) ? request.getQueryString() + "&" : "";
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
}
