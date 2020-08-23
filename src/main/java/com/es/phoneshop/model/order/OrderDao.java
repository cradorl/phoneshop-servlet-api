package com.es.phoneshop.model.order;

public interface OrderDao {
    Order getOrder(Long id) throws OrderNotFoundException;

    void save(Order order) throws OrderNotFoundException;

    Order getOrderBySecureId(String secureId) throws  OrderNotFoundException;

}
