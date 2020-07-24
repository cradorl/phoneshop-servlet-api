package com.es.phoneshop.model.product;

import com.es.phoneshop.model.item.Item;

import java.math.BigDecimal;
import java.util.*;
public class Product extends Item {

    private String code;
    private String description;
    /**
     * null means there is no price because the product is outdated or new
     */
    private BigDecimal price;
    /**
     * can be null if the price is null
     */
    private Currency currency;
    private int stock;
    private String imageUrl;
    private List<ProductPriceHistory> priceHistoryList;

    public Product() {
    }

    public Product(Long id, String code, String description, BigDecimal price, Currency currency, int stock,
                   String imageUrl, List<ProductPriceHistory> productPriceHistoryList) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.priceHistoryList = productPriceHistoryList;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
        priceHistoryList.add(new ProductPriceHistory(new GregorianCalendar(), price));
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<ProductPriceHistory> getPriceHistoryList() {
        return priceHistoryList;
    }

    public void setPriceHistoryList(List<ProductPriceHistory> priceHistoryList) {
        this.priceHistoryList = priceHistoryList;
    }
}
