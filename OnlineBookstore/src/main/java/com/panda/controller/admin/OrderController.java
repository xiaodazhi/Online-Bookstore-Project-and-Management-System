package com.panda.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.panda.domain.Book;
import com.panda.domain.Order;
import com.panda.domain.OrderBookMapping;
import com.panda.service.BookService;
import com.panda.service.OrderBookMappingService;
import com.panda.service.OrderService;
import com.panda.utils.ResponseUtil;
import com.panda.utils.web.Controller;
import com.panda.utils.web.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class OrderController {

    @Path("/admin/orderList.api")
    public void orderList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Order> orderList = OrderService.getOrderList();
        JSONArray result = new JSONArray();
        for (Order order : orderList) {
            StringBuilder orderStr = new StringBuilder("订单号：");
            orderStr.append(order.getId());
            orderStr.append("，");
            List<OrderBookMapping> orderBookMappings = OrderBookMappingService.getOrderBookMappingByOrderId(order.getId());
            for (OrderBookMapping orderBookMapping : orderBookMappings) {
                Book book = BookService.getBookById(orderBookMapping.getBookId());
                orderStr.append(book.getName());
                orderStr.append("-");
                orderStr.append(orderBookMapping.getCount());
                orderStr.append("本-");
                orderStr.append(orderBookMapping.getPrice());
                orderStr.append("元，");
            }
            orderStr.append("总价：");
            orderStr.append(order.getTotalPrice());
            orderStr.append("元，地址：");
            orderStr.append(order.getAddress());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("orderId", order.getId());
            jsonObject.put("msg", orderStr.toString());
            result.add(jsonObject);
        }
        ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "查询成功", result.toJSONString());
    }

    @Path("/admin/orderSend.api")
    public void orderSend(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String orderIdStr = request.getParameter("orderId");
        int orderId = Integer.parseInt(orderIdStr);
        OrderService.orderSend(orderId);
    }
}
