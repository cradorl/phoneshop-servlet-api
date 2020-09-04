package com.es.phoneshop.web.servlets;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exceptions.OutOfStockException;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.enums.SortField;
import com.es.phoneshop.model.enums.SortOrder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.Optional;

import static com.es.phoneshop.servletHelper.ServletHelper.*;


public class ProductListPageServlet extends HttpServlet {

    private ProductDao productDao;
    private CartService cartService;
    private static final String PRODUCT_LIST_JSP="/WEB-INF/pages/productList.jsp";

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
        request.getRequestDispatcher(PRODUCT_LIST_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = getProductIdIfExist(request, response, request.getParameter("productId"));

        try {
            int quantity = getQuantity(request.getParameter("quantity"), request);
            cartService.add(cartService.getCart(request.getSession()), productId, quantity);
        } catch (ParseException | OutOfStockException e) {
            handleError(productId, request, response, e);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/products?"
                + getQueryString(request) + "message=Product " + productId +" added to cart");
    }

    private String getQueryString(HttpServletRequest request) {
        return !(request.getQueryString() == null) ? request.getQueryString() + "&" : "";
    }

    private void handleError(Long productId, HttpServletRequest request, HttpServletResponse response, Exception e) throws ServletException, IOException {
        Map<Long, String> errorAttributes = mapErrors(productId, e);
        request.setAttribute("errors", errorAttributes);
        doGet(request, response);
    }
}
