package com.study.ordersystem.member.api.dto.request;

public record LoginReqDto(
	String email,
	String password
) {
}
