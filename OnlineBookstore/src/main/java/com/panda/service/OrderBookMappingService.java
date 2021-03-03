package com.panda.service;

import com.panda.domain.OrderBookMapping;
import com.panda.utils.db.DBUtil;

import java.util.List;

public class OrderBookMappingService {

    public static List<OrderBookMapping> getOrderBookMappingByOrderId(int orderId) throws Exception {
        return DBUtil.read(OrderBookMapping.class, "*", "order_id=?", orderId);
    }
}
