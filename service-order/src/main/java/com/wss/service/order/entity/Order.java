package com.wss.service.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("`order`")
public class Order {
	@TableId(type = IdType.INPUT)
	private String id;

	private String product_id;
	private Integer total;
}
