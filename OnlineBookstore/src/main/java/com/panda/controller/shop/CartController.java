package com.panda.controller.shop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.panda.domain.Book;
import com.panda.domain.Cart;
import com.panda.service.BookService;
import com.panda.service.CartService;
import com.panda.utils.CookieUtil;
import com.panda.utils.ResponseUtil;
import com.panda.utils.web.Controller;
import com.panda.utils.web.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class CartController {

    @Path("/shop/addCart.api")
    public static void addCart(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userIdStr = CookieUtil.getCookieByName(request.getCookies(), "userId");
        String bookIdStr = request.getParameter("id");
        int userId = Integer.parseInt(userIdStr);
        int bookId = Integer.parseInt(bookIdStr);
        CartService.addCart(bookId, userId);
        ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "添加成功", null);
    }

    @Path("/shop/cartList.api")
    public static void cartList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userIdStr = CookieUtil.getCookieByName(request.getCookies(), "userId");
        int userId = Integer.parseInt(userIdStr);
        List<Cart> cartList = CartService.getCartList(userId);
        JSONArray result = new JSONArray();
        for (Cart cart : cartList) {
            Book book = BookService.getBookById(cart.getBookId());
            String bookJson = JSON.toJSONString(book);
            JSONObject jsonObject = JSON.parseObject(bookJson);
            jsonObject.put("count", cart.getCount());
            result.add(jsonObject);
        }
        ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "查询成功", result.toJSONString());
    }

    @Path("/shop/removeCart.api")
    public static void removeCart(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userIdStr = CookieUtil.getCookieByName(request.getCookies(), "userId");
        String bookIdStr = request.getParameter("id");
        int userId = Integer.parseInt(userIdStr);
        int bookId = Integer.parseInt(bookIdStr);
        CartService.removeCart(userId, bookId);
    }
}
