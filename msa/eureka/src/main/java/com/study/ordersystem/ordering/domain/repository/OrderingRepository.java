package com.study.ordersystem.ordering.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.ordersystem.ordering.domain.Ordering;

@Repository
public interface OrderingRepository extends JpaRepository<Ordering, Long> {
}
