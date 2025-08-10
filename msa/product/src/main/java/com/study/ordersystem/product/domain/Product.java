package com.study.ordersystem.product.domain;

import com.study.ordersystem.common.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private Integer price;

	private Integer stockQuantity;

	@Column(nullable = false)
	private Long memberId;

	@Builder
	private Product(String name, Integer price, Integer stockQuantity, Long memberId) {
		this.name = name;
		this.price = price;
		this.stockQuantity = stockQuantity;
		this.memberId = memberId;
	}

	public void updateStockQuantity(int stockQuantity) {
		this.stockQuantity -= stockQuantity;
	}
}
