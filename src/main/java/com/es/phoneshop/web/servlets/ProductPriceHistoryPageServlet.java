package com.es.phoneshop.web.servlets;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;

public class ProductPriceHistoryPageServlet extends HttpServlet {
    private ProductDao productDao;
    private static final String PRODUCT_HISTORY_JSP = "/WEB-INF/pages/productHistory.jsp";

    @Override
    public void init(ServletConfig config) {
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Long productId = Long.valueOf(request.getPathInfo().substring(1));
            request.setAttribute("product", productDao.get(productId));
            request.getRequestDispatcher(PRODUCT_HISTORY_JSP).forward(request, response);
        } catch (NoSuchElementException | NumberFormatException e) {
            response.sendError(404);
        }
    }
}
