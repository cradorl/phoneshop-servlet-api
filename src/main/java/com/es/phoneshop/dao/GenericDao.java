package com.es.phoneshop.dao;

import com.es.phoneshop.model.Item;

public interface GenericDao<T>{

    void save(T item);

    T get(Long id);

    void delete(Long id);

    <T extends Item> Long getId(T t);
}
