package com.study.ordersystem.member.api.dto.response;

import lombok.Builder;

@Builder
public record LoginResDto(
	Long id,
	String accessToken,
	String refreshToken
) {
	public static LoginResDto of(Long id, String accessToken, String refreshToken) {
		return LoginResDto.builder()
			.id(id)
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}
}
