package com.study.ordersystem.member.application;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.ordersystem.member.api.dto.request.LoginReqDto;
import com.study.ordersystem.member.api.dto.request.MemberRefreshReqDto;
import com.study.ordersystem.member.api.dto.request.MemberSaveReqDto;
import com.study.ordersystem.member.api.dto.response.LoginResDto;
import com.study.ordersystem.member.domain.Member;
import com.study.ordersystem.member.domain.repository.MemberRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;

	@Qualifier("rtdb")
	private final RedisTemplate<String, Object> redisTemplate;

	@Value("${jwt.secret-key-rt}")
	private String secretKeyRt;

	public Long save(MemberSaveReqDto memberSaveReqDto) {
		if (memberRepository.findByEmail(memberSaveReqDto.email()).isPresent()) {
			throw new IllegalArgumentException("이미 존재하는 회원입니다.");
		}

		Member member = memberRepository.save(memberSaveReqDto.toEntity(
			passwordEncoder.encode(memberSaveReqDto.password())
		));

		return member.getId();
	}

	public LoginResDto login(LoginReqDto loginReqDto) {
		Member member = memberRepository.findByEmail(loginReqDto.email())
			.orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

		if (!passwordEncoder.matches(loginReqDto.password(), member.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}

		String accessToken = jwtTokenProvider.createToken(member.getId().toString(), member.getRole().toString());
		String refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail(), member.getRole().toString());
		redisTemplate.opsForValue().set(member.getEmail(), refreshToken, 200, TimeUnit.DAYS);

		return LoginResDto.of(member.getId(), accessToken, refreshToken);
	}

	public String newAccessToken(MemberRefreshReqDto memberRefreshReqDto) {
		Claims claims = Jwts.parserBuilder()
			.setSigningKey(secretKeyRt)
			.build()
			.parseClaimsJws(memberRefreshReqDto.refreshToken())
			.getBody();

		Object rt = redisTemplate.opsForValue().get(claims.getSubject());
		if (rt == null || !rt.toString().equals(memberRefreshReqDto.refreshToken())) {
			throw new IllegalArgumentException("refreshToken 만료 또는 일치하지 않습니다.");
		}

		return jwtTokenProvider.createToken(claims.getSubject(), claims.get("role").toString());
	}
}
