package com.xuan.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.xuan.mapper.DayMapper;
import com.xuan.model.entity.Day;
import com.xuan.model.vo.DayVO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.List;

import static com.xuan.constant.RedisConstant.DATE_PREFIX;


/**
 * @author: xuan
 * @since: 2023/3/30
 */

@SpringBootTest
class DayServiceTest {

	@Resource
	private DayService dayService;

	@Resource
	private DayMapper dayMapper;

	@Resource
	private StringRedisTemplate stringRedisTemplate;

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

	@Test
	void insertRedis() {

		List<Day> days = dayMapper.selectList(null);
		for (Day day : days) {
			DayVO dayVO = BeanUtil.copyProperties(day, DayVO.class);
			// 写入redis
			stringRedisTemplate.opsForValue().set(DATE_PREFIX + dayVO.getDate(), JSONUtil.toJsonStr(dayVO));
		}
	}

	@Test
	void insertStr() {
		String s = RandomUtil.randomString(16);
		System.out.println(s);
		
	}

}