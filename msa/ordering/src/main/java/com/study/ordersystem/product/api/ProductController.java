package com.study.ordersystem.product.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.ordersystem.product.api.dto.request.ProductRegisterReqDto;
import com.study.ordersystem.product.application.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

	private final ProductService productService;

	@PostMapping("/create")
	public ResponseEntity<Long> productCreate(ProductRegisterReqDto productRegisterReqDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(productService.productCreate(productRegisterReqDto));
	}
}
