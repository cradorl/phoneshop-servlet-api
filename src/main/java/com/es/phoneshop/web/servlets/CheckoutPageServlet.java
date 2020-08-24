package com.es.phoneshop.web.servlets;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.service.CartService;
import com.es.phoneshop.model.cart.service.DefaultCartService;
import com.es.phoneshop.model.order.DefaultOrderService;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderService;
import com.es.phoneshop.model.order.PaymentMethod;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CheckoutPageServlet extends HttpServlet {

    private CartService cartService;
    private OrderService orderService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
        orderService = DefaultOrderService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request.getSession());
        request.setAttribute("order", orderService.getOrder(cart));
        request.setAttribute("paymentMethods", orderService.getPaymentMethods());
        request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request.getSession());
        Order order = orderService.getOrder(cart);
        Map<String, String> errors = new HashMap<>();

        setRequiredParameters(request, "firstName", errors, order::setFirstName);
        setRequiredParameters(request, "lastName", errors, order::setFirstName);
        setRequiredParameters(request, "phone", errors, order::setFirstName);
        setRequiredDateParameter(request, errors, order);
        setRequiredParameters(request, "deliveryAddress", errors, order::setFirstName);
        setPaymentMethod(request, errors, order);

        handleError(request, response, errors, order, cart);
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, Map<String, String> errors,
                             Order order, Cart cart) throws IOException, ServletException {
        if (errors.isEmpty()) {
            orderService.placeOrder(order);
            cartService.clear(cart);
            response.sendRedirect(request.getContextPath() + "/order/overview/" + order.getSecureId());
        } else {
            request.setAttribute("errors", errors);
            request.setAttribute("order", order);
            request.setAttribute("paymentMethods", orderService.getPaymentMethods());
            request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
        }
    }

    private void setRequiredParameters(HttpServletRequest request, String parameter, Map<String, String> errors, Consumer<String> consumer) {
        String value = request.getParameter(parameter);
        if (value == null || value.isEmpty()) {
            errors.put(parameter, "Value is required");
        } else {
            consumer.accept(value);
        }
    }

    private void setPaymentMethod(HttpServletRequest request, Map<String, String> errors, Order order) {
        String value = request.getParameter("paymentMethod");
        if (value == null || value.isEmpty()) {
            errors.put("paymentMethod", "Value is required");
        } else {
            order.setPaymentMethod(PaymentMethod.valueOf(value));
        }
    }
    private boolean isNotEmpty(String parameter, Map<String, String> errorAttributes, String value) {
        if (value.isEmpty()) {
            errorAttributes.put(parameter, "Value is required");
            return false;
        }
        return true;
    }
    private void setRequiredDateParameter(HttpServletRequest request,
                                          Map<String, String> errorAttributes, Order order) {
        String value = request.getParameter("deliveryDate");
        if (isNotEmpty("deliveryDate", errorAttributes, value))
        {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            Date dateValue = null;
            try {
                dateValue = format.parse(value);
            } catch (ParseException e) {
                errorAttributes.put("deliveryDate", "Wrong format, should be: dd-MM-yyyy");
            }
            order.setDeliveryDate(dateValue);
        }
    }
}

