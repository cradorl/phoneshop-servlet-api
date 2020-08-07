package com.es.phoneshop.model.product.dao;

import com.es.phoneshop.model.product.item.Item;

public interface Dao<T> {

    void save(T item);

    T get(Long id);

    void delete(Long id);

    <T extends Item> Long getId(T t);
}
