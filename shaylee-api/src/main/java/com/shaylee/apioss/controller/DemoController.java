package com.shaylee.apioss.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.shaylee.common.gateway.support.RouteCacheHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Title: demo控制器
 * Project: shaylee-cloud
 *
 * @author Adrian
 * @date 2020-03-20
 */
@RestController
@RequestMapping("/demo")
public class DemoController {
    private static Logger logger = LoggerFactory.getLogger(DemoController.class);

    @Value("${server.port}")
    private Integer port;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @GetMapping("/whoami")
    @SentinelResource("/whoami")
    public Object whoami() {
        return "shaylee-api:" + port;
    }

    @GetMapping("/addRoutes")
    public Object addRoutes() {
        try {
            List<RouteDefinition> routeList = this.getRouteDefinitionList();
            RouteCacheHolder.setRouteList(routeList);
            this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
        } catch (Exception e) {
            logger.error("路由配置解析失败", e);
            // 回滚路由，重新加载即可
            //this.applicationEventPublisher.publishEvent(new DynamicRouteInitEvent(this));
            // 抛出异常
            throw new RuntimeException(e);
        }
        return "shaylee-routes:" + port;
    }

    @GetMapping("/routes")
    public Object routes() {
        return RouteCacheHolder.getRouteList() + "shaylee-routes:" + port;
    }

    private List<RouteDefinition> getRouteDefinitionList() {
        List<RouteDefinition> routeDefinitions = new LinkedList<>();
        routeDefinitions.add(
                buildRouteDefinition("shaylee-api-oss", "lb://shaylee-api-oss", "/api/oss/**"));
        routeDefinitions.add(
                buildRouteDefinition("shaylee-api", "lb://shaylee-api", "/api/**"));
        return routeDefinitions;
    }

    private RouteDefinition buildRouteDefinition(String id, String uri, String path) {
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setId(id);
        routeDefinition.setUri(URI.create(uri));
        List<PredicateDefinition> predicateDefinitionList = new ArrayList<>();
        PredicateDefinition predicateDefinition = new PredicateDefinition();
        predicateDefinition.setName("Path");
        Map<String, String> args = new HashMap<>(1);
        args.put("_genkey_0", path);
        predicateDefinition.setArgs(args);
        predicateDefinitionList.add(predicateDefinition);
        routeDefinition.setPredicates(predicateDefinitionList);
        return routeDefinition;
    }
}
