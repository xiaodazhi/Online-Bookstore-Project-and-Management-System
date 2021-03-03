package com.panda.utils.db;

import java.lang.reflect.Field;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class DBUtil {
    /*
    * 1. 哪些Domain对应了哪些表
    * 2. 要知道对象中哪些属性对应表中的哪些字段
    * */

    public static final String URL = "jdbc:mysql://127.0.0.1:3306/panda?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=utf-8";
    public static final String USER = "root";
    public static final String PASSWORD = "root";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        return conn;
    }

    private static <T> List<T> getObject(Class<T> clazz, ResultSet resultSet) throws SQLException, IllegalAccessException, InstantiationException {
        List<T> result = new ArrayList<>();
        while (resultSet.next()) {//每轮循环得到一个对象
            T obj = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0 ; i < fields.length ; i ++) {//遍历对象的所有字段
                fields[i].setAccessible(true);
                Column column = fields[i].getAnnotation(Column.class);
                if (column == null) continue;
                Object value = resultSet.getObject(column.value());//得到这个字段的值
                fields[i].set(obj, value);//将值设置到obj对象中
            }
            result.add(obj);
        }
        return result;
    }

    public static int create(Object object) throws Exception {
        Table table = object.getClass().getAnnotation(Table.class);
        if (table == null) throw new Exception("没有table注解");
        String tableName = table.value();
        StringBuilder columns = new StringBuilder("");
        StringBuilder values = new StringBuilder("");
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Column column = field.getAnnotation(Column.class);
            if (column == null) continue;
            ID id = field.getAnnotation(ID.class);
            if (id != null) continue;
            String columnName = column.value();
            Object value = field.get(object);
            columns.append("`" + columnName + "`,");
            if (value.getClass() == String.class) {
                values.append("'" + value + "',");
            } else {
                values.append(value + ",");
            }

        }
        String sql = "insert into `" + tableName + "` (" + columns.toString().substring(0, columns.length() - 1) + ") values (" + values.toString().substring(0, values.length() - 1) + ");";
        System.out.println(sql);
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.execute();

        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        int generateKey = -1;
        while (generatedKeys.next()) {
            generateKey = generatedKeys.getInt(1);
        }
        return generateKey;
    }

    public static int readCount(Class clazz, String where, Object... params) throws Exception {
        Table table = (Table) clazz.getAnnotation(Table.class);
        if (table == null) throw new Exception("没有table注解");
        String tableName = table.value();
        String sql = "select count(1) as count from `" + tableName + "`";
        if (where == null || "".equals(where)) {
            sql += ";";
        } else {
            sql += " where " + where + ";";
        }
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 0 ; params != null && i < params.length ; i ++) {
            if (params[i].getClass() == Integer.class || params[i].getClass() == int.class) {
                preparedStatement.setInt(i + 1, (int) params[i]);
            } else if (params[i].getClass() == Long.class || params[i].getClass() == long.class) {
                preparedStatement.setLong(i + 1, (long) params[i]);
            } else if (params[i].getClass() == Double.class || params[i].getClass() == double.class) {
                preparedStatement.setDouble(i + 1, (double) params[i]);
            } else if (params[i].getClass() == Float.class || params[i].getClass() == float.class) {
                preparedStatement.setFloat(i + 1, (float) params[i]);
            } else if (params[i].getClass() == String.class) {
                preparedStatement.setString(i + 1, (String) params[i]);
            } else if (params[i].getClass() == Date.class) {
                preparedStatement.setDate(i + 1, (Date) params[i]);
            }
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("count");
        }
        return 0;
    }
    public static <T> List<T> readAll(Class<T> clazz) throws Exception {
        return read(clazz, "*", null, null, null);
    }
    public static <T> List<T> read(Class<T> clazz, String where, Object... params) throws Exception {
        return read(clazz, "*", where, params);
    }
    public static <T> List<T> read(Class<T> clazz, String select, String where, Object... params) throws Exception {
        return read(clazz, select, where, null, null, params);
    }
    public static <T> List<T> read(Class<T> clazz, String select, String where, String orderBy, String limit, Object... params) throws Exception {
        Table table = clazz.getAnnotation(Table.class);
        if (table == null) throw new Exception("没有table注解");
        String tableName = table.value();
        StringBuilder sql = new StringBuilder("select " + select + " from `" + tableName + "`");
        if (where != null && !"".equals(where)) {
            sql.append(" where " + where);
        }
        if (orderBy != null && !"".equals(orderBy)) {
            sql.append(" order by " + orderBy);
        }
        if (limit != null && !"".equals(limit)) {
            sql.append(" limit " + limit);
        }
        sql.append(";");
        System.out.println(sql.toString());
        return readBySql(clazz, sql.toString(), params);
    }
    public static <T> List<T> readBySql(Class<T> clazz, String sql, Object... params) throws SQLException, InstantiationException, IllegalAccessException {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 0 ; params != null && i < params.length ; i ++) {
            if (params[i].getClass() == Integer.class || params[i].getClass() == int.class) {
                preparedStatement.setInt(i + 1, (int) params[i]);
            } else if (params[i].getClass() == Long.class || params[i].getClass() == long.class) {
                preparedStatement.setLong(i + 1, (long) params[i]);
            } else if (params[i].getClass() == Double.class || params[i].getClass() == double.class) {
                preparedStatement.setDouble(i + 1, (double) params[i]);
            } else if (params[i].getClass() == Float.class || params[i].getClass() == float.class) {
                preparedStatement.setFloat(i + 1, (float) params[i]);
            } else if (params[i].getClass() == String.class) {
                preparedStatement.setString(i + 1, (String) params[i]);
            } else if (params[i].getClass() == Date.class) {
                preparedStatement.setDate(i + 1, (Date) params[i]);
            }
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        return getObject(clazz, resultSet);
    }

    public static <T> void update(T obj) throws Exception {
        String primkey = "";
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            ID id = field.getAnnotation(ID.class);
            if (id != null) {
                Column column = field.getAnnotation(Column.class);
                primkey = column.value() + "=" + field.get(obj);
            }
        }
        if (primkey != null && !"".equals(primkey)) {
            update(obj, primkey);
        } else {
            throw new Exception("主键错误，无法执行update");
        }
    }
    public static <T> void update(T obj, String where) throws Exception {
        Table table = obj.getClass().getAnnotation(Table.class);
        if (table == null) throw new Exception("没有table注解");
        String tableName = table.value();
        StringBuilder sql = new StringBuilder("update `" + tableName + "` set ");
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Column column = field.getAnnotation(Column.class);
            if (column == null) continue;
            ID id = field.getAnnotation(ID.class);
            if (id != null) continue;
            sql.append(column.value() + " = ");
            if (field.getType() == String.class) {
                sql.append("'" + field.get(obj) + "',");
            } else {
                sql.append(field.get(obj) + ",");
            }
        }
        String realSql = sql.substring(0, sql.length() - 1);
        if (where != null) {
            realSql += " where " + where;
        }
        realSql += ";";
        System.out.println(realSql);
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(realSql);
        preparedStatement.execute();
    }
    public static <T> void update(Class<T> clazz, String[] keys, Object[] values, String where) throws Exception {
        update(clazz, keys, values, where, null);
    }
    public static <T> void update(Class<T> clazz, String[] keys, Object[] values, String where, Object... params) throws Exception {
        Table table = clazz.getAnnotation(Table.class);
        if (table == null) throw new Exception("没有table注解");
        String tableName = table.value();
        StringBuilder sql = new StringBuilder("update `" + tableName + "` set ");
        for (int i = 0 ; i < keys.length ; i ++) {
            sql.append(keys[i] + "=");
            if (values[i].getClass() == String.class) {
                sql.append("'" + values[i] + "',");
            } else {
                sql.append(values[i] + ",");
            }
        }
        String realSql = sql.substring(0, sql.length() - 1);
        if (where != null && !"".equals(where)) {
            realSql += " where " + where;
        }
        realSql += ";";
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(realSql);
        for (int i = 0 ; params != null && i < params.length ; i ++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        System.out.println(realSql);
        preparedStatement.execute();
    }

    public static <T> void delete(T obj) throws Exception {
        String primkey = "";
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            ID id = field.getAnnotation(ID.class);
            if (id != null) {
                Column column = field.getAnnotation(Column.class);
                primkey = column.value() + "=" + field.get(obj);
            }
        }
        if (primkey != null && !"".equals(primkey)) {
            delete(obj.getClass(), primkey);
        } else {
            throw new Exception("主键错误，无法执行update");
        }
    }
    public static <T> void delete(Class<T> clazz, String where) throws Exception {
       delete(clazz, where, null);
    }
    public static <T> void delete(Class<T> clazz, String where, Object... params) throws Exception {
        Table table = clazz.getAnnotation(Table.class);
        if (table == null) throw new Exception("没有table注解");
        String tableName = table.value();
        StringBuilder sql = new StringBuilder("delete from `" + tableName + "` ");
        if (where != null) {
            sql.append(" where " + where);
        }
        sql.append(";");
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
        for (int i = 0 ; params != null && i < params.length ; i ++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        preparedStatement.execute();
    }

    public static void main(String[] args) throws Exception {

//        List<Admin> adminList = DBUtil.read(Admin.class, "account='panda'");
//        Admin admin = adminList.get(0);
//        System.out.println(admin);
//
//        admin.setPassword("666666");
//
//        DBUtil.update(admin);

//        DBUtil.update(Admin.class, new String[]{"password"}, new Object[]{"121212"}, "account=?", "panda");
//
//        List<Admin> adminList = DBUtil.read(Admin.class, "account='panda'", null);
//        Admin admin = adminList.get(0);
//        System.out.println(admin);

//        DBUtil.delete(Admin.class, "account=?", "dog");
//
//        Admin admin = new Admin();
//        admin.setAccount("dog");
//        admin.setPassword("111222");
//
//        System.out.println(DBUtil.create(admin));

//        DBUtil.delete(Admin.class, "account=?", "dog");
//
//        List<Admin> adminList = DBUtil.readAll(Admin.class);
//
//        for (Admin temp : adminList) {
//            System.out.println(temp);
//        }

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> urls = classLoader.getResources("");
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            System.out.println(url);
        }
    }
}
