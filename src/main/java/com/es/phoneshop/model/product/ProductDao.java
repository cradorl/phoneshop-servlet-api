package com.es.phoneshop.model.product;

import com.es.phoneshop.model.exceptions.ProductNotFoundException;
import com.es.phoneshop.model.order.SortField;
import com.es.phoneshop.model.order.SortOrder;

import java.util.List;

public interface ProductDao {
    Product getProduct(Long id) throws ProductNotFoundException;
    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);
    void save(Product product);
    void delete(Long id);
}
