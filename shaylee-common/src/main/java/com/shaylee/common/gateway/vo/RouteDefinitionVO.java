package com.shaylee.common.gateway.vo;

import org.springframework.cloud.gateway.route.RouteDefinition;

import java.io.Serializable;

/**
 * Title: 扩展此类支持序列化a
 * Project: shaylee-gateway
 *
 * @author Adrian
 * @date 2020-03-22
 */
public class RouteDefinitionVO extends RouteDefinition implements Serializable {
	/**
	 * 路由名称
	 */
	private String routeName;

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}
}
