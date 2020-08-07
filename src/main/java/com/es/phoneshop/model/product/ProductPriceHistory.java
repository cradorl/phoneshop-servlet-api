package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;

public class ProductPriceHistory {
    private Calendar date;
    private BigDecimal price;

    public ProductPriceHistory() {
    }

    public ProductPriceHistory(Calendar date, BigDecimal price) {
        this.date = date;
        this.price = price;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
