package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;


public class ArrayListProductDao implements ProductDao {
    private List<Product> products;
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public ArrayListProductDao() {
        products = new ArrayList<>();
    }

    @Override
    public Product getProduct(Long id) throws NoSuchElementException {
        lock.readLock().lock();
        try {
            return products
                    .stream()
                    .filter(product -> product.getId().equals(id))
                    .findFirst()
                    .get(); //если продукт не будет найден
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        lock.readLock().lock();
        try {
            Comparator<Product> comparator = Comparator.comparing(product -> {
                if (sortField != null && SortField.description == sortField) {
                    return (Comparable) product.getDescription();
                } else {
                    return (Comparable) product.getPrice();
                }
            });

            return products.stream()
                    .filter(product -> query==null || query.isEmpty() || product.getDescription().contains(query))
                    .filter(product -> product.getPrice()!=null)
                    .filter(product -> product.getStock()>0)
                    .collect(Collectors.toList());
                    //.collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void save(Product product) {
        lock.readLock().lock();
        try {
            if (products.stream()
                    .anyMatch(product1 -> product1.getId()
                            .equals(product.getId()))) {
                products.remove(product);
            }
            products.add(product);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void delete(Long id) {
        lock.readLock().lock();
        try {
            products.removeIf(product -> product.getId().equals(id));
        } finally {
            lock.readLock().unlock();
        }
    }
}
