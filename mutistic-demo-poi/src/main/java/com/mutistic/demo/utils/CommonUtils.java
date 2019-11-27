package com.mutistic.demo.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommonUtils {

    public final static String NULL_STR = "null";

    public static boolean isBlank(String data) {
        return data == null || data.trim().length() == 0 || data.toLowerCase().equals(NULL_STR);
    }
    public static boolean isNotBlank(String data) {
        return !isBlank(data);
    }

    public static boolean isEmpty(List data) {
        return data == null || data.size() == 0;
    }

    public static boolean isEmpty(Set data) {
        return data == null || data.size() == 0;
    }

    public static boolean isEmpty(Map data) {
        return data == null || data.size() == 0;
    }

}
