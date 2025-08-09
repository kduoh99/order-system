package com.study.ordersystem.ordering.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.ordersystem.ordering.api.dto.request.OrderCreateReqDto;
import com.study.ordersystem.ordering.application.OrderingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ordering")
public class OrderingController {

	private final OrderingService orderingService;

	@PostMapping("/create")
	public ResponseEntity<Long> orderCreate(@RequestBody OrderCreateReqDto orderCreateReqDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(orderingService.orderCreate(orderCreateReqDto));
	}
}
