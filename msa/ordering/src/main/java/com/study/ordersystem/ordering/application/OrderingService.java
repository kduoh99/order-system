package com.study.ordersystem.ordering.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.ordersystem.ordering.api.dto.request.OrderCreateReqDto;
import com.study.ordersystem.ordering.domain.OrderStatus;
import com.study.ordersystem.ordering.domain.Ordering;
import com.study.ordersystem.ordering.domain.repository.OrderingRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderingService {

	private final OrderingRepository orderingRepository;

	public Long orderCreate(OrderCreateReqDto orderCreateReqDto, String userId) {

		Product product = productRepository.findById(orderCreateReqDto.productId())
			.orElseThrow(() -> new EntityNotFoundException("상품이 존재하지 않습니다."));

		int quantity = orderCreateReqDto.productCount();
		if (product.getStockQuantity() < quantity) {
			throw new IllegalArgumentException("재고가 부족합니다.");
		} else {
			product.updateStockQuantity(orderCreateReqDto.productCount());
		}

		Ordering ordering = orderingRepository.save(Ordering.builder()
			.memberId(Long.parseLong(userId))
			.productId(orderCreateReqDto.productId())
			.quantity(orderCreateReqDto.productCount())
			.orderStatus(OrderStatus.ORDERED)
			.build());

		return ordering.getId();
	}
}
