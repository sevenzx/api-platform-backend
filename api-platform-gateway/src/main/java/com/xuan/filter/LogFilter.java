package com.xuan.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * 日志过滤器
 *
 * @author: xuan
 * @since: 2023/4/25
 */

@Slf4j
@Component
public class LogFilter implements GlobalFilter, Ordered {
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		String path = request.getPath().toString();

		// 请求日志
		String method = Objects.requireNonNull(request.getMethod()).toString();
		log.info("请求id: {}", request.getId());
		String remoteAddress = Objects.requireNonNull(request.getRemoteAddress()).getHostString();
		log.info("请求地址: {}", remoteAddress);
		log.info("请求路径: {}", path);
		log.info("请求方法: {}", method);
		log.info("请求参数: {}", request.getQueryParams());
		log.info("请求头: {}", request.getHeaders());

		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return -1;
	}
}
