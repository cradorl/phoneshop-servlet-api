package com.es.phoneshop.model.product;

import com.es.phoneshop.model.exceptions.ProductNotFoundException;
import com.es.phoneshop.model.order.SortField;
import com.es.phoneshop.model.order.SortOrder;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;


public class ArrayListProductDao implements ProductDao {
    private static final String SEPARATOR = " ";
    private static ProductDao instance;
    private List<Product> products;
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    ArrayListProductDao() {
        this.products = new ArrayList<>();
    }

    public static synchronized ProductDao getInstance() {
        if (instance == null) {
            instance = new ArrayListProductDao();
        }
        return instance;
    }

    @Override
    public Product getProduct(Long id) throws ProductNotFoundException {
        lock.readLock().lock();
        try {
            return products
                    .stream()
                    .filter(product -> product.getId().equals(id))
                    .findAny()
                    .orElseThrow(()-> new ProductNotFoundException(id));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        lock.readLock().lock();
        try {
            Comparator<Product> comparator = getComparatorForProducts(query, sortField, sortOrder);
            return products.stream()
                    .filter(product -> query == null || query.isEmpty() || isProductContainsQuery(product, query))
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> product.getStock() > 0)
                    .sorted(comparator)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    private Comparator<Product> getComparatorForProducts(String query, SortField sortField, SortOrder sortOrder) {
        Comparator<Product> comparator = Comparator.comparing((Product product) -> (query != null && !query.isEmpty()
                ? getQueryRate(product, query) : 0))
                .reversed();

        if (sortField == SortField.description) {
            comparator = Comparator.comparing(Product::getDescription);
        } else if (sortField == SortField.price) {
            comparator = Comparator.comparing(Product::getPrice);
        } else if (sortOrder == SortOrder.desc) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

    private double getQueryRate(Product product, String query) {
        return (double) Arrays.stream(query.split(SEPARATOR))
                .filter(wordFromQuery -> product.getDescription().contains(wordFromQuery))
                .count() / product.getDescription().split(SEPARATOR).length;
    }

    private boolean isProductContainsQuery(Product product, String query) {
        return Arrays.stream(product.getDescription().split(SEPARATOR))
                .anyMatch(wordFromDescription -> Arrays.stream(query.split(SEPARATOR))
                        .anyMatch(wordFromDescription::contains));
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
