package com.xuan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: xuan
 * @since: 2023/3/29
 */

@SpringBootApplication
@MapperScan("com.xuan.mapper")
public class InterfaceV1Application {
	public static void main(String[] args) {
		SpringApplication.run(InterfaceV1Application.class, args);
	}
}
