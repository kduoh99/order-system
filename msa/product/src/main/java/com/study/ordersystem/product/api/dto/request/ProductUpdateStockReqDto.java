package com.study.ordersystem.product.api.dto.request;

public record ProductUpdateStockReqDto(
	Long productId,
	Integer productQuantity
) {
}
