package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest {
    private ProductDao productDao;
    private List<Product> products;
    private static final Product PRODUCT = new Product(30L, "sgs", "Samsung Galaxy S III", new BigDecimal(100), Currency.getInstance("USD"), 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
        products = new ArrayList<>();
    }

    @Test
    public void testFindProductsNoResults() {
        assertTrue(productDao.findProducts().isEmpty());
    }

    @Test
    public void testGetProduct() {
        Currency usd = Currency.getInstance("USD");
        productDao.save(new Product(12L, "sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        productDao.save(PRODUCT);
        productDao.save(new Product(40L, "iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"));

        Long id = PRODUCT.getId();
        Product product = productDao.getProduct(id);
        assertEquals(product, PRODUCT);
    }

    @Test
    public void testFindProducts() {
        productDao.save(PRODUCT);
        products.add(PRODUCT);
        assertEquals(productDao.findProducts(), products);
    }

    @Test
    public void testSaveProduct() {
        products.add(PRODUCT);
        productDao.save(PRODUCT);
        assertEquals(productDao.findProducts(), products);
    }

    @Test
    public void testDeleteProduct() {
        productDao.save(PRODUCT);
        Long id = PRODUCT.getId();
        productDao.delete(id);

        assertTrue(productDao.findProducts().isEmpty());
    }
}
