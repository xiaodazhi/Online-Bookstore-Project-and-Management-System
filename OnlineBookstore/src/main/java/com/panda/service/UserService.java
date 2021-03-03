package com.panda.service;

import com.panda.domain.User;
import com.panda.utils.db.DBUtil;

import java.util.List;

public class UserService {

    public static boolean addUser(User user) throws Exception {
        try {
            System.out.println("==================");
            DBUtil.create(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static User getUserByAccount(String account) throws Exception {
        List<User> userList = DBUtil.read(User.class, "*", "account=?", account);
        if (userList == null || userList.size() == 0) {
            return null;
        }
        User user = userList.get(0);
        return user;
    }

    public static User getUserById(int id) throws Exception {
        List<User> userList = DBUtil.read(User.class, "*", "id=?", id);
        if (userList == null || userList.size() == 0) {
            return null;
        }
        User user = userList.get(0);
        return user;
    }


    public static boolean login(String account, String password) throws Exception {
        List<User> userList = DBUtil.read(User.class, "*", "account=?", account);
        if (userList == null || userList.size() <= 0 ) {
            return false;
        }
        User user = userList.get(0);
        if (user.getPassword().equals(password)) {
            return true;
        } else {
            return false;
        }
    }

}
