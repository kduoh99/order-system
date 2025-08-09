package com.study.ordersystem.product.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.ordersystem.product.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
