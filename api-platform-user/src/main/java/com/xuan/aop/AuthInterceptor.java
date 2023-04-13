package com.xuan.aop;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.xuan.annotation.AuthCheck;
import com.xuan.common.ErrorCode;
import com.xuan.exception.BusinessException;
import com.xuan.model.vo.UserVO;
import com.xuan.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 权限校验 AOP
 *
 * @author xuan
 */
@Aspect
@Component
public class AuthInterceptor {

	@Resource
	private UserService userService;

	/**
	 * 执行拦截
	 *
	 * @param joinPoint ProceedingJoinPoint
	 * @param authCheck AuthCheck
	 * @return Object
	 */
	@Around("@annotation(authCheck)")
	public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
		List<String> anyRole = Arrays.stream(authCheck.anyRole()).filter(StrUtil::isNotBlank).collect(Collectors.toList());
		String mustRole = authCheck.mustRole();
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
		// 当前登录用户
		UserVO userVO = userService.currentUser(request);
		// 拥有任意权限即通过
		if (CollectionUtil.isNotEmpty(anyRole)) {
			String userRole = userVO.getUserRole();
			if (!anyRole.contains(userRole)) {
				throw new BusinessException(ErrorCode.NO_PERMISSION_ERROR);
			}
		}
		// 必须有所有权限才通过
		if (StrUtil.isNotBlank(mustRole)) {
			String userRole = userVO.getUserRole();
			if (!mustRole.equals(userRole)) {
				throw new BusinessException(ErrorCode.NO_PERMISSION_ERROR);
			}
		}
		// 通过权限校验，放行
		return joinPoint.proceed();
	}
}

