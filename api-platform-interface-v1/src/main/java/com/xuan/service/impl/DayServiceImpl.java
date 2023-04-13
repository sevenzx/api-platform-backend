package com.xuan.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuan.common.ErrorCode;
import com.xuan.exception.BusinessException;
import com.xuan.mapper.DayMapper;
import com.xuan.model.entity.Day;
import com.xuan.model.vo.DayVO;
import com.xuan.service.DayService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.xuan.constant.RedisConstant.DATE_PREFIX;

/**
 * @author xuan
 * @description 针对表【day(每日信息)】的数据库操作Service实现
 * @createDate 2023-03-30 12:17:03
 */
@Service
public class DayServiceImpl extends ServiceImpl<DayMapper, Day>
		implements DayService {

	private static final String DATE_FORMAT = "yyyyMMdd";

	private static final int MIN_DATE = 20230101;

	private static final int MAX_DATE = 20231231;

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	@Override
	public DayVO getOneDay(String date) {
		String dateNum;
		// 如果date为空就返回当天的信息
		if (StrUtil.isBlank(date)) {
			DateTime dateTime = DateUtil.date();
			dateNum = DateUtil.format(dateTime, DATE_FORMAT);
		} else {
			dateNum = date.replace("-", "");
			if (!NumberUtil.isNumber(dateNum)) {
				throw new BusinessException(ErrorCode.PARAMS_ERROR);
			}
		}

		try {
			DateUtil.parse(dateNum);
		} catch (Exception e) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, e.getMessage());
		}

		if (Integer.parseInt(dateNum) < MIN_DATE || Integer.parseInt(dateNum) > MAX_DATE) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "date range is " + MIN_DATE + " to " + MAX_DATE);
		}

		// 查询redis缓存
		String value = stringRedisTemplate.opsForValue().get(DATE_PREFIX + dateNum);
		if (StrUtil.isBlank(value)) {
			throw new BusinessException(ErrorCode.SYSTEM_ERROR);
		}
		return JSONUtil.toBean(value, DayVO.class);
	}

	@Override
	public List<DayVO> listDayFromStartToEnd(String startDate, String endDate) {
		String startDateNum = startDate.replace("-", "");
		String endDateNum = endDate.replace("-", "");

		if (!NumberUtil.isNumber(startDateNum) || !NumberUtil.isNumber(endDateNum)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}

		Date localStartDate;
		Date localEndDate;
		try {
			localStartDate= DateUtil.parse(startDateNum, DATE_FORMAT);
			localEndDate = DateUtil.parse(endDateNum, DATE_FORMAT);
		} catch (Exception e) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, e.getMessage());
		}

		if (Integer.parseInt(startDateNum) > Integer.parseInt(endDateNum)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}

		if (Integer.parseInt(startDateNum) < MIN_DATE || Integer.parseInt(endDateNum) > MAX_DATE) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "date range is " + MIN_DATE + " to " + MAX_DATE);
		}

		List<String> datesInRange = DateUtil.rangeToList(localStartDate, localEndDate, DateField.DAY_OF_YEAR,1)
				.stream()
				.map(date -> DATE_PREFIX + DateUtil.format(date, DATE_FORMAT))
				.collect(Collectors.toList());

		List<String> strList = stringRedisTemplate.opsForValue().multiGet(datesInRange);
		if (strList == null || strList.isEmpty()) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		return strList
				.stream()
				.map(str -> JSONUtil.toBean(str, DayVO.class))
				.collect(Collectors.toList());
	}
}




