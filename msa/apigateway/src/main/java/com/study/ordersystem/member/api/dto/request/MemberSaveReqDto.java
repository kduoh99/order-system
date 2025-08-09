package com.study.ordersystem.member.api.dto.request;

import com.study.ordersystem.member.domain.Member;
import com.study.ordersystem.member.domain.Role;

public record MemberSaveReqDto(
	String name,
	String email,
	String password
) {
	public Member toEntity(String encodedPassword) {
		return Member.builder()
			.name(this.name)
			.email(this.email)
			.password(encodedPassword)
			.role(Role.USER)
			.build();
	}
}
