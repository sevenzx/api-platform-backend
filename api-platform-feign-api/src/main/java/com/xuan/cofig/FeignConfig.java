package com.xuan.cofig;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * OpenFeign配置
 *
 * @author: xuan
 * @since: 2023/4/13
 */

@Configuration
public class FeignConfig {

	@Bean
	public HttpMessageConverters httpMessageConverters() {
		return new HttpMessageConverters(new MappingJackson2HttpMessageConverter());
	}

}
