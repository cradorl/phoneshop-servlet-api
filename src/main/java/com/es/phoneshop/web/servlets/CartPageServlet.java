package com.es.phoneshop.web.servlets;

import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.exceptions.OutOfStockException;
import com.es.phoneshop.servletHelper.ServletHelper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class CartPageServlet extends HttpServlet {

    private CartService cartService;
    private static final String CART_JSP = "/WEB-INF/pages/cart.jsp";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("cart", cartService.getCart(request.getSession()));
        request.getRequestDispatcher(CART_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] productIds = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");
        Map<Long, String> errorAttributes = new HashMap<>();

        if (productIds != null) {
            for (int i = 0; i < productIds.length; i++) {
                Long productId = ServletHelper.getProductIdIfExist(request, response, productIds[i]);
                try {
                    int quantity = ServletHelper.getQuantity(quantities[i], request);
                    cartService.update(cartService.getCart(request.getSession()), productId, quantity);
                } catch (ParseException | OutOfStockException e) {
                    errorAttributes = ServletHelper.mapErrors(productId, e);
                }
            }
        }
        handleErrors(request, response, errorAttributes);
    }

    private void handleErrors(HttpServletRequest request, HttpServletResponse response, Map<Long, String> errorAttributes) throws IOException, ServletException {
        if (errorAttributes.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart?message=Cart updated successfully");
        } else {
            request.setAttribute("errors", errorAttributes);
            request.setAttribute("cart", cartService.getCart(request.getSession()));
            request.getRequestDispatcher(CART_JSP).forward(request, response);
        }
    }
}


