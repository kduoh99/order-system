package com.study.ordersystem.ordering.api.dto;

public record ProductDto(
	Long id,
	String name,
	int price,
	int stockQuantity
) {
}
