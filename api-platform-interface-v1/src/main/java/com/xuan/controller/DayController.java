package com.xuan.controller;

import com.xuan.common.Result;
import com.xuan.model.vo.DayVO;
import com.xuan.service.DayService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: xuan
 * @since: 2023/3/30
 */

@RestController
@RequestMapping(value = "/day")
public class DayController {

	@Resource
	DayService dayService;

	@GetMapping(value = "/get")
	Result<DayVO> getOneDay(@RequestParam(value = "date") String date) {
		DayVO dayVO = dayService.getOneDay(date);
		return Result.success(dayVO);
	}

	@GetMapping(value = "/list")
	Result<List<DayVO>> listDayFromStartToEnd(@RequestParam(value = "startDate") String startDate,
	                                          @RequestParam(value = "endDate") String endDate) {
		List<DayVO> list = dayService.listDayFromStartToEnd(startDate, endDate);
		return Result.success(list);
	}

}
