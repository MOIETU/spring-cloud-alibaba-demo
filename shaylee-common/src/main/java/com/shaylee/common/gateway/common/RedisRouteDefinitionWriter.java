package com.shaylee.common.gateway.common;

import com.shaylee.common.gateway.constant.CacheConstants;
import com.shaylee.common.gateway.vo.RouteDefinitionVO;
import com.shaylee.common.redis.service.CacheService;
import com.shaylee.common.gateway.support.RouteCacheHolder;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Title: redis 保存路由信息，优先级比配置文件高
 * Project: shaylee-gateway
 *
 * @author Adrian
 * @date 2020-03-22
 */
@Component
public class RedisRouteDefinitionWriter implements RouteDefinitionRepository {
	private static Logger logger = LoggerFactory.getLogger(RedisRouteDefinitionWriter.class);
	@Autowired
	private CacheService cacheService;

	@Override
	public Mono<Void> save(Mono<RouteDefinition> route) {
		return route.flatMap(r -> {
			RouteDefinitionVO vo = new RouteDefinitionVO();
			BeanUtils.copyProperties(r, vo);
			logger.info("保存路由信息{}", vo);
			cacheService.hSet(CacheConstants.ROUTE_KEY, r.getId(), vo);
			RouteCacheHolder.removeRouteList();
			return Mono.empty();
		});
	}

	@Override
	public Mono<Void> delete(Mono<String> routeId) {
		routeId.subscribe(id -> {
			logger.info("删除路由信息{}", id);
			cacheService.hDel(CacheConstants.ROUTE_KEY, id);
		});
		RouteCacheHolder.removeRouteList();
		return Mono.empty();
	}

	/**
	 * 动态路由入口
	 * <p>
	 * 1. 先从内存中获取
	 * 2. 为空加载Redis中数据
	 * 3. 更新内存
	 *
	 * @return
	 */
	@Override
	public Flux<RouteDefinition> getRouteDefinitions() {
		List<RouteDefinitionVO> routeList = RouteCacheHolder.getRouteList();
		if (CollectionUtils.isNotEmpty(routeList)) {
			logger.debug("内存 中路由定义条数： {}， {}", routeList.size(), routeList);
			return Flux.fromIterable(routeList);
		}

		Map<String, Object> map = cacheService.hGetAll(CacheConstants.ROUTE_KEY);
		List<RouteDefinitionVO> values = map.values().stream()
				.map(entity -> (RouteDefinitionVO)entity)
				.collect(Collectors.toList());

		logger.debug("redis 中路由定义条数： {}， {}", values.size(), values);

		RouteCacheHolder.setRouteList(values);
		return Flux.fromIterable(values);
	}
}
