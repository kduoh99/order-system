package com.study.ordersystem.ordering.domain;

import com.study.ordersystem.common.entity.BaseTimeEntity;
import com.study.ordersystem.member.domain.Member;
import com.study.ordersystem.product.domain.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Ordering extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;


	@Column(nullable = false)
	private Integer quantity;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	@Builder
	private Ordering(Member member, Product product, Integer quantity, OrderStatus orderStatus) {
		this.member = member;
		this.product = product;
		this.quantity = quantity;
		this.orderStatus = orderStatus;
	}
}
