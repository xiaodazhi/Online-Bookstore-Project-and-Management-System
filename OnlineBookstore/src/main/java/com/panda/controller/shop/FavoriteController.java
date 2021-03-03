package com.panda.controller.shop;

import com.alibaba.fastjson.JSON;
import com.panda.domain.Book;
import com.panda.domain.Favorite;
import com.panda.service.FavoriteService;
import com.panda.utils.CookieUtil;
import com.panda.utils.ResponseUtil;
import com.panda.utils.TimeUtil;
import com.panda.utils.web.Controller;
import com.panda.utils.web.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class FavoriteController {

    @Path("/shop/favo.api")
    public static void addFavo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userIdStr = CookieUtil.getCookieByName(request.getCookies(), "userId");
        String bookIdStr = request.getParameter("id");
        System.out.println(userIdStr);
        System.out.println(bookIdStr);
        Favorite favorite = new Favorite();
        favorite.setBookId(Integer.parseInt(bookIdStr));
        favorite.setUserId(Integer.parseInt(userIdStr));
        favorite.setCtime(TimeUtil.getNow());
        FavoriteService.addFavorite(favorite);
        ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "收藏成功", null);
    }

    @Path("/shop/favoList.api")
    public static void favoList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userIdStr = CookieUtil.getCookieByName(request.getCookies(), "userId");
        int userId = Integer.parseInt(userIdStr);
        List<Book> result = FavoriteService.getFavoList(userId);
        ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "查询成功", JSON.toJSONString(result));
    }

    @Path("/shop/cancelFavo.api")
    public static void cancelFavo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userIdStr = CookieUtil.getCookieByName(request.getCookies(), "userId");
        int userId = Integer.parseInt(userIdStr);
        String bookIdStr = request.getParameter("id");
        int bookId = Integer.parseInt(bookIdStr);
        FavoriteService.cancelFavo(userId, bookId);
        ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "取消成功", null);
    }
}
