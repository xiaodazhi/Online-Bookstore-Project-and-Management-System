package com.panda.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.panda.domain.Book;
import com.panda.domain.BookTagsMapping;
import com.panda.service.BookService;
import com.panda.service.BookTagsMappingService;
import com.panda.utils.FileUploadUtil;
import com.panda.utils.ResponseUtil;
import com.panda.utils.TimeUtil;
import com.panda.utils.web.Controller;
import com.panda.utils.web.Path;
import org.apache.commons.fileupload.FileUploadException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class BookController {

    private static String[] suffixes = new String[]{"jpg", "jpeg", "png"};
    private static long limitSize = 2 * 1024 * 1024;

    @Path("/admin/book/add.api")
    public void addBooks(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Map<String, String> params = FileUploadUtil.uploadFile(request, suffixes, limitSize, 1);

            String name = params.get("name");
            String author = params.get("author");
            int count = Integer.parseInt(params.get("count"));
            double price = Double.parseDouble(params.get("price"));
            String detail = params.get("detail");
            JSONArray jsonArray = JSON.parseArray(params.get("pic"));
            String picUrl = jsonArray.getString(0);

            Book book = new Book();
            book.setName(name);
            book.setAuthor(author);
            book.setCount(count);
            book.setPrice(price);
            book.setDetail(detail);
            book.setCtime(TimeUtil.getNow());
            book.setPicUrl(picUrl);

            int bookId = BookService.addBook(book);

            String tags = params.get("tags");
            int tagsId = Integer.parseInt(tags);

            BookTagsMapping bookTagsMapping = new BookTagsMapping();
            bookTagsMapping.setBookId(bookId);
            bookTagsMapping.setTagsId(tagsId);
            bookTagsMapping.setCtime(TimeUtil.getNow());

            BookTagsMappingService.addBookTagsMapping(bookTagsMapping);

        } catch (FileUploadUtil.SuffixNotMatchException e) {
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.FAIL, "后缀不符合要求", null);
        } catch (FileUploadUtil.OutOfLimitSizeException e) {
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.FAIL, "超出文件大小限制", null);
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (FileUploadUtil.OutOfLimitFileCountException e) {
            ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.FAIL, "超出文件数量限制", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "上传成功", null);
    }

    @Path("/admin/book/allBook.api")
    public void allBooks(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Book> result = BookService.getAllBook();
        ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "查询成功", JSON.toJSONString(result));
    }

    @Path("/admin/book/delete.api")
    public void deleteBook(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        Book book = new Book();
        book.setId(Integer.parseInt(id));
        BookService.deleteBook(book);
        ResponseUtil.writeJSON(response, ResponseUtil.ResponseEnum.OK, "删除成功", null);
    }
}
