package com.xuan;

import com.xuan.client.UserClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author: xuan
 * @since: 2023/3/28
 */

@EnableFeignClients(clients = {UserClient.class})
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.xuan.mapper")
public class InterfaceInfoApplication {
	public static void main(String[] args) {
		SpringApplication.run(InterfaceInfoApplication.class, args);
	}
}
