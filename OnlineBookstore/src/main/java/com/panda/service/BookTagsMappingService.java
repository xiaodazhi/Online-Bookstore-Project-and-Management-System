package com.panda.service;

import com.panda.domain.BookTagsMapping;
import com.panda.utils.db.DBUtil;

import java.util.List;

public class BookTagsMappingService {

    public static void addBookTagsMapping(BookTagsMapping bookTagsMapping) throws Exception {
        DBUtil.create(bookTagsMapping);
    }

    public static List<BookTagsMapping> getBookTagsMappingByBookId(int bookId) throws Exception {
        return DBUtil.read(BookTagsMapping.class, "book_id=?", bookId);
    }

    public static List<BookTagsMapping> getBookTagsMappingByTagsId(int tagsId) throws Exception {
        return DBUtil.read(BookTagsMapping.class, "tags_id=?", tagsId);
    }

}
