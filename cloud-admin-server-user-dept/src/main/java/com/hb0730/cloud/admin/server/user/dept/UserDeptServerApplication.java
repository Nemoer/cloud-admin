package com.hb0730.cloud.admin.server.user.dept;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * <p>
 * </P>
 *
 * @author bing_huang
 * @since V1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableMethodCache(basePackages = "com.hb0730.cloud.admin.server.user.dept")
@EnableCreateCacheAnnotation
public class UserDeptServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserDeptServerApplication.class, args);
    }
}
