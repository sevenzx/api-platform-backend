package com.xuan;

import com.xuan.client.InvokeInterfaceClient;
import com.xuan.client.UserClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author: xuan
 * @since: 2023/3/23
 */

@EnableFeignClients(clients = {UserClient.class, InvokeInterfaceClient.class})
@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication {
	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}
}
