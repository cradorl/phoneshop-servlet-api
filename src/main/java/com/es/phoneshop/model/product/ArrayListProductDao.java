package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.enums.SortField;
import com.es.phoneshop.model.enums.SortOrder;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class ArrayListProductDao extends AbstractDAO<Product> implements ProductDao {
    private ArrayListProductDao() {
    }

    public static ArrayListProductDao getInstance() {
        return Holder.instance;
    }

    private static class Holder {
        private static final ArrayListProductDao instance = new ArrayListProductDao();
    }

    private List<Product> defaultSearch() {
        return items.stream()
                .filter(product -> product.getPrice() != null)
                .filter(product -> product.getStock() > 0)
                .collect(Collectors.toList());
    }

    private List<Product> querySearch(String query) {
        String[] words = query.split(" ");
        return defaultSearch().stream()
                .collect(Collectors.toMap(Function.identity(), product -> Arrays.stream(words)
                        .filter(word -> product.getDescription().contains(word))
                        .count()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() != 0)
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private List<Product> sortProducts(SortField sortField, SortOrder sortOrder, List<Product> productList) {
        Comparator<Product> comparator = null;
        switch (sortField) {
            case description:
                comparator = Comparator.comparing(product -> product.getDescription().toLowerCase());
                break;
            case price:
                comparator = Comparator.comparing(Product::getPrice);
                break;
        }

        if (sortOrder == SortOrder.desc) {
            comparator = comparator.reversed();
        }

        productList.sort(comparator);
        return productList;
    }

    @Override
    public synchronized List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        if (query != null && sortField == null) {
            return querySearch(query);
        } else if (query == null && sortField == null) {
            return defaultSearch();
        } else if (query == null) {
            return sortProducts(sortField, sortOrder, defaultSearch());
        } else
            return sortProducts(sortField, sortOrder, querySearch(query));
    }

    public List<Product> getItems() {
        return items;
    }

}
