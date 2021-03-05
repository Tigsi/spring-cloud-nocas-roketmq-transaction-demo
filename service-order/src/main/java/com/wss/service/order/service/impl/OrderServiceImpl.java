package com.wss.service.order.service.impl;

import com.wss.service.order.dao.OrderMapper;
import com.wss.service.order.dao.TransactionLogMapper;
import com.wss.service.order.service.OrderService;
import com.wss.service.order.entity.Order;
import com.wss.service.order.entity.TransactionLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service("orderService")
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderMapper orderMapper;
//	@Autowired
//	private ProduceClient produceClient;
	@Autowired
	private TransactionLogMapper transactionLogMapper;

	@Override
	@Transactional
	public void createOrder(Order order,TransactionLog transactionLog) {
		orderMapper.insert(order);
		transactionLogMapper.insert(transactionLog);
	}
}
