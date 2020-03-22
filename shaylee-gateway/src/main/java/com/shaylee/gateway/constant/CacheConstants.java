package com.shaylee.gateway.constant;

/**
 * Title: 缓存的key 常量
 * Project: shaylee-gateway
 *
 * @author Adrian
 * @date 2020-03-22
 */
public interface CacheConstants {
	/**
	 * 路由存放
	 */
	String ROUTE_KEY = "gateway_route_key";

	String ROUTE_HASH_KEY = "gateway_route_hash_key";

	/**
	 * redis reload 事件
	 */
	String ROUTE_REDIS_RELOAD_TOPIC = "gateway_redis_route_reload_topic";

	/**
	 * 内存reload 时间
	 */
	String ROUTE_JVM_RELOAD_TOPIC = "gateway_jvm_route_reload_topic";
}
