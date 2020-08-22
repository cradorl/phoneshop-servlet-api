package com.es.phoneshop.model.product.dao;

import com.es.phoneshop.model.order.SortField;
import com.es.phoneshop.model.order.SortOrder;
import com.es.phoneshop.model.product.Product;

import java.util.List;

public interface ProductDao extends Dao<Product> {
    Product get(Long id);

    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);

    void save(Product product);

    void delete(Long id);
}
