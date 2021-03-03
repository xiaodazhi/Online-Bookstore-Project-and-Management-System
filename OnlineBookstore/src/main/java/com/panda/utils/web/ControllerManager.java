package com.panda.utils.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ControllerManager {

    private static Map<String, Method> getMap = new HashMap<>();
    private static Map<String, Class> classMap = new HashMap<>();
    private static Map<Class, Object> objectMap = new HashMap<>();

    static {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> urls = classLoader.getResources("");
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                File file = new File(url.getPath());
                if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    for (File temp : files) {
                        analysisPackage(temp, "");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Class loadClass(String className) throws ClassNotFoundException {
        Class clazz = Class.forName(className);
        return clazz;
    }

    private static void analysisPackage(File file, String packageName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (file == null) {
            return;
        }
        if (file.isDirectory()) {//是一个路径
            File[] files = file.listFiles();
            for (File temp : files) {
                analysisPackage(temp, packageName + file.getName() + ".");
            }
        } else {//是一个文件
            if (file.getName().endsWith(".class")) {
                String classFullName = packageName + file.getName();
                classFullName = classFullName.substring(0, classFullName.lastIndexOf("."));
                Class clazz = loadClass(classFullName);
                Controller controller = (Controller) clazz.getAnnotation(Controller.class);
                if (controller != null) {
                    Method[] methods = clazz.getDeclaredMethods();
                    for (Method method : methods) {
                        Path path = method.getAnnotation(Path.class);
                        if (path == null) continue;
                        System.out.println(path.value());
                        getMap.put(path.value(), method);
                        classMap.put(path.value(), clazz);
                        Object obj = objectMap.get(clazz);
                        if (obj == null) {
                            obj = clazz.newInstance();
                            objectMap.put(clazz, obj);
                        }
                    }
                }
            }
        }
    }

    public static void doMethod(HttpServletRequest request, HttpServletResponse response) throws InvocationTargetException, IllegalAccessException {
        Method method = getMap.get(request.getServletPath());
        if (method != null) {
            Class clazz = classMap.get(request.getServletPath());
            Object object = objectMap.get(clazz);
            method.invoke(object, request, response);
        } else {//返回404

        }
    }
}
