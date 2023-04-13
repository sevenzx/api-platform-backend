package com.xuan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuan.model.entity.Day;
import com.xuan.model.vo.DayVO;

import java.util.List;

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
	DayVO getOneDay(String date);

	/**
	 * 查询一段日期的信息
	 *
	 * @param startDate 开始日期
	 * @param endDate   结束日期
	 * @return List<DayVO>
	 */
	List<DayVO> listDayFromStartToEnd(String startDate, String endDate);
}
