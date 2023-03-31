package com.xuan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuan.model.entity.Day;
import com.xuan.model.vo.DayVO;

/**
 * @author xuan
 * @description 针对表【day(每日信息)】的数据库操作Service
 * @createDate 2023-03-30 12:17:03
 */
public interface DayService extends IService<Day> {

	/**
	 * 查询一天的信息
	 *
	 * @param date 日期
	 * @return DayVO
	 */
	DayVO selectOneDay(String date);

}
