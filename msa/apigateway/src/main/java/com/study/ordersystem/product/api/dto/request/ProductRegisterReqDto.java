package com.study.ordersystem.product.api.dto.request;

import com.study.ordersystem.member.domain.Member;
import com.study.ordersystem.product.domain.Product;

public record ProductRegisterReqDto(
	String name,
	int price,
	int stockQuantity
) {
	public Product toEntity(Member member) {
		return Product.builder()
			.name(this.name)
			.price(this.price)
			.stockQuantity(this.stockQuantity)
			.member(member)
			.build();
	}
}
