package com.xuan.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.xuan.model.entity.Day;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author: xuan
 * @since: 2023/3/30
 */

@SpringBootTest
class DayServiceTest {

	@Resource
	private DayService dayService;

	@Test
	void testData() {
		String filePath = "/Users/xuan/Desktop/2023.xlsx";
		ExcelReader reader = ExcelUtil.getReader(FileUtil.file(filePath), 0);
		List<Day> dayInfos = reader.readAll(Day.class);
		for (Day dayInfo : dayInfos) {
			String s = dayInfo.getDateStr().split(" ")[0];
			dayInfo.setDateStr(s);
			// System.out.println(dayInfo);
			dayService.save(dayInfo);
		}
	}

}