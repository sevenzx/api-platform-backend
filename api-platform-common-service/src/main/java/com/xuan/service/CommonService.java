package com.xuan.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xuan.common.ErrorCode;
import com.xuan.common.FuzzyQueryRequest;
import com.xuan.common.PageRequest;
import com.xuan.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.xuan.constant.CommonConstant.MAX_PAGE_SIZE;
import static com.xuan.constant.CommonConstant.UPDATE_TIME;

/**
 * service层的公共实现
 *
 * @author: xuan
 * @since: 2023/4/29
 */

@Slf4j
public class CommonService {

	/**
	 * 分页查询
	 *
	 * @param service     MBP IService
	 * @param pageRequest 分页参数
	 * @param <T>         实体类
	 * @return 分页结果
	 */
	public static <T> Page<T> listByPage(IService<T> service, PageRequest pageRequest) {

		if (pageRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}

		// 拿到分页参数
		long current = pageRequest.getCurrent();
		long pageSize = pageRequest.getPageSize();
		boolean needTotal = pageRequest.isNeedTotal();

		// 限制爬虫
		if (pageSize > MAX_PAGE_SIZE) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "pageSize不得超过" + MAX_PAGE_SIZE);
		}
		// 更新时间倒序排序
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		queryWrapper.orderByDesc(UPDATE_TIME);

		// 分页查询
		Page<T> userPage = new Page<>(current, pageSize);
		// 设置是否返回总量total
		userPage.setSearchCount(needTotal);
		try {
			userPage = service.page(userPage, queryWrapper);
		} catch (Exception e) {
			log.error("分页查询用户列表失败", e);
			throw new BusinessException(ErrorCode.SYSTEM_ERROR);
		}

		return userPage;
	}

	/**
	 * 分页模糊查询
	 *
	 * @param service           MBP IService
	 * @param fuzzyQueryRequest 模糊查询参数
	 * @param <T>               实体类
	 * @return 分页结果
	 */
	public static <T> Page<T> listByFuzzy(IService<T> service, FuzzyQueryRequest fuzzyQueryRequest) {
		List<String> fields = fuzzyQueryRequest.getFields();
		String keyword = fuzzyQueryRequest.getKeyword();
		if (CollectionUtil.isEmpty(fields) || StrUtil.isBlank(keyword)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		// 拿到分页参数
		long current = fuzzyQueryRequest.getCurrent();
		long pageSize = fuzzyQueryRequest.getPageSize();
		boolean needTotal = fuzzyQueryRequest.isNeedTotal();
		// 限制爬虫
		if (pageSize > MAX_PAGE_SIZE) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "pageSize不得超过" + MAX_PAGE_SIZE);
		}
		Page<T> queryPage = new Page<>(current, pageSize);
		// 设置是否返回总量total (默认为true,设置为false可提升性能)
		queryPage.setSearchCount(needTotal);
		// 构建查询条件
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		for (String field : fields) {
			// 驼峰命名转下划线
			field = StrUtil.toUnderlineCase(field);
			queryWrapper.like(StrUtil.isNotBlank(field), field, keyword).or();
		}
		// 默认按更新时间排序
		queryWrapper.orderByDesc(UPDATE_TIME);
		// 执行查询
		try {
			queryPage = service.page(queryPage, queryWrapper);
		} catch (Exception e) {
			log.error("模糊查询用户列表失败", e);
			log.error("模糊查询中 fields: {} ; keyword: {}", fields, keyword);
			throw new BusinessException(ErrorCode.SYSTEM_ERROR);
		}

		return queryPage;

	}
}
