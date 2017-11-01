package com.zhouchiji.test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhouchiji
 */
public class ThreadLocalTest {
    private static ThreadLocal threadLocal = new ThreadLocal();

    public static void main(String[] args) {
        setMany();
        Map<String, Object> map = (Map<String, Object>) threadLocal.get();
        System.out.println(map);
    }

    public static void setValue(String key, Object value) {
        Map<String, Object> map = (Map<String, Object>) threadLocal.get();
        if (map == null) {
            map = new HashMap<>();
        }
        map.put(key, value);
        threadLocal.set(map);
    }

    public static void setMany() {
        setValue("user", 1);
        setValue("sex", 2);
        setValue("age", 3);
    }
}
