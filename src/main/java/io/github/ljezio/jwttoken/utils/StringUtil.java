package io.github.ljezio.jwttoken.utils;

public class StringUtil {

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
