package com.es.phoneshop.dao;

import com.es.phoneshop.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AbstractGenericDAO<T extends Item> implements GenericDao<T> {
    protected List<T> items;
    protected final ReadWriteLock rwLock = new ReentrantReadWriteLock();


    protected AbstractGenericDAO() {
        items = new ArrayList<>();
    }

    protected void setItemList(List<T> items) {
        this.items = items;
    }

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
        findItemById(id).ifPresentOrElse((item) -> items.remove(item), () -> {
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
