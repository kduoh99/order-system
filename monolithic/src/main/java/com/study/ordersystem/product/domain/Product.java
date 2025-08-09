package com.study.ordersystem.product.domain;

import com.study.ordersystem.common.entity.BaseTimeEntity;
import com.study.ordersystem.member.domain.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Builder
	private Product(String name, Integer price, Integer stockQuantity, Member member) {
		this.name = name;
		this.price = price;
		this.stockQuantity = stockQuantity;
		this.member = member;
	}

	public void updateStockQuantity(int stockQuantity) {
		this.stockQuantity -= stockQuantity;
	}
}
