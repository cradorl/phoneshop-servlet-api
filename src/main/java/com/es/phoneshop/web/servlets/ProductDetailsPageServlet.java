package com.es.phoneshop.web.servlets;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.dao.CartService;
import com.es.phoneshop.model.dao.ProductDao;
import com.es.phoneshop.model.dao.RecentlyViewedProductsService;
import com.es.phoneshop.model.dao.impl.DefaultCartService;
import com.es.phoneshop.model.dao.impl.DefaultRecentlyViewedProductsService;
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
        recentlyViewedProductsService=DefaultRecentlyViewedProductsService.getInstance();
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
            request.setAttribute("cart", cartService.getCart(request));
            request.getRequestDispatcher("/WEB-INF/pages/productDetails.jsp").forward(request, response);
        } catch (NoSuchElementException | NumberFormatException e) {
            response.sendError(404);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quantityString = request.getParameter("quantity");
        Long productId = parseProductId(request);
        int quantity;
        try {
            NumberFormat format=NumberFormat.getInstance(request.getLocale());
            quantity = format.parse(quantityString).intValue();
        } catch (NoSuchElementException | NumberFormatException | ParseException e) {
            request.setAttribute("error", "Not a number");
            doGet(request, response);
            return;
        }
        Cart cart=cartService.getCart(request);
        try {
            cartService.add(cart, productId, quantity);
        } catch (OutOfStockException e) {
            request.setAttribute("error", "Out of stock, available"+e.getStockAvailable());
            doGet(request, response);
            return;
        }
        response.sendRedirect(request.getContextPath()+"/products/" +productId+"?message=Product added to cart");
    }

    private Long parseProductId(HttpServletRequest request) {
        return Long.valueOf(request.getPathInfo().substring(1));
    }
}


