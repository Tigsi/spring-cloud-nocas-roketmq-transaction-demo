package com.wss.service.order.service.impl;

import com.wss.service.order.dao.TransactionLogMapper;
import com.wss.service.order.service.TransactionLogService;
import com.wss.service.order.entity.TransactionLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("transactionLogService")
public class TransactionLogServiceImpl implements TransactionLogService {
	@Autowired
	private TransactionLogMapper transactionLogMapper;

	public int insert(TransactionLog transactionLog) {
		return transactionLogMapper.insert(transactionLog);
	}
}
