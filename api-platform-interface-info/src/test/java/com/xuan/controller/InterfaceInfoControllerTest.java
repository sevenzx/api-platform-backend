package com.xuan.controller;

import com.xuan.mapper.InvokeInterfaceMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


/**
 * @author: xuan
 * @since: 2023/3/28
 */

@SpringBootTest
class InterfaceInfoControllerTest {

	@Resource
	private InvokeInterfaceMapper invokeInterfaceMapper;

	@Test
	void test() {
		System.out.println(invokeInterfaceMapper.selectByUserIdPathAndMethod(1L, "/api/v1/day/get", "GET"));
	}

}