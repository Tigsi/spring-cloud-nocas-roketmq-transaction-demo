package com.wss.service.product.controller;

import com.alibaba.fastjson.JSON;
import com.wss.service.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/produce")
public class ProduceController {

	@Autowired
	private ProductService productService;

	/**
	 * 根据主键ID获取商品
	 */
	@GetMapping("/find")
	public String findById(@RequestParam(value = "produceId") int produceId) {
		return JSON.toJSONString(productService.findById(produceId));

	}

}
