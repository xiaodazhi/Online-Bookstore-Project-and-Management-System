package com.panda.controller.shop;

import com.panda.service.OrderService;
import com.panda.utils.CookieUtil;
import com.panda.utils.ResponseUtil;
import com.panda.utils.web.Controller;
import com.panda.utils.web.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class OrderController {

    @Path("/shop/order.api")
    public static void order(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userIdStr = CookieUtil.getCookieByName(request.getCookies(), "userId");
        String address = request.getParameter("address");
        int userId = Integer.parseInt(userIdStr);
        OrderService.addOrder(userId, address);
        ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "下单成功", null);
    }
}
