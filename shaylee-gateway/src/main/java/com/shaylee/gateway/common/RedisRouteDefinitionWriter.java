package com.shaylee.gateway.common;

import com.shaylee.gateway.constant.CacheConstants;
import com.shaylee.gateway.support.RouteCacheHolder;
import com.shaylee.gateway.vo.RouteDefinitionVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
	private RedisTemplate redisTemplate;

	@Override
	public Mono<Void> save(Mono<RouteDefinition> route) {
		return route.flatMap(r -> {
			RouteDefinitionVO vo = new RouteDefinitionVO();
			BeanUtils.copyProperties(r, vo);
			logger.info("保存路由信息{}", vo);
			redisTemplate.setKeySerializer(new StringRedisSerializer());
			redisTemplate.opsForHash().put(CacheConstants.ROUTE_KEY, r.getId(), vo);
			redisTemplate.convertAndSend(CacheConstants.ROUTE_JVM_RELOAD_TOPIC, "新增路由信息,网关缓存更新");
			return Mono.empty();
		});
	}

	@Override
	public Mono<Void> delete(Mono<String> routeId) {
		routeId.subscribe(id -> {
			logger.info("删除路由信息{}", id);
			redisTemplate.setKeySerializer(new StringRedisSerializer());
			redisTemplate.opsForHash().delete(CacheConstants.ROUTE_KEY, id);
		});
		redisTemplate.convertAndSend(CacheConstants.ROUTE_JVM_RELOAD_TOPIC, "删除路由信息,网关缓存更新");
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

		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(RouteDefinitionVO.class));
		List<RouteDefinitionVO> values = redisTemplate.opsForHash().values(CacheConstants.ROUTE_KEY);
		logger.debug("redis 中路由定义条数： {}， {}", values.size(), values);

		RouteCacheHolder.setRouteList(values);
		return Flux.fromIterable(values);
	}
}
