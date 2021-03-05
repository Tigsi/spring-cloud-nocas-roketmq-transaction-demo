package com.wss.service.product.service.impl;

import com.wss.service.product.entity.Produce;
import com.wss.service.product.service.ProductService;
import org.springframework.stereotype.Service;

@Service("productService")
public class ProductServiceImpl implements ProductService {
	@Override
	public Produce findById(int produceId) {
		return null;
	}

	@Override
	public void updateStore(int produceId, int store, String key) throws Exception {

	}
}
