package com.panda.controller.admin;

import com.panda.domain.Admin;
import com.panda.service.AdminService;
import com.panda.utils.ResponseUtil;
import com.panda.utils.web.Controller;
import com.panda.utils.web.Path;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {
    @Path("/admin/login.api")
    public void adminLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String account = request.getParameter("account");
        String password = request.getParameter("password");
        Admin admin = AdminService.getAdminByAccount(account);
        if (admin == null) {
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.FAIL, "找不到这个用户", null);
            return;
        }
        if (admin.getPassword().equals(password)) {
            Cookie cookie = new Cookie("adminId", "" + admin.getId());
//            cookie.setMaxAge(10);
//            cookie.setDomain("baidu.com");
            cookie.setPath("/admin");
            response.addCookie(cookie);
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "登录成功", null);
            return;
        } else {
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.FAIL, "用户名密码不匹配", null);
            return;
        }
    }

    @Path("/admin/register.api")
    public void adminRegister(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String account = request.getParameter("account");
        String password = request.getParameter("password");
        if ("".equals(account) || "".equals(password)) {
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.FAIL, "用户名和密码不能为空", null);
            return;
        }
        if (account.length() < 4 || account.length() > 16) {
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.FAIL, "用户名长度为4 ~ 16", null);
            return;
        }
        if (password.length() < 4 || password.length() > 16) {
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.FAIL, "密码长度为4 ~ 16", null);
            return;
        }
        Admin admin = AdminService.getAdminByAccount(account);
        if (admin != null) {
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.FAIL, "用户已注册", null);
            return;
        } else {
            admin = new Admin();
            admin.setAccount(account);
            admin.setPassword(password);
            AdminService.register(admin);
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "注册成功", null);
            return;
        }
    }
}
