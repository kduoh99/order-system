package com.study.ordersystem.ordering.api.dto.request;

public record OrderCreateReqDto(
	Long productId,
	Integer productCount
) {
}
