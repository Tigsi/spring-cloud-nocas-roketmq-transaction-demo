package com.wss.service.order.service.mq;


import com.alibaba.fastjson.JSONObject;
import com.wss.service.order.common.SnowflakeIdWorker;
import com.wss.service.order.config.Jms;
import com.wss.service.order.service.OrderService;
import com.wss.service.order.entity.Order;
import com.wss.service.order.entity.TransactionLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.BrokerConfig;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Slf4j
@Component
public class OrderTransactionProducer {
	/**
	 * 需要自定义事务监听器 用于 事务的二次确认 和 事务回查
	 */
	private TransactionListener transactionListener;

	/**
	 * 这里的生产者和之前的不一样
	 */
	private TransactionMQProducer producer = null;

	/**
	 * 官方建议自定义线程 给线程取自定义名称 发现问题更好排查
	 */
	private ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS,
			new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
		@Override
		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r);
			thread.setName("client-transaction-msg-check-thread");
			return thread;
		}

	});

	public OrderTransactionProducer(@Autowired Jms jms,@Autowired OrderService orderService) {

		transactionListener = new TransactionListenerImpl(orderService);
		// 初始化 事务生产者
		producer = new TransactionMQProducer(jms.getOrderTopic());
		// 添加服务器地址
		producer.setNamesrvAddr(jms.getNameServer());
		// 添加事务监听器
		producer.setTransactionListener(transactionListener);
		// 添加自定义线程池
		producer.setExecutorService(executorService);
		start();
	}

	public TransactionMQProducer getProducer() {
		return this.producer;
	}

	/**
	 * 对象在使用之前必须要调用一次，只能初始化一次
	 */
	public void start() {
		try {
			this.producer.start();
		} catch (MQClientException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 一般在应用上下文，使用上下文监听器，进行关闭
	 */
	public void shutdown() {
		this.producer.shutdown();
	}
}

@Slf4j
class TransactionListenerImpl implements TransactionListener {

	private final OrderService orderService;
	public TransactionListenerImpl(OrderService orderService) {
		this.orderService = orderService;
	}

	@Override
	public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
		log.info("=========本地事务开始执行=============");
		LocalTransactionState state;
		try {
			String message = new String(msg.getBody());
			JSONObject jsonObject = JSONObject.parseObject(message);
			String productId = jsonObject.getString("productId");
			Integer total = jsonObject.getInteger("total");
			int userId = Integer.parseInt(arg.toString());

			log.info("本地事务执行参数,用户id={},商品ID={},销售库存={}", userId, productId, total);
			Order order = new Order();
			order.setId(SnowflakeIdWorker.generateId().toString());
			order.setTotal(total);
			order.setProduct_id(productId);

			TransactionLog transactionLog = new TransactionLog();
			transactionLog.setId(msg.getTransactionId());
			transactionLog.setBusiness("订单入库");
			transactionLog.setForeign_key(productId);
			try {
				orderService.createOrder(order, transactionLog);
				state = LocalTransactionState.COMMIT_MESSAGE;
			}catch (Exception e){
				log.info("执行本地事务失败。{}", e);
				state = LocalTransactionState.ROLLBACK_MESSAGE;
			}
			int i= 1/0;
		} catch (Exception e) {
			log.info("执行本地事务方法执行失败。{}", e);
			state = LocalTransactionState.UNKNOW;
		}
		System.out.println(state.toString());
		return state;

	}

	/**
	 * 只有上面接口返回 LocalTransactionState.UNKNOW 才会调用查接口被调用
	 *
	 * @param msg 消息
	 * @return
	 */
	@Override
	public LocalTransactionState checkLocalTransaction(MessageExt msg) {
		log.info("==========回查接口=========");
//		// String key = msg.getKeys();
//		String transactionId = msg.getTransactionId();
//		// 根据 transactionId 查询本地事务是否执行完毕
//		//TODO 1、必须根据key先去检查本地事务消息是否完成。
//		/**
//		 * 因为有种情况就是：上面本地事务执行成功了，但是return LocalTransactionState.COMMIT_MESSAG的时候
//		 * 服务挂了，那么最终 Brock还未收到消息的二次确定，还是个半消息 ，所以当重新启动的时候还是回调这个回调接口。
//		 * 如果不先查询上面本地事务的执行情况 直接在执行本地事务，那么就相当于成功执行了两次本地事务了。
//		 */
//		// TODO 2、这里返回要么commit 要么rollback。没有必要在返回 UNKNOW
		return LocalTransactionState.COMMIT_MESSAGE;
	}
}
