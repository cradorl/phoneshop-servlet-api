package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.dao.Dao;
import com.es.phoneshop.model.product.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public abstract class AbstractDAO<T extends Item> implements Dao<T> {
    List<T> items = new ArrayList<>();

    @Override
    public synchronized void save(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Item should not be null");
        }
        try {
            get(getId(item));
            throw new IllegalArgumentException("Item already exists");
        } catch (NoSuchElementException e) {
            items.add(item);
        }
    }

    @Override
    public synchronized T get(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id should not be null");
        }
        return findItemById(id)
                .orElseThrow(() -> new NoSuchElementException("Item with id " + id + " was not found"));
    }

    @Override
    public synchronized void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id should not be null");
        }
        findItemById(id).ifPresentOrElse((item) -> items.remove(item), () ->{
            throw new NoSuchElementException("Item with id " + id + " was not found");
        });
    }

    public <T extends Item> Long getId(T t) {
        return t.getId();
    }

    private Optional<T> findItemById(Long id) {
        return items.stream().
                filter(item -> getId(item).equals(id))
                .findAny();
    }
}
