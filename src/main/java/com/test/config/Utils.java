package com.test.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class Utils {
    private static Map<String, String> timekey = new ConcurrentHashMap<>();
    private static Map<String, Long> keytime = new ConcurrentHashMap<>();
    private static Map<String,String> token = new ConcurrentHashMap<>();
    	private static final long EXPIRATIONTIME=1000*60*90;//1个半小时
//    private static final long EXPIRATIONTIME = 1000 * 20000;//测试用20秒
    private static final int START = 0;//设置执行开始时间
    private static final int INTERVAL = 10000;//设置间隔执行时间 单位/毫秒
    public static void put(String key, String vale) {
        timekey.put(key, vale);
        keytime.put(key, new Date().getTime());
        System.out.println(timekey);
    }
    public static void putToken(String key, String vale) {
        token.put(key, vale);
        System.out.println(token);
    }
    public static String getToken(String key) {
        return token.get(key);
    }
    public static String get(String key) {
        return timekey.get(key);
    }
    public static void delete(String key) {
        timekey.remove(key);
        keytime.remove(key);
        Iterator<String> timekeys = timekey.keySet().iterator();
        Iterator<String> keytimes = keytime.keySet().iterator();
        while (timekeys.hasNext()) {
            String keys = timekeys.next();
            if (keys.startsWith(key)) {
                timekeys.remove();
            }
        }
        while (keytimes.hasNext()) {
            String keys = keytimes.next();
            if (keys.startsWith(key)) {
                keytimes.remove();
            }
        }
    }
    static {
        Timer tt = new Timer();
        tt.schedule(new TimerTask() {
            @Override
            public void run() {
                long nd = new Date().getTime();
                Iterator<Map.Entry<String, Long>> entries = keytime.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry<String, Object> entry = (Map.Entry) entries.next();
                    String key = (String) entry.getKey();
                    long value = (Long) entry.getValue();
                    long rt = nd - value;//获取当前时间跟存入时间的差值
                    if (rt > EXPIRATIONTIME) {//判断时间是否已经过期  如果过期则清楚key 否则不做处理
                        delete(key);
                        entries.remove();
                        System.out.println("Key:" + key + " 已过期  清空");
                        System.out.println(timekey);
                    }
                }
            }
        }, START, INTERVAL);
    }

    public static <T> T requireNonNull(T obj, String message) {
        if (obj == null || StringUtils.isBlank(obj.toString()))
            throw new NullPointerException(message);
        return obj;
    }



}
