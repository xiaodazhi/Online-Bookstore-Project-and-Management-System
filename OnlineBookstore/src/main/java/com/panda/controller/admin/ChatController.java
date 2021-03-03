package com.panda.controller.admin;

import com.alibaba.fastjson.JSON;
import com.panda.service.ChatService;
import com.panda.utils.ResponseUtil;
import com.panda.utils.web.Controller;
import com.panda.utils.web.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class ChatController {
    @Path("/admin/chat/adminSend.api")
    public void userSend(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userIdStr = request.getParameter("userId");
        int userId = Integer.parseInt(userIdStr);
        String content = request.getParameter("text");
        ChatService.addChatMsg(userId, content, 2);
    }

    @Path("/admin/chat/chatList.api")
    public void chatList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String result = ChatService.getChatList();
        ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "查询成功", result);
    }

    @Path("/admin/chat/getChatMsg.api")
    public void getChatMsg(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userIdStr = request.getParameter("userId");
        int userId = Integer.parseInt(userIdStr);
        List<String> result = ChatService.getChatRecord(userId);
        ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "查询成功", JSON.toJSONString(result));
    }


}
