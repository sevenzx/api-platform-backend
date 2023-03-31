package com.xuan.controller;

import com.xuan.common.Result;
import com.xuan.model.vo.DayVO;
import com.xuan.service.DayService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: xuan
 * @since: 2023/3/30
 */

@RestController
@RequestMapping("/day")
public class DayController {

	@Resource
	DayService dayService;

	@GetMapping()
	Result<DayVO> selectOneDay(@RequestParam(value = "date") String date) {
		DayVO dayVO = dayService.selectOneDay(date);
		return Result.success(dayVO);
	}

}
