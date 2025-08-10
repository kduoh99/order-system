package com.study.ordersystem.product.api.dto.response;

import lombok.Builder;

@Builder
public record ProductResDto(
	Long id,
	String name,
	int price,
	int stockQuantity
) {
	public static ProductResDto of(Long id, String name, int price, int stockQuantity) {
		return ProductResDto.builder()
			.id(id)
			.name(name)
			.price(price)
			.stockQuantity(stockQuantity)
			.build();
	}
}
