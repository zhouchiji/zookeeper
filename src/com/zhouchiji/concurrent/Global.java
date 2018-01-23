package com.zhouchiji.concurrent;

import java.util.HashMap;
import java.util.Map;

public class Global {
    public static ThreadLocal transfer = new ThreadLocal();

    public static Map<String, Object> getTransfer() {
        Map<String, Object> map = (Map<String, Object>) transfer.get();
        if (map == null) {
            map = new HashMap<String, Object>();
            transfer.set(map);
        }
        return map;
    }

    public static void setTransfer(String key, Object value) {
        Map<String, Object> map = getTransfer();
        map.put(key, value);
        transfer.set(map);
    }
}
