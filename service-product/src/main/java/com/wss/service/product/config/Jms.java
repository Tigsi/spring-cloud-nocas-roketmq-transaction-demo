package com.wss.service.product.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@RefreshScope
public class Jms {
	/**
	 * 配置中心读取 服务器地址
	 */
	@Value("${config.name_server}")
	private String nameServer;

	/**
	 * 配置中心读取 主题
	 */
	@Value("${config.order_topic}")
	private String orderTopic;

}
