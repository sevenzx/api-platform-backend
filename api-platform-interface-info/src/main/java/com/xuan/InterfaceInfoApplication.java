package com.xuan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author: xuan
 * @since: 2023/3/28
 */

@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.xuan.mapper")
public class InterfaceInfoApplication {
	public static void main(String[] args) {
		SpringApplication.run(InterfaceInfoApplication.class, args);
	}
}
