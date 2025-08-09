package com.study.ordersystem.ordering.application;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.ordersystem.member.domain.Member;
import com.study.ordersystem.member.domain.repository.MemberRepository;
import com.study.ordersystem.ordering.api.dto.request.OrderCreateReqDto;
import com.study.ordersystem.ordering.domain.OrderStatus;
import com.study.ordersystem.ordering.domain.Ordering;
import com.study.ordersystem.ordering.domain.repository.OrderingRepository;
import com.study.ordersystem.product.domain.Product;
import com.study.ordersystem.product.domain.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderingService {

	private final OrderingRepository orderingRepository;
	private final MemberRepository memberRepository;
	private final ProductRepository productRepository;

	public Long orderCreate(OrderCreateReqDto orderCreateReqDto) {
		String id = SecurityContextHolder.getContext().getAuthentication().getName();
		Member member = memberRepository.findById(Long.parseLong(id))
			.orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

		Product product = productRepository.findById(orderCreateReqDto.productId())
			.orElseThrow(() -> new EntityNotFoundException("상품이 존재하지 않습니다."));

		int quantity = orderCreateReqDto.productCount();
		if (product.getStockQuantity() < quantity) {
			throw new IllegalArgumentException("재고가 부족합니다.");
		} else {
			product.updateStockQuantity(orderCreateReqDto.productCount());
		}

		Ordering ordering = orderingRepository.save(Ordering.builder()
			.member(member)
			.product(product)
			.quantity(orderCreateReqDto.productCount())
			.orderStatus(OrderStatus.ORDERED)
			.build());

		return ordering.getId();
	}
}
