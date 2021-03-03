package com.panda.utils;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUtil {

    public enum ResponseEnum {
        OK("OK"), FAIL("FAIL");
        private String value;
        ResponseEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static void writeJSON(HttpServletResponse response, ResponseEnum responseEnum, String msg, String data) throws IOException {
        response.setCharacterEncoding("utf-8");
        JSONObject result = new JSONObject();
        result.put("status", responseEnum.getValue());
        result.put("msg", msg);
        result.put("data", data);
        response.getWriter().write(result.toJSONString());
    }
}
