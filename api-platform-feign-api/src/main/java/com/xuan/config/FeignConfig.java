package com.xuan.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * feign配置
 *
 * @author: xuan
 * @since: 2023/3/28
 */

@Configuration
public class FeignConfig implements RequestInterceptor {

	/**
	 * 复写feign请求对象
	 *
	 * @param requestTemplate RequestTemplate
	 */
	@Override
	public void apply(RequestTemplate requestTemplate) {
		// 获取请求头
		Map<String, String> headers = getHeaders(Objects.requireNonNull(getHttpServletRequest()));
		for (String headerName : headers.keySet()) {
			requestTemplate.header(headerName, headers.get(headerName));
		}
	}

	// 获取请求对象
	private HttpServletRequest getHttpServletRequest() {
		try {
			return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 拿到请求头信息
	private Map<String, String> getHeaders(HttpServletRequest request) {
		Map<String, String> map = new LinkedHashMap<>();
		Enumeration<String> enumeration = request.getHeaderNames();
		while (enumeration.hasMoreElements()) {
			String key = enumeration.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}
		return map;
	}
}
