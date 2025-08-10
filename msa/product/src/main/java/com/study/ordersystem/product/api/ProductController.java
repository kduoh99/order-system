package com.study.ordersystem.product.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.ordersystem.product.api.dto.request.ProductRegisterReqDto;
import com.study.ordersystem.product.api.dto.request.ProductUpdateStockReqDto;
import com.study.ordersystem.product.api.dto.response.ProductResDto;
import com.study.ordersystem.product.application.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

	private final ProductService productService;

	@PostMapping("/create")
	public ResponseEntity<Long> productCreate(ProductRegisterReqDto productRegisterReqDto, @RequestHeader("X-User-Id") String userId) {
		return ResponseEntity.status(HttpStatus.CREATED).body(productService.productCreate(productRegisterReqDto, userId));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductResDto> productDetail(@PathVariable Long id, @RequestHeader("X-User-Id") String userId) {
		return ResponseEntity.ok(productService.productDetail(id));
	}

	@PutMapping("/updatestock")
	public ResponseEntity<Long> updateStock(@RequestBody ProductUpdateStockReqDto productUpdateStockReqDto) {
		return ResponseEntity.ok(productService.updateStockQuantity(productUpdateStockReqDto));
	}
}
