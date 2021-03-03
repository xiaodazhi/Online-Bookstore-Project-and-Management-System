package com.panda.service;

import com.panda.domain.Cart;
import com.panda.utils.TimeUtil;
import com.panda.utils.db.DBUtil;

import java.util.List;

public class CartService {

    public static void addCart(int bookId, int userId) throws Exception {
        //判断之前是否有这本书
        List<Cart> carts = DBUtil.read(Cart.class, "*", "user_id = ? and book_id = ?", userId, bookId);

        if (carts == null || carts.isEmpty()) {//如果没有，则添加这本书
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setBookId(bookId);
            cart.setCount(1);
            cart.setCtime(TimeUtil.getNow());
            DBUtil.create(cart);
        } else {//如果有，则增加数量
            Cart cart = carts.get(0);
            cart.setCount(cart.getCount() + 1);
            DBUtil.update(cart);
        }
    }

    public static List<Cart> getCartList(int userId) throws Exception {
        List<Cart> carts = DBUtil.read(Cart.class, "*", "user_id=?", userId);
        return carts;
    }

    public static void removeCart(int userId, int bookId) throws Exception {
        DBUtil.delete(Cart.class, "user_id = ? and book_id = ?", userId, bookId);
    }
}
