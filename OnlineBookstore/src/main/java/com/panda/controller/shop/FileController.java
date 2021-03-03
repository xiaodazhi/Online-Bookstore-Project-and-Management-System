package com.panda.controller.shop;

import com.panda.utils.web.Controller;
import com.panda.utils.web.Path;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

@Controller
public class FileController {

    @Path("/shop/getPic.api")
    public void allBooks(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String path = request.getParameter("path");
        File file = new File(path);
        InputStream inputStream = new FileInputStream(file);
        OutputStream outputStream = response.getOutputStream();
        IOUtils.copy(inputStream, outputStream);
    }


}
