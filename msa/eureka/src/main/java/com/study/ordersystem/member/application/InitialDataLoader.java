package com.study.ordersystem.member.application;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.study.ordersystem.member.domain.Member;
import com.study.ordersystem.member.domain.Role;
import com.study.ordersystem.member.domain.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InitialDataLoader implements CommandLineRunner {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		if (memberRepository.findByEmail("admin@naver.com").isPresent())
			return;

		memberRepository.save(Member.builder()
			.name("admin")
			.email("admin@naver.com")
			.password(passwordEncoder.encode("12341234"))
			.role(Role.ADMIN)
			.build());
	}
}
