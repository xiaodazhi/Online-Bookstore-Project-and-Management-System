package com.panda.service;

import com.panda.domain.Admin;
import com.panda.utils.db.DBUtil;

import java.util.List;

public class AdminService {

    public static boolean login(String account, String password) throws Exception {
        List<Admin> adminList = DBUtil.read(Admin.class, "account=?", account);
        if (adminList == null || adminList.size() <= 0 ) {
            return false;
        }
        Admin admin = adminList.get(0);
        if (admin.getPassword().equals(password)) {
            return true;
        } else {
            return false;
        }
    }

    public static Admin getAdminByAccount(String account) throws Exception {
        List<Admin> adminList = DBUtil.read(Admin.class, "*","account=?", account);
        if (adminList == null || adminList.size() <= 0) {
            return null;
        }
        Admin admin = adminList.get(0);
        return admin;
    }

    public static void register(Admin admin) throws Exception {
        DBUtil.create(admin);
    }
}
