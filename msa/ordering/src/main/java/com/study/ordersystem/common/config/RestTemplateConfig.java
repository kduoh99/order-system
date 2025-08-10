package com.study.ordersystem.common.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

	@Bean
	@LoadBalanced // Eureka에 등록된 서비스명을 통해 내부 서비스 호출 (내부통신)
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
