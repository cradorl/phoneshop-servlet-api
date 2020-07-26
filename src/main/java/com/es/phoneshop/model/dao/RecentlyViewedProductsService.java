package com.es.phoneshop.model.dao;

import com.es.phoneshop.model.recentlyviewedproducts.RecentlyViewedProducts;

import javax.servlet.http.HttpServletRequest;

public interface RecentlyViewedProductsService {
    RecentlyViewedProducts getRecentlyViewedProducts(HttpServletRequest request);

    void addProduct(Long productId, RecentlyViewedProducts recentlyViewedProducts);
}
