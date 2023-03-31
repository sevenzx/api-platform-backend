package com.xuan.util;

import org.springframework.util.DigestUtils;

/**
 * @author: xuan
 * @since: 2023/3/30
 */
public class SignUtil {

	/**
	 * 混淆
	 */
	private static final String SALT = "xuan";

	public static String getSign(String timestamp, String secretKey) {
		String content = SALT + "." + timestamp + "." + secretKey;
		return DigestUtils.md5DigestAsHex(content.getBytes());
	}

}
