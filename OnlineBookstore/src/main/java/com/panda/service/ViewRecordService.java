package com.panda.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.panda.domain.Book;
import com.panda.domain.ViewRecord;
import com.panda.utils.TimeUtil;
import com.panda.utils.db.DBUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewRecordService {

    public static void addViewRecord(ViewRecord viewRecord) throws Exception {
        DBUtil.create(viewRecord);
    }

    public static JSONArray getViewRecordList(int userId) throws Exception {
        //假设只查询一周之内的
        int now = TimeUtil.getNow();
        int begin = now - 7 * 24 * 3600;
        List<ViewRecord> viewRecords = DBUtil.read(ViewRecord.class, "*", "user_id = ? and ctime between ? and ?;", userId, begin, now);
        JSONArray result = new JSONArray();
        Map<Integer, Book> bookMap = new HashMap<>();
        for (ViewRecord viewRecord : viewRecords) {
            Book book = bookMap.get(viewRecord.getBookId());
            if (book == null) {
                book = BookService.getBookById(viewRecord.getBookId());
                bookMap.put(book.getId(), book);
            }
            result.add(JSON.toJSONString(book));
        }
        return result;
    }
}
