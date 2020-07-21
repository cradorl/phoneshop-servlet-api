package com.es.phoneshop.model.product;

public class ProductNotFoundException extends RuntimeException {
    private  Long productId;

    public ProductNotFoundException(Long productCode) {
        this.productId = productCode;
    }

    public Long getProductId() {
        return productId;
    }
}
