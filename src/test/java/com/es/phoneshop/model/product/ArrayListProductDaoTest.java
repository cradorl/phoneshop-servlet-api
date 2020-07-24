package com.es.phoneshop.model.product;

import com.es.phoneshop.model.order.SortField;
import com.es.phoneshop.model.order.SortOrder;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ArrayListProductDaoTest {
    private ArrayListProductDao productDao;

    @Mock
    private Product product1;
    @Mock
    private Product product2;
    @Mock
    private Product product3;
    @Mock
    private Product product4;

    private static final String QUERY = "Apple n";
    private static final SortField PRICE_SORT_FIELD = SortField.price;
    private static final SortField DESCRIPTION_SORT_FIELD = SortField.description;
    private static final SortOrder DESC_SORT_ORDER = SortOrder.desc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        productDao = ArrayListProductDao.getInstance();
        productDao.getItems().add(product1);
        productDao.getItems().add(product3);
        productDao.getItems().add(product4);

        when(product1.getId()).thenReturn(1L);
        when(product1.getPrice()).thenReturn(new BigDecimal(100));
        when(product1.getStock()).thenReturn(100);
        when(product1.getDescription()).thenReturn("Samsung Galaxy S");

        when(product2.getId()).thenReturn(2L);

        when(product3.getId()).thenReturn(3L);
        when(product3.getPrice()).thenReturn(null);
        when(product3.getStock()).thenReturn(300);


        when(product4.getId()).thenReturn(4L);
        when(product4.getPrice()).thenReturn(new BigDecimal(10));
        when(product4.getStock()).thenReturn(30);
        when(product4.getDescription()).thenReturn("Apple iPhone 6");
    }

    @After
    public void clearProductList() {
        productDao.getItems().clear();
    }

    @Test
    public void testGetProduct() {
        assertEquals(product1.getId(), productDao.get(1L).getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetProductNullId() {
        productDao.get(null);
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetNonExistentProduct() {
        productDao.get(2L);
    }

    @Test
    public void testFindProducts() {
        assertEquals(productDao.findProducts(null, null, null),
                Arrays.asList(product1, product4));
    }

    @Test
    public void testFindProductsQuerySearch() {
        assertEquals(productDao.findProducts(QUERY, null, null), Arrays.asList(product4, product1));
    }

    @Test
    public void testFindProductsQuerySearchWithSorting() {
        assertEquals(productDao.findProducts(QUERY, PRICE_SORT_FIELD, DESC_SORT_ORDER), Arrays.asList(product1, product4));
    }

    @Test
    public void testFindProductsWithSorting() {
        assertEquals(productDao.findProducts(null, PRICE_SORT_FIELD, DESC_SORT_ORDER), Arrays.asList(product1, product4));
    }

    @Test
    public void testFindProductsWithSortingOnDescription() {
        assertEquals(productDao.findProducts(null, DESCRIPTION_SORT_FIELD, DESC_SORT_ORDER), Arrays.asList(product1, product4));
    }

    @Test
    public void testSave() {
        productDao.save(product2);
        assertTrue(productDao.getItems().contains(product2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveAlreadyExistentProduct() {
        productDao.save(product1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveProductNullId() {
        productDao.save(null);
    }

    @Test
    public void testDelete() {
        productDao.delete(1L);
        productDao.delete(3L);
        productDao.delete(4L);
        assertTrue(productDao.getItems().isEmpty());
    }

    @Test(expected = NoSuchElementException.class)
    public void testDeleteNonExistentProduct() {
        productDao.delete(5L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteProductNullId() {
        productDao.delete(null);
    }
}
