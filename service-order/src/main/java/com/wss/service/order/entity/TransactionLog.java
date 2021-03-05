package com.wss.service.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("`transaction_log`")
public class TransactionLog {
	@TableId(type= IdType.INPUT)
	private String id;

	private String business;

	private String foreign_key;
}
