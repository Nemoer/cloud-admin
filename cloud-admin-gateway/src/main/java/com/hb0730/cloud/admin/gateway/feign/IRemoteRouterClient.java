package com.hb0730.cloud.admin.gateway.feign;

import com.hb0730.cloud.admin.common.util.ServerNameConstants;
import com.hb0730.cloud.admin.common.web.response.ResultJson;
import com.hb0730.cloud.admin.gateway.config.FeignConfiguration;
import com.hb0730.cloud.admin.gateway.model.GatewayRouteDefinition;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * 远程路由server
 * //, configuration = FeignConfiguration.class, fallbackFactory = RemoteRouterClientFactory.class
 * </P>
 *
 * @author bing_huang
 * @since V1.0
 */
@FeignClient(name = ServerNameConstants.ROUTER_SERVER, path = "/admin/system/router", configuration = FeignConfiguration.class)
public interface IRemoteRouterClient {
    /**
     * <p>
     * 获取路由
     * </p>
     *
     * @return 路由集
     */
    @GetMapping("/routers")
    ResultJson getRouters();

}
