package com.es.phoneshop.model.recentlyviewedproducts.service;

import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.recentlyviewedproducts.RecentlyViewedProducts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Deque;
import java.util.Optional;

public class DefaultRecentlyViewedProductsService implements RecentlyViewedProductsService {

    private ProductDao productDao;
    private static final String RESENT_VIEWS_ATTRIBUTE="recentViews";

    private DefaultRecentlyViewedProductsService() {
        productDao= ArrayListProductDao.getInstance();
    }

    public static DefaultRecentlyViewedProductsService getInstance(){
        return Holder.instance;
    }

    private  static class Holder{
        private  static final  DefaultRecentlyViewedProductsService instance= new DefaultRecentlyViewedProductsService();
    }

    @Override
    public synchronized RecentlyViewedProducts getRecentlyViewedProducts(HttpServletRequest request) {
        HttpSession session=request.getSession();
        RecentlyViewedProducts recentlyViewedProducts=
                (RecentlyViewedProducts)session.getAttribute(RESENT_VIEWS_ATTRIBUTE);
        if(recentlyViewedProducts==null){
            recentlyViewedProducts=new RecentlyViewedProducts();
            session.setAttribute(RESENT_VIEWS_ATTRIBUTE, recentlyViewedProducts);
        }
        return  recentlyViewedProducts;
    }

    @Override
    public synchronized void addProduct(Long productId, RecentlyViewedProducts recentlyViewedProducts) {
        Product product=productDao.get(productId);
        Deque<Product> recentViewsProductList=recentlyViewedProducts.getRecentlyViewedProducts();

        Optional<Product> productOptional=recentViewsProductList.stream()
                .filter(p -> productId.equals(p.getId()))
                .findAny();

        recentViewsProductList.addFirst(product);
        if(productOptional.isPresent()){
            recentViewsProductList.removeFirstOccurrence(product);
        }else if(recentViewsProductList.size()>3){
            recentViewsProductList.removeLast();
        }
    }
}
