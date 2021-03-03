package com.panda.service;

import com.panda.domain.Book;
import com.panda.domain.Favorite;
import com.panda.utils.db.DBUtil;

import java.util.ArrayList;
import java.util.List;

public class FavoriteService {

    public static void addFavorite(Favorite favorite) throws Exception {
        DBUtil.create(favorite);
    }

    public static List<Book> getFavoList(int userId) throws Exception {
        List<Favorite> favorites = DBUtil.read(Favorite.class, "*", "user_id=?;", userId);
        List<Book> result = new ArrayList<>();
        for (Favorite favorite : favorites) {
            Book book = BookService.getBookById(favorite.getBookId());
            result.add(book);
        }
        return result;
    }

    public static void cancelFavo(int userId, int bookId) throws Exception {
        DBUtil.delete(Favorite.class, "user_id = ? and book_id = ? ;", userId, bookId);
    }
}
