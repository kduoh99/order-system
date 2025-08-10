package com.study.ordersystem.ordering.application;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.study.ordersystem.ordering.api.dto.ProductDto;
import com.study.ordersystem.ordering.api.dto.ProductUpdateStockDto;
import com.study.ordersystem.ordering.api.dto.request.OrderCreateReqDto;
import com.study.ordersystem.ordering.domain.OrderStatus;
import com.study.ordersystem.ordering.domain.Ordering;
import com.study.ordersystem.ordering.domain.repository.OrderingRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderingService {

	private final OrderingRepository orderingRepository;
	private final RestTemplate restTemplate;
	private final ProductFeign productFeign;
	private final KafkaTemplate<String, Object> kafkaTemplate;

	public Long orderCreate(OrderCreateReqDto orderCreateReqDto, String userId) {

		// Product Get 요청
		String productGetUrl = "http://product-service/product/" + orderCreateReqDto.productId();

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("X-User-Id", userId);
		HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
		ResponseEntity<ProductDto> response = restTemplate.exchange(productGetUrl, HttpMethod.GET, httpEntity,
			ProductDto.class);
		ProductDto productDto = response.getBody();

		int quantity = orderCreateReqDto.productCount();
		if (productDto.stockQuantity() < quantity) {
			throw new IllegalArgumentException("재고가 부족합니다.");
		} else {
			// Product Put 요청
			String productPutUrl = "http://product-service/product/updatestock";

			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<ProductUpdateStockDto> updateEntity = new HttpEntity<>(
				ProductUpdateStockDto.builder()
					.productId(orderCreateReqDto.productId())
					.productQuantity(orderCreateReqDto.productCount())
					.build()
				, httpHeaders
			);
			restTemplate.exchange(productPutUrl, HttpMethod.PUT, updateEntity, Void.class);
		}

		Ordering ordering = orderingRepository.save(Ordering.builder()
			.memberId(Long.parseLong(userId))
			.productId(orderCreateReqDto.productId())
			.quantity(orderCreateReqDto.productCount())
			.orderStatus(OrderStatus.ORDERED)
			.build());

		return ordering.getId();
	}

	// CircuitBreaker는 해당 name을 가진 메서드에 한해서만 유효함
	// 즉, circuit이 open되어도 다른 메서드에서 product-service에 요청을 보내는 것은 허용함
	@CircuitBreaker(name = "productService", fallbackMethod = "fallbackProductService")
	public Long orderFeignKafkaCreate(OrderCreateReqDto orderCreateReqDto, String userId) {

		ProductDto productDto = productFeign.getProductById(orderCreateReqDto.productId(), userId);

		int quantity = orderCreateReqDto.productCount();
		if (productDto.stockQuantity() < quantity) {
			throw new IllegalArgumentException("재고가 부족합니다.");
		} else {
			// productFeign.updateProductStock(
			// 	ProductUpdateStockDto.builder()
			// 		.productId(orderCreateReqDto.productId())
			// 		.productQuantity(orderCreateReqDto.productCount())
			// 		.build()
			// );

			kafkaTemplate.send("update-stock-topic", ProductUpdateStockDto.builder()
				.productId(orderCreateReqDto.productId())
				.productQuantity(orderCreateReqDto.productCount())
				.build());
		}

		Ordering ordering = orderingRepository.save(Ordering.builder()
			.memberId(Long.parseLong(userId))
			.productId(orderCreateReqDto.productId())
			.quantity(orderCreateReqDto.productCount())
			.orderStatus(OrderStatus.ORDERED)
			.build());

		return ordering.getId();
	}

	public Long fallbackProductService(OrderCreateReqDto orderCreateReqDto, String userId, Throwable t) {
		throw new RuntimeException("상품 서비스가 응답이 없습니다. 나중에 다시 시도해주세요.");
	}
}
