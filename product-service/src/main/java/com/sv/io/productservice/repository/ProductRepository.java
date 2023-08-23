package com.sv.io.productservice.repository;

import com.sv.io.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    //long findQuantityByProductId(long quantity, long productId);

    //Product findByProductId(long productId);
}
