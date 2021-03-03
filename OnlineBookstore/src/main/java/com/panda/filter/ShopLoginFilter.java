package com.panda.filter;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ShopLoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    private static String[] ignorePath = {
            "/shop/Login.html",
            "/shop/Register.html",
            "/shop/register.api",
            "/shop/login.api"};

    public static boolean isIgnore(String path) {
        for (String temp : ignorePath) {
            if (temp.equals(path)) return true;
        }
        return false;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("检查是否登录");
        Cookie[] cookies = ((HttpServletRequest) servletRequest).getCookies();
        String adminId = "";
        for (Cookie cookie : cookies) {
            if ("userId".equals(cookie.getName())) {
                adminId = cookie.getValue();
            }
        }
        String path = ((HttpServletRequest) servletRequest).getServletPath();
        //登录页面，注册页面，登录接口，注册接口
        if ("".equals(adminId) && !isIgnore(path)) {
            //跳转到登录页面(重定向)
            ((HttpServletResponse) servletResponse).sendRedirect("/shop/Login.html");
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
