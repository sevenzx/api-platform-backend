package com.xuan.controller;

import com.xuan.client.UserClient;
import com.xuan.common.Result;
import com.xuan.model.vo.UserVO;
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
	private UserClient userClient;

	@Test
	void testClient(){
		Result<UserVO> result = userClient.getUserById(1L);
		System.out.println("result = " + result);
	}

}