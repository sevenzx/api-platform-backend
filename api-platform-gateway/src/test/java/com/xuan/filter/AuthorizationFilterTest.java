package com.xuan.filter;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
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
		String sign = SignUtil.getSign(String.valueOf(timestamp), secret);
		System.out.println(sign);
	}

	@Test
	public void keySecretTest() {
		String SALT = "xuan";
		String userAccount = "client";
		String userKey = "cli_" + DigestUtil.md5Hex(SALT + userAccount + RandomUtil.randomNumbers(4));
		String userSecret = DigestUtil.md5Hex(SALT + userAccount + RandomUtil.randomNumbers(8));
		System.out.println("userKey = " + userKey);
		System.out.println("userSecret = " + userSecret);
	}

}