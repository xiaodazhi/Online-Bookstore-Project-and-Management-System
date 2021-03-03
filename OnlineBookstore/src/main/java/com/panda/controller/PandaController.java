package com.panda.controller;

import com.panda.utils.web.Controller;
import com.panda.utils.web.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class PandaController {

    @Path("/panda.api")
    public void panda(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    @Path("/panda2.api")
    public void panda2(HttpServletRequest request, HttpServletResponse response) {

    }
}
