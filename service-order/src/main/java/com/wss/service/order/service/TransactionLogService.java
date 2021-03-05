package com.wss.service.order.service;

import com.wss.service.order.entity.TransactionLog;
import org.springframework.stereotype.Service;


public interface TransactionLogService {
	int insert(TransactionLog transactionLog);
}
