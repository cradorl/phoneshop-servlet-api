package com.es.phoneshop.model.order;

import com.es.phoneshop.model.product.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class ArrayListOrderDao implements OrderDao {
    private List<Order> orderList;
    private long orderId;
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

    private ArrayListOrderDao() {
        orderList = new ArrayList<>();
    }

    private static class SingletonHelper {
        private static final ArrayListOrderDao INSTANCE = new ArrayListOrderDao();
    }

    public static ArrayListOrderDao getInstance() {
        return SingletonHelper.INSTANCE;
    }

    protected void setProductList(List<Product> productList) {
        this.orderList = orderList;
    }

    @Override
    public Order getOrder(Long id) throws OrderNotFoundException {
        Lock readLock = rwLock.readLock();
        readLock.lock();
        try {
            return orderList.stream()
                    .filter((order) -> id.equals(order.getId()))
                    .findAny()
                    .orElseThrow(OrderNotFoundException::new);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void save(Order order) throws OrderNotFoundException {
        Lock writeLock = rwLock.writeLock();
        writeLock.lock();
        try {
            Long id = order.getId();
            if (id != null) {
                orderList.remove(getOrder(id));
                orderList.add(order);
            } else {
                order.setId(++orderId);
                orderList.add(order);
            }
        } finally {
            writeLock.unlock();
        }
    }
}
