package com.panda.controller.shop;

import com.panda.domain.User;
import com.panda.service.UserService;
import com.panda.utils.ResponseUtil;
import com.panda.utils.web.Controller;
import com.panda.utils.web.Path;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {
    @Path("/shop/login.api")
    public void userLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String account = request.getParameter("account");
        String password = request.getParameter("password");
        boolean result = UserService.login(account, password);
        if (!result) {
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.FAIL, "用户名或密码有误", null);
            return;
        }
        User user = UserService.getUserByAccount(account);
        Cookie cookie = new Cookie("userId", "" + user.getId());
        cookie.setPath("/shop");
        response.addCookie(cookie);
        ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "登录成功", null);
        return;
    }

    @Path("/shop/register.api")
    public void adminRegister(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String account = request.getParameter("account");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
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
        if (email == null || email.equals("")) {
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.FAIL, "必须要有Email ~ 16", null);
            return;
        }

        User user = UserService.getUserByAccount(account);
        if (user != null) {
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.FAIL, "用户已注册", null);
            return;
        } else {
            user = new User();
            user.setAccount(account);
            user.setPassword(password);
            user.setEmail(email);
            user.setAddress("");
            UserService.addUser(user);
            System.out.println(user);
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "注册成功", null);
            return;
        }
    }
}
