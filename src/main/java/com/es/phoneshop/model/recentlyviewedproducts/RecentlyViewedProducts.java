package com.es.phoneshop.model.recentlyviewedproducts;

import com.es.phoneshop.model.product.Product;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class RecentlyViewedProducts {

    private Deque<Product> recentlyViewedProducts = new ConcurrentLinkedDeque<>();

    public RecentlyViewedProducts() {
    }

    public RecentlyViewedProducts(Deque<Product> recentlyViewedProducts) {
        this.recentlyViewedProducts = recentlyViewedProducts;
    }

    public Deque<Product> getRecentlyViewedProducts() {
        return recentlyViewedProducts;
    }

    public void setRecentlyViewedProducts(Deque<Product> recentlyViewedProducts) {
        this.recentlyViewedProducts = recentlyViewedProducts;
    }
}
