package com.panda.service;

import com.panda.domain.Book;
import com.panda.domain.Cart;
import com.panda.domain.Order;
import com.panda.domain.OrderBookMapping;
import com.panda.utils.TimeUtil;
import com.panda.utils.db.DBUtil;

import java.util.List;
import java.util.UUID;

public class OrderService {


    public static void addOrder(int userId, String address) throws Exception {

        //写入order概况表
        Order order = new Order();
        order.setAddress(address);
        order.setCtime(TimeUtil.getNow());
        order.setStatus(1);//1已下单，2已发货，3已接收
        order.setTotalPrice(0.0);
        order.setUserId(userId);
        order.setOrderNum(UUID.randomUUID().toString());
        int orderId = DBUtil.create(order);

        List<Cart> carts = CartService.getCartList(userId);
        double totalPrice = 0.0;
        for (Cart cart : carts) {
            Book book = BookService.getBookById(cart.getBookId());
            double price = book.getPrice() * cart.getCount();
            OrderBookMapping orderBookMapping = new OrderBookMapping();
            orderBookMapping.setBookId(cart.getBookId());
            orderBookMapping.setCount(cart.getCount());
            orderBookMapping.setOrderId(orderId);
            orderBookMapping.setCtime(TimeUtil.getNow());
            orderBookMapping.setPrice(price);
            DBUtil.create(orderBookMapping);//写入order详情表
            totalPrice += price;
        }
        order.setId(orderId);
        order.setTotalPrice(totalPrice);
        DBUtil.update(order);//修改order总价

        DBUtil.delete(Cart.class, "user_id = ?", userId);
    }

    public static List<Order> getOrderList() throws Exception {
        return DBUtil.read(Order.class, "*", "status=1");
    }

    public static void orderSend(int orderId) throws Exception {
        List<Order> orders = DBUtil.read(Order.class, "*", "id=?", orderId);
        if (orders != null && !orders.isEmpty()) {
            Order order = orders.get(0);
            order.setStatus(2);
            DBUtil.update(order);
        }
    }
}
