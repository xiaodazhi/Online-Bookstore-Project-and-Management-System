package com.panda.utils;

public class TimeUtil {

    public static int getNow() {
        return (int) (System.currentTimeMillis() / 1000);
    }
}
