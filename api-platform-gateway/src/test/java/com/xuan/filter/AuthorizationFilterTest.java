package com.xuan.filter;

import com.xuan.util.SignUtil;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author: xuan
 * @since: 2023/4/13
 */

@SpringBootTest
public class AuthorizationFilterTest {

	@Test
	public void testSign() {
		long timestamp = System.currentTimeMillis();
		System.out.println(timestamp);
		String secret = "2c094e789d4fc41f808519ff2f2bd201";
		String sign = SignUtil.getSign(timestamp + "", secret);
		System.out.println(sign);
	}

}