package com.hb0730.cloud.admin.gateway.service.impl;

import com.google.common.collect.Lists;
import com.hb0730.cloud.admin.api.feign.router.handler.RemoteRouterHandler;
import com.hb0730.cloud.admin.api.feign.router.model.vo.GatewayFilterDefinition;
import com.hb0730.cloud.admin.api.feign.router.model.vo.GatewayPredicateDefinition;
import com.hb0730.cloud.admin.api.feign.router.model.vo.GatewayRouteDefinition;
import com.hb0730.cloud.admin.common.util.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 动态路由
 * </P>
 *
 * @author bing_huang
 * @since V1.0
 */
@Component
public class DynamicRouteServiceImpl implements RouteDefinitionRepository, ApplicationEventPublisherAware {
    private static Logger logger = LoggerFactory.getLogger(DynamicRouteServiceImpl.class);
    private ApplicationEventPublisher publisher;
    private List<RouteDefinition> routeDefinitionList = new ArrayList<>();
    @Autowired
    private RemoteRouterHandler routerHandler;

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        logger.debug("获取全部的路由");
        return Flux.fromIterable(routeDefinitionList);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        logger.debug("保存路由");
        return route.flatMap((r) -> {
            GatewayRouteDefinition router = BeanUtils.transformFrom(r, GatewayRouteDefinition.class);
            assert router != null;
            router.setUri(r.getUri().toString());
            routerHandler.save(router);
            load();
            return Mono.empty();
        });

    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        logger.debug("删除路由");
        return routeId.flatMap(id -> {
            routerHandler.delete(id);
            load();
            return Mono.empty();
        });
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @PostConstruct
    public void init() {
        load();
    }

    /**
     * <p>
     * 更新路由
     * </p>
     *
     * @param routeDefinition 路由
     */
    public void update(GatewayRouteDefinition routeDefinition) {
        if (Objects.isNull(routeDefinition)) {
            throw new NullPointerException("参数为空");
        }
        if (Objects.isNull(routeDefinition.getId())) {
            throw new NullPointerException("id为空");
        }
        routerHandler.update(routeDefinition);
        load();
    }

    /**
     * <p>
     * 获取info
     * </p>
     *
     * @param id id
     * @return info
     */
    public GatewayRouteDefinition getInfo(String id) {
        if (CollectionUtils.isEmpty(routeDefinitionList)) {
            return null;
        }
        List<RouteDefinition> collect = routeDefinitionList.stream().filter(d -> d.getId().equals(id)).collect(Collectors.toList());
        List<GatewayRouteDefinition> definitions = Lists.newArrayList();
        convertToGateway(collect, definitions);
        return definitions.get(0);
    }

    /**
     * 初始化
     */
    public void load() {
        List<GatewayRouteDefinition> routers = routerHandler.getRouters();
        routeDefinitionList.clear();
        routers.forEach(definition -> convertToRouter(definition, routeDefinitionList));
    }

    /**
     * <p>
     * 获取gatewayrouter
     * </p>
     *
     * @return gateway router
     */
    public List<GatewayRouteDefinition> getRouters() {
        List<GatewayRouteDefinition> gatewayRouteDefinitions = Lists.newArrayList();
        convertToGateway(routeDefinitionList, gatewayRouteDefinitions);
        return gatewayRouteDefinitions;
    }

    /**
     * <p>
     * 将Gateway转从router
     * </p>
     *
     * @param definition          参数类型
     * @param routeDefinitionList 转换类型
     */
    private void convertToRouter(GatewayRouteDefinition definition, List<RouteDefinition> routeDefinitionList) {
        if (Objects.isNull(definition)) {
            return;
        }
        RouteDefinition routeDefinition = BeanUtils.transformFrom(definition, RouteDefinition.class);
        assert routeDefinition != null;
        List<FilterDefinition> filterDefinitions = BeanUtils.transformFromInBatch(definition.getFilters(), FilterDefinition.class);
        routeDefinition.setFilters(filterDefinitions);
        List<PredicateDefinition> predicateDefinitions = BeanUtils.transformFromInBatch(definition.getPredicates(), PredicateDefinition.class);
        routeDefinition.setPredicates(predicateDefinitions);
        routeDefinition.setUri(URI.create(definition.getUri()));
        routeDefinitionList.add(routeDefinition);
    }

    /**
     * <p>
     * 将router转换成Gateway
     * </p>
     *
     * @param routeDefinitionList 参数类型
     * @param definitions         转换类型
     */
    private void convertToGateway(List<RouteDefinition> routeDefinitionList, List<GatewayRouteDefinition> definitions) {
        if (CollectionUtils.isEmpty(routeDefinitionList)) {
            return;
        }
        routeDefinitionList.forEach(routeDefinition -> {
            GatewayRouteDefinition gatewayRouteDefinition = BeanUtils.transformFrom(routeDefinition, GatewayRouteDefinition.class);
            assert gatewayRouteDefinition != null;
            gatewayRouteDefinition.setUri(routeDefinition.getUri().toString());
            List<FilterDefinition> filters = routeDefinition.getFilters();
            List<GatewayFilterDefinition> gatewayFilterDefinitions = BeanUtils.transformFromInBatch(filters, GatewayFilterDefinition.class);
            gatewayRouteDefinition.setFilters(gatewayFilterDefinitions);
            List<PredicateDefinition> predicates = routeDefinition.getPredicates();
            List<GatewayPredicateDefinition> gatewayPredicateDefinitions = BeanUtils.transformFromInBatch(predicates, GatewayPredicateDefinition.class);
            gatewayRouteDefinition.setPredicates(gatewayPredicateDefinitions);
            definitions.add(gatewayRouteDefinition);
        });
    }
}
