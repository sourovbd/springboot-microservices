package com.sv.io.productservice.service;

import com.sv.io.productservice.entity.Product;
import com.sv.io.productservice.exception.ProductServiceCustomException;
import com.sv.io.productservice.model.ProductRequest;
import com.sv.io.productservice.model.ProductResponse;
import com.sv.io.productservice.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Override
    public long addProduct(ProductRequest productRequest) {
        log.info("Adding product..");
        Product product = Product.builder()
                .productName(productRequest.getName())
                .quantity(productRequest.getQuantity())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);

        log.info("Product is created..");
        return product.getProductId();
    }

    @Override
    public ProductResponse getProductById(long productId) {
        log.info("Get the product for productId: "+productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceCustomException("Product with this id is not available: "+productId, "PRODUCT_NOT_FOUND"));

        return ProductResponse
                .builder()
                .productName(product.getProductName())
                .productId(product.getProductId())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }

    @Override
    public void reduceQuantity(long productId, long quantity) {
        log.info("Reduce quantity {} for id: {}",quantity, productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceCustomException("Product with given id is not found", "PRODUCT_NOT_FOUND"));

        if (product.getQuantity() < quantity) {
            throw new ProductServiceCustomException("Current product quantity is less then required quantity",
                    "INSUFFICIENT_QUANTITY");
        }

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
        log.info("Product quantity updated successfully!");
    }
}
