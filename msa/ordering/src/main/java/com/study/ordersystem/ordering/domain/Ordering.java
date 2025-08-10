package com.study.ordersystem.ordering.domain;

import com.study.ordersystem.common.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Ordering extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long memberId;

	@Column(nullable = false)
	private Long productId;

	@Column(nullable = false)
	private Integer quantity;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	@Builder
	private Ordering(Long memberId, Long productId, Integer quantity, OrderStatus orderStatus) {
		this.memberId = memberId;
		this.productId = productId;
		this.quantity = quantity;
		this.orderStatus = orderStatus;
	}
}
