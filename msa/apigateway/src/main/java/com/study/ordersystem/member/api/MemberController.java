package com.study.ordersystem.member.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.ordersystem.member.api.dto.request.LoginReqDto;
import com.study.ordersystem.member.api.dto.request.MemberRefreshReqDto;
import com.study.ordersystem.member.api.dto.request.MemberSaveReqDto;
import com.study.ordersystem.member.api.dto.response.LoginResDto;
import com.study.ordersystem.member.application.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/create")
	public ResponseEntity<Long> memberCreate(@RequestBody MemberSaveReqDto memberSaveReqDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(memberService.save(memberSaveReqDto));
	}

	@PostMapping("/doLogin")
	public ResponseEntity<LoginResDto> doLogin(@RequestBody LoginReqDto loginReqDto) {
		return ResponseEntity.ok(memberService.login(loginReqDto));
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<String> generateNewAt(@RequestBody MemberRefreshReqDto memberRefreshReqDto) {
		return ResponseEntity.ok(memberService.newAccessToken(memberRefreshReqDto));
	}
}
