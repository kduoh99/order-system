package com.study.ordersystem.product.application;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.ordersystem.member.domain.Member;
import com.study.ordersystem.member.domain.repository.MemberRepository;
import com.study.ordersystem.product.api.dto.request.ProductRegisterReqDto;
import com.study.ordersystem.product.domain.Product;
import com.study.ordersystem.product.domain.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

	private final ProductRepository productRepository;
	private final MemberRepository memberRepository;

	public Long productCreate(ProductRegisterReqDto productRegisterReqDto) {
		String id = SecurityContextHolder.getContext().getAuthentication().getName();
		Member member = memberRepository.findById(Long.parseLong(id))
			.orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

		Product product = productRepository.save(productRegisterReqDto.toEntity(member));
		return product.getId();
	}
}
