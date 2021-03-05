package com.wss.service.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.wss.service.order.common.SnowflakeIdWorker;
import com.wss.service.order.config.Jms;
import com.wss.service.order.service.mq.OrderTransactionProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
@Slf4j
@RequestMapping("api/v1/order")
public class OrderController {
	@Autowired
	private Jms jms;
	@Autowired
	private OrderTransactionProducer transactionProducer;

	@GetMapping("save")
	public Object save(@RequestParam("userId") int userId, @RequestParam("productId") int productId, @RequestParam("total") int total) throws MQClientException {
		// 雪花算法生成事务id
		String id = SnowflakeIdWorker.generateId().toString();
		// 封装消息
		JSONObject msgJson = new JSONObject();
		msgJson.put("productId", productId);
		msgJson.put("total", total);
		msgJson.put("transactionId", id);

		String jsonString = msgJson.toJSONString();
		Message message = new Message(jms.getOrderTopic(), null, id, jsonString.getBytes(StandardCharsets.UTF_8));

		//发送消息 用 sendMessageInTransaction  第一个参数可以理解成消费方需要的参数 第二个参数可以理解成消费方不需要 本地事务需要的参数
		TransactionSendResult sendResult = transactionProducer.getProducer().sendMessageInTransaction(message, userId);
		System.out.printf("发送结果=%s, sendResult=%s \n", sendResult.getSendStatus(), sendResult.toString());
		if (SendStatus.SEND_OK == sendResult.getSendStatus()) {
			return "success";
		}
		return "error";
	}
}
