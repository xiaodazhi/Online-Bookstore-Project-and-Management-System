package com.panda.controller.shop;

import com.alibaba.fastjson.JSONArray;
import com.panda.service.ViewRecordService;
import com.panda.utils.CookieUtil;
import com.panda.utils.ResponseUtil;
import com.panda.utils.web.Controller;
import com.panda.utils.web.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ViewRecordController {
    @Path("/shop/getViewRecords.api")
    public static void getViewRecords(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userIdStr = CookieUtil.getCookieByName(request.getCookies(), "userId");
        int userId = Integer.parseInt(userIdStr);
        JSONArray result = ViewRecordService.getViewRecordList(userId);
        System.out.println(result.toJSONString());
        ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "查询成功", result.toJSONString());
    }
}
