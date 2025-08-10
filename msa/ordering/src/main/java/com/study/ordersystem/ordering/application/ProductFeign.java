package com.study.ordersystem.ordering.application;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.study.ordersystem.ordering.api.dto.ProductDto;
import com.study.ordersystem.ordering.api.dto.ProductUpdateStockDto;

// name은 eureka에 등록된 호출할 서비스명
@FeignClient(name = "product-service")
public interface ProductFeign {

	@GetMapping("/product/{productId}")
	ProductDto getProductById(@PathVariable Long productId, @RequestHeader("X-User-Id") String userId);

	@PutMapping("/product/updatestock")
	void updateProductStock(@RequestBody ProductUpdateStockDto productUpdateStockDto);
}
