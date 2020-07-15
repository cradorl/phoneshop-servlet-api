package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class ArrayListProductDao implements ProductDao {
    private List<Product> products;
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public ArrayListProductDao() { products = new ArrayList<>();}

    public ArrayListProductDao(boolean initWithSampleProducts)
    {
        products = new ArrayList<>();
        if (initWithSampleProducts)
            saveSampleProducts();

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
    public List<Product> findProducts() {
        lock.readLock().lock();
        try {
            return products;
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

    private List<Product> saveSampleProducts() {
        List<Product> result = new ArrayList<>();
        Currency usd = Currency.getInstance("USD");
        result.add(new Product(1L, "sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        result.add(new Product(2L, "sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        result.add(new Product(3L, "sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        result.add(new Product(4L, "iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"));
        result.add(new Product(5L, "iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg"));
        result.add(new Product(6L, "htces4g", "HTC EVO Shift 4G", new BigDecimal(320), usd, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg"));
        result.add(new Product(7L, "sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg"));
        result.add(new Product(8L, "xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg"));
        result.add(new Product(9L, "nokia3310", "Nokia 3310", new BigDecimal(70), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg"));
        result.add(new Product(10L, "palmp", "Palm Pixi", new BigDecimal(170), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg"));
        result.add(new Product(11L, "simc56", "Siemens C56", new BigDecimal(70), usd, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg"));
        result.add(new Product(12L, "simc61", "Siemens C61", new BigDecimal(80), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg"));
        result.add(new Product(13L, "simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));

        return result;
    }
}
