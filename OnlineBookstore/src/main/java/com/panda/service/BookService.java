package com.panda.service;

import com.panda.domain.Book;
import com.panda.domain.BookTagsMapping;
import com.panda.utils.db.DBUtil;

import java.util.ArrayList;
import java.util.List;

public class BookService {

    public static int addBook(Book book) throws Exception {
        try {
            return DBUtil.create(book);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static Book getBookById(int id) throws Exception {
        List<Book> books = DBUtil.read(Book.class, "id=?", id);
        if (books != null && books.size() > 0) {
            return books.get(0);
        } else {
            return null;
        }
    }

    public static Book getBookByName(String name) throws Exception {
        List<Book> books = DBUtil.read(Book.class, "name=?", name);
        if (books != null && books.size() > 0) {
            return books.get(0);
        } else {
            return null;
        }
    }

    public static List<Book> getAllBook() throws Exception {
        return DBUtil.readAll(Book.class);
    }

    public static List<Book> getBookByPage(int offset, int limit) throws Exception {
        return DBUtil.read(Book.class, "*", "", "", "?,?", offset, limit);
    }
    public static int getBookCount() throws Exception {
        return DBUtil.readCount(Book.class, null);
    }

    public static List<Book> getBookByTagsPage(int tagsId, int offset, int limit) throws Exception {
        List<BookTagsMapping> bookTagsMappingList = DBUtil.read(BookTagsMapping.class, "*", "tagsId=?", "book_id", "?,?", tagsId, offset, limit);
        List<Book> bookList = new ArrayList<>(bookTagsMappingList.size());
        for (BookTagsMapping bookTagsMapping : bookTagsMappingList) {
            List<Book> books = DBUtil.read(Book.class, "id=?", bookTagsMapping.getBookId());
            if(books != null && books.size() > 0) {
                bookList.add(books.get(0));
            }
        }
        return bookList;
    }
    public static int getBookByTagsCount(int tagsId) throws Exception {
        return DBUtil.readCount(BookTagsMapping.class, "tags_id=?", tagsId);
    }

    public static void updateBook(Book book) throws Exception {
        DBUtil.update(book);
    }

    public static void deleteBook(Book book) throws Exception {
        DBUtil.delete(book);
    }
}
