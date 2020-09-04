package com.es.phoneshop.exceptions;

public class ProductNotFoundException extends RuntimeException {
    private Long productId;

    public ProductNotFoundException(Long productCode) {
        this.productId = productCode;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
