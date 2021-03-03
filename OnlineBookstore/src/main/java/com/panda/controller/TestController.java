package com.panda.controller;

import com.panda.utils.web.Controller;
import com.panda.utils.web.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class TestController {

    @Path("/test.api")
    public void test(HttpServletRequest request, HttpServletResponse response) {

    }

    @Path("/test2.api")
    public void test2(HttpServletRequest request, HttpServletResponse response) {

    }
}
