package com.xuan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * 跨域配置
 *
 * @author: xuan
 * @since: 2023/3/23
 */

@Configuration
public class CorsConfig {

	@Bean
	public CorsWebFilter corsWebFilter() {
		// 1.添加CORS配置信息
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		// 配置跨域
		corsConfiguration.addAllowedHeader("*");
		corsConfiguration.addAllowedMethod("*");
		corsConfiguration.addAllowedOriginPattern("*");
		corsConfiguration.setAllowCredentials(true);
		// 2.添加映射路径，拦截一切请求
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration);
		// 3.返回新的CorsFilter
		return new CorsWebFilter(source);
	}

}
