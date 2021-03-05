package com.wss.service.order.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wss.service.order.entity.TransactionLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TransactionLogMapper extends BaseMapper<TransactionLog> {
}
