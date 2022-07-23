package com.example.reggie.service.impl;

import com.example.reggie.entity.Orders;
import com.example.reggie.mapper.OrdersMapper;
import com.example.reggie.service.OrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author chy
 * @since 2022-07-21
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

}
