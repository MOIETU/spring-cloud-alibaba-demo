package com.shaylee.common.gateway.support;

import com.shaylee.common.gateway.constant.CacheConstants;
import com.shaylee.common.redis.service.CacheService;
import com.shaylee.common.utils.SpringContextHolder;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.util.LinkedList;
import java.util.List;

/**
 * Title: 路由缓存工具类
 * Project: shaylee-gateway
 *
 * @author Adrian
 * @date 2020-03-22
 */
public class RouteCacheHolder {

	private static CacheService cacheService;

	static {
		cacheService = SpringContextHolder.getBean(CacheService.class);
	}

	/**
	 * 获取缓存的全部对象
	 *
	 * @return routeList
	 */
	public static List<RouteDefinition> getRouteList() {
		List<RouteDefinition> routeList = (List<RouteDefinition>) cacheService.get(CacheConstants.ROUTE_LIST_KEY);
		return routeList;
	}

	/**
	 * 更新缓存
	 *
	 * @param routeList 缓存列表
	 */
	public static void setRouteList(List<RouteDefinition> routeList) {
		cacheService.set(CacheConstants.ROUTE_LIST_KEY, routeList);
	}

	/**
	 * 清空缓存
	 */
	public static void removeRouteList() {
		cacheService.delete(CacheConstants.ROUTE_LIST_KEY);
	}

}
