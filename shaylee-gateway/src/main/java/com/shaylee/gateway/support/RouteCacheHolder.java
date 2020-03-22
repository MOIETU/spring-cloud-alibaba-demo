package com.shaylee.gateway.support;

import com.shaylee.common.redis.service.CacheService;
import com.shaylee.common.utils.SpringContextHolder;
import com.shaylee.gateway.constant.CacheConstants;
import com.shaylee.gateway.vo.RouteDefinitionVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	public static List<RouteDefinitionVO> getRouteList() {
		List<RouteDefinitionVO> routeList = new ArrayList<>();
		Map<String, Object> cacheMap = cacheService.hGetAll(CacheConstants.ROUTE_HASH_KEY);
		cacheMap.forEach((key, value) -> routeList.add((RouteDefinitionVO) value));
		return routeList;
	}

	/**
	 * 更新缓存
	 *
	 * @param routeList 缓存列表
	 */
	public static void setRouteList(List<RouteDefinitionVO> routeList) {
		routeList.forEach(route -> cacheService.hSet(CacheConstants.ROUTE_HASH_KEY, route.getId(), route));
	}

	/**
	 * 清空缓存
	 */
	public static void removeRouteList() {
		cacheService.delete(CacheConstants.ROUTE_HASH_KEY);
	}

}
