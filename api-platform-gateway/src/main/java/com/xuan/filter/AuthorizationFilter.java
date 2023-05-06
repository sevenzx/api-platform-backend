package com.xuan.filter;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.xuan.client.InvokeInterfaceClient;
import com.xuan.client.UserClient;
import com.xuan.common.ErrorCode;
import com.xuan.common.Result;
import com.xuan.model.entity.InvokeInterface;
import com.xuan.model.vo.InvokeInterfaceUserVO;
import com.xuan.util.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * 调用接口鉴权过滤器
 *
 * @author: xuan
 * @since: 2023/3/30
 */

@Slf4j
@Component
@RefreshScope  // 动态刷新
public class AuthorizationFilter implements GlobalFilter, Ordered {

	@Resource
	private UserClient userClient;

	@Resource
	private InvokeInterfaceClient invokeInterfaceClient;

	private static final String V1_API_PATH = "/api/v1";

	// private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1", "127.0.0.2");

	// TODO 调试完后记得将时间改回来
	/**
	 * 限制时间
	 */
	private static final long LIMIT_TIME = 1000 * 60 * 1000L;

	/**
	 * 在线调用KEY
	 */
	@Value("${config.client.key}")
	private String clientKey;

	/**
	 * 在线调用SECRET
	 */
	@Value("${config.client.secret}")
	private String clientSecret;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		String path = request.getPath().toString();
		if (!path.contains(V1_API_PATH)) {
			// 不包含，放行
			return chain.filter(exchange);
		}

		// 1. 请求日志 - 由于日志过滤器的优先级高于鉴权过滤器，所以这里不再打印日志

		// 2. 访问控制 - 黑白名单
		ServerHttpResponse response = exchange.getResponse();
		// if (!IP_WHITE_LIST.contains(remoteAddress)) {
		// 	return handleNoPermission(response);
		// }

		// 3. 用户鉴权
		HttpHeaders headers = request.getHeaders();
		String userKey = headers.getFirst("userKey");
		String timestamp = headers.getFirst("timestamp");
		String sign = headers.getFirst("sign");

		if (StrUtil.hasBlank(userKey, timestamp, sign)) {
			return handleNoPermission(response);
		}

		// 4.1. 是否是在线调用
		if (StrUtil.equals(userKey, clientKey)) {
			String sign1 = SignUtil.getSign(timestamp, clientSecret);
			if (!StrUtil.equals(sign, sign1)) {
				return handleNoPermission(response);
			}
			return chain.filter(exchange);
		}

		// 4.2. 通过key查到secret再计算sign进行比对
		Result<InvokeInterfaceUserVO> result = userClient.getSecretByKey(userKey);

		InvokeInterfaceUserVO invokeInterfaceUserVO = result.getData();
		if (invokeInterfaceUserVO == null) {
			return handleNoPermission(response);
		}
		Long userId = invokeInterfaceUserVO.getId();
		String userSecret = invokeInterfaceUserVO.getUserSecret();
		String sign1 = SignUtil.getSign(timestamp, userSecret);

		if (!StrUtil.equals(sign, sign1)) {
			return handleNoPermission(response);
		}

		// 4.3. 规定时间内的请求有效
		if (!(NumberUtil.isNumber(timestamp) && System.currentTimeMillis() - Long.parseLong(timestamp) <= LIMIT_TIME)) {
			return handleNoPermission(response, "timestamp is invalid");
		}

		// 5. 获取调用信息
		Result<InvokeInterface> result1 = invokeInterfaceClient.selectByUserIdPathAndMethod(userId, path, request.getMethodValue());
		InvokeInterface invokeInterface = result1.getData();
		if (invokeInterface == null) {
			return handleNoPermission(response, "interface is not exist");
		}

		if (invokeInterface.getLeftNum() <= 0) {
			return handleNoPermission(response, "has no invoke num");
		}

		if (invokeInterface.getStatus() != 1) {
			return handleNoPermission(response, "interface is not enable");
		}

		// 6. 请求转发，调用模拟接口
		// TODO 确认调用接口成功后再计数
		invokeInterfaceClient.count(userId, invokeInterface.getInterfaceInfoId());
		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return 0;
	}


	/**
	 * 没有权限
	 *
	 * @param response response
	 * @return Mono<Void>
	 */
	private Mono<Void> handleNoPermission(ServerHttpResponse response) {
		response.setStatusCode(HttpStatus.FORBIDDEN);
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
		Result<Object> error = Result.error(ErrorCode.NO_PERMISSION_ERROR);
		String jsonStr = JSONUtil.toJsonStr(error);
		return response.writeWith(Mono.just(response.bufferFactory().wrap(jsonStr.getBytes())));
	}

	/**
	 * 没有权限
	 *
	 * @param response response
	 * @param message  message
	 * @return Mono<Void>
	 */
	private Mono<Void> handleNoPermission(ServerHttpResponse response, String message) {
		response.setStatusCode(HttpStatus.FORBIDDEN);
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
		Result<Object> error = Result.error(ErrorCode.NO_PERMISSION_ERROR.getCode(), message);
		String jsonStr = JSONUtil.toJsonStr(error);
		return response.writeWith(Mono.just(response.bufferFactory().wrap(jsonStr.getBytes())));
	}

}
