package com.panda.controller.shop;

import com.alibaba.fastjson.JSON;
import com.panda.domain.Book;
import com.panda.domain.ViewRecord;
import com.panda.service.BookService;
import com.panda.service.ViewRecordService;
import com.panda.utils.CookieUtil;
import com.panda.utils.ResponseUtil;
import com.panda.utils.TimeUtil;
import com.panda.utils.web.Controller;
import com.panda.utils.web.Path;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class BookController {

    private static String[] suffixes = new String[]{"jpg", "jpeg", "png"};
    private static long limitSize = 2 * 1024 * 1024;

    @Path("/shop/book/allBook.api")
    public void allBooks(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Book> result = BookService.getAllBook();
        ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "查询成功", JSON.toJSONString(result));
    }

    @Path("/shop/getBook.api")
    public void getBook(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String idStr = request.getParameter("id");
        int id = Integer.parseInt(idStr);//book id
        Book book = BookService.getBookById(id);
        ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "查询成功", JSON.toJSONString(book));

        Cookie[] cookies = request.getCookies();
        String userId = CookieUtil.getCookieByName(cookies, "userId");//用户id

        ViewRecord viewRecord = new ViewRecord();
        viewRecord.setBookId(id);
        viewRecord.setUserId(Integer.parseInt(userId));
        viewRecord.setCtime(TimeUtil.getNow());
        ViewRecordService.addViewRecord(viewRecord);

    }

}
