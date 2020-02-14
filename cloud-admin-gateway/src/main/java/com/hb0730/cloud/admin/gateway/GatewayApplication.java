package com.hb0730.cloud.admin.gateway;

import com.hb0730.cloud.admin.gateway.feign.IRemoteRouterClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * <p>
 * 服务网关
 * </P>
 *
 * @author bing_huang
 * @since V1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients()
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
