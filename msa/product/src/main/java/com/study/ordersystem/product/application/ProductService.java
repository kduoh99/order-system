package com.study.ordersystem.product.application;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.ordersystem.product.api.dto.request.ProductRegisterReqDto;
import com.study.ordersystem.product.api.dto.request.ProductUpdateStockReqDto;
import com.study.ordersystem.product.api.dto.response.ProductResDto;
import com.study.ordersystem.product.domain.Product;
import com.study.ordersystem.product.domain.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

	private final ProductRepository productRepository;

	public Long productCreate(ProductRegisterReqDto productRegisterReqDto, String userId) {

		Product product = productRepository.save(productRegisterReqDto.toEntity(Long.parseLong(userId)));
		return product.getId();
	}

	public ProductResDto productDetail(Long id) {
		Product product = productRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 상품입니다."));

		return ProductResDto.of(product.getId(), product.getName(), product.getPrice(), product.getStockQuantity());
	}

	public Long updateStockQuantity(ProductUpdateStockReqDto productUpdateStockReqDto) {
		Product product = productRepository.findById(productUpdateStockReqDto.productId())
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 상품입니다."));

		product.updateStockQuantity(productUpdateStockReqDto.productQuantity());
		return product.getId();
	}

	@KafkaListener(topics = "update-stock-topic", containerFactory = "kafkaListener")
	public void stockConsumer(String message) {
		ObjectMapper objectMapper = new ObjectMapper();
		ProductUpdateStockReqDto dto = null;
		try {
			dto = objectMapper.readValue(message, ProductUpdateStockReqDto.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
		this.updateStockQuantity(dto);
	}
}
