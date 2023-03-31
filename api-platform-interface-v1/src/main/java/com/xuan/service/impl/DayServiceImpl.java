package com.xuan.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuan.common.ErrorCode;
import com.xuan.common.Result;
import com.xuan.exception.BusinessException;
import com.xuan.mapper.DayMapper;
import com.xuan.model.entity.Day;
import com.xuan.model.vo.DayVO;
import com.xuan.service.DayService;
import org.springframework.stereotype.Service;

import static com.xuan.constant.CommonConstant.MAX_PAGE_SIZE;

/**
 * @author xuan
 * @description 针对表【day(每日信息)】的数据库操作Service实现
 * @createDate 2023-03-30 12:17:03
 */
@Service
public class DayServiceImpl extends ServiceImpl<DayMapper, Day>
		implements DayService {

	@Override
	public DayVO selectOneDay(String date) {
		Integer dateNum;
		// 如果date为空就返回当天的信息
		if (StrUtil.isBlank(date)) {
			DateTime dateTime = DateUtil.date();
			String format = DateUtil.format(dateTime, "yyyyMMdd");
			dateNum = new Integer(format);
		} else {
			String newDate = date.replace("-", "");
			if (!NumberUtil.isNumber(newDate)) {
				throw new BusinessException(ErrorCode.PARAMS_ERROR);
			}
			dateNum = new Integer(newDate);
		}
		LambdaQueryWrapper<Day> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Day::getDate, dateNum);
		Day day = this.getOne(queryWrapper);
		return BeanUtil.copyProperties(day, DayVO.class);
	}
}




