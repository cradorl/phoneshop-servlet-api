package com.es.phoneshop.web.servlets;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductPriceHistoryPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ArrayListProductDao productDao;
    @Mock
    private Product product1;

    @InjectMocks
    private ProductPriceHistoryPageServlet servlet = new ProductPriceHistoryPageServlet();

    @Before
    public void setup() {
        when(request.getRequestDispatcher("/WEB-INF/pages/productHistory.jsp")).thenReturn(requestDispatcher);
        when(productDao.get(1L)).thenReturn(product1);
        when(request.getPathInfo()).thenReturn("/1");

        when(productDao.get(2L)).thenThrow(NoSuchElementException.class);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(productDao).get(1L);
        verify(request).setAttribute("product", productDao.get(1L));
        verify(requestDispatcher).forward(request, response);
    }
}
