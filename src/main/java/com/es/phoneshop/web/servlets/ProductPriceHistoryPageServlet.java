package com.es.phoneshop.web.servlets;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.ProductDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;

public class ProductPriceHistoryPageServlet extends HttpServlet {
    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Long productId = Long.valueOf(request.getPathInfo().substring(1));
            request.setAttribute("product", productDao.get(productId));
            request.getRequestDispatcher("/WEB-INF/pages/productHistory.jsp").forward(request, response);
        } catch (NoSuchElementException | NumberFormatException e) {
            response.sendError(404);
        }
    }
}
