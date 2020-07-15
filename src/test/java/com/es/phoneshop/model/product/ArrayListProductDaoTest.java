package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest {
    private ProductDao productDao;
    private List<Product> testProducts;
    private Currency usd;
    private static final Product TEST_PRODUCT = new Product(33L, "test3", "4", new BigDecimal(30), Currency.getInstance("USD"), 57, "https://3.jpg");

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
        testProducts = new ArrayList<>();
        usd = Currency.getInstance("USD");
    }

    @Test
    public void testFindProductsNoResults() {
        assertTrue(productDao.findProducts().isEmpty());
    }

    @Test
    public void testGetProduct() {
        productDao.save(TEST_PRODUCT);
        assertEquals(productDao.getProduct(TEST_PRODUCT.getId()), TEST_PRODUCT);
    }

    @Test
    public void testFindProducts() {
        productDao.save(TEST_PRODUCT);
        testProducts.add(TEST_PRODUCT);

        assertEquals(productDao.findProducts(), testProducts);
    }

    @Test
    public void testSaveProduct() {
        Product productToSave = new Product(20L, "sgs_new", "New Product Test", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg");
        productDao.save(productToSave);
        assertTrue(productToSave.getId() > 0);
        Product actualProduct = productDao.getProduct(productToSave.getId());
        assertEquals(productToSave, actualProduct);

        productToSave = new Product(15L, "sgs_updated", "Updated Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg");
        productDao.save(productToSave);
        actualProduct = productDao.getProduct(productToSave.getId());
        assertEquals(productToSave, actualProduct);
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testDeleteProduct() {
        Product productToDelete = new Product(20L, "sgs_new", "New Product Test", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg");
        productDao.delete(productToDelete.getId());
        exception.expect(NoSuchElementException.class);
        productDao.getProduct(productToDelete.getId());
    }
}
