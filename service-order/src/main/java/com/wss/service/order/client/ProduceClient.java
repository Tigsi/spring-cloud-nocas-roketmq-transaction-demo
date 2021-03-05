package com.wss.service.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "product-service")
public interface ProduceClient {

	/**
	 * @Title:
	 * @Description: 这样组合就相当于http://product-service/api/v1/product/find
	 * @author xub
	 * @throws
	 */
	@GetMapping("/api/v1/produce/find")
	String findById(@RequestParam(value = "produceId") int produceId);

}
