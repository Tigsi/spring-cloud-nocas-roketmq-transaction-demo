package com.wss.service.order.service;

import com.wss.service.order.entity.Order;
import com.wss.service.order.entity.TransactionLog;

public interface OrderService {
	void createOrder(Order order, TransactionLog transactionLog);
}
