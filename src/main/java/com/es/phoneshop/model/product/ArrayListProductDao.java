package com.es.phoneshop.model.product;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {
    private static List<Product> products = new ArrayList<>();

    @Override
    public Product getProduct(Long id) {
        return products.
                stream().
                filter(product -> product.getId().equals(id)).
                findFirst().
                get();
    }

    @Override
    public List<Product> findProducts() {
        return products.stream()
                .filter(x -> x.getPrice() != null && x.getStock() > 0)
                .collect(Collectors.toList());
    }

    @Override
    public void save(Product product) {
        if (products.stream()
                .noneMatch(product1 -> product1.getId()
                        .equals(product.getId()))) {
            products.add(product);
        } else {
            products.remove(product);
            products.add(product);
        }
    }

    @Override
    public void delete(Long id) {
        Product product = getProduct(id);
        if (product != null) {
            products.remove(product);
        } else {
            throw new NoSuchElementException("Does not contain such product");
        }
    }
}
