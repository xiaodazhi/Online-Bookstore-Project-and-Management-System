package com.panda.controller.shop;

import com.alibaba.fastjson.JSON;
import com.panda.service.ChatService;
import com.panda.utils.CookieUtil;
import com.panda.utils.ResponseUtil;
import com.panda.utils.web.Controller;
import com.panda.utils.web.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class ChatController {

    @Path("/shop/chat/userSend.api")
    public void userSend(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userIdStr = CookieUtil.getCookieByName(request.getCookies(), "userId");
        int userId = Integer.parseInt(userIdStr);
        String content = request.getParameter("text");
        ChatService.addChatMsg(userId, content, 1);
    }

    @Path("/shop/chat/getChatMsg.api")
    public void getChatMsg(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userIdStr = CookieUtil.getCookieByName(request.getCookies(), "userId");
        int userId = Integer.parseInt(userIdStr);
        List<String> result = ChatService.getChatRecord(userId);
        ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "查询成功", JSON.toJSONString(result));
    }
}
