package com.study.ordersystem.ordering.api.dto;

import lombok.Builder;

@Builder
public record ProductUpdateStockDto(
	Long productId,
	Integer productQuantity
) {
}
