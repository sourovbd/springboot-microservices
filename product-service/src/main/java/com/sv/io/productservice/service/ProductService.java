package com.sv.io.productservice.service;

import com.sv.io.productservice.model.ProductRequest;
import com.sv.io.productservice.model.ProductResponse;

public interface ProductService {
    long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long productId);

    void reduceQuantity(long productId, long quantity);
}
