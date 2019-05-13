package com.test.config;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class test {
    public static void main(String[] args) throws ParseException {

//        LocalDateTime now = LocalDateTime.of(2019,05, 11, 00, 00, 01);
//        LocalDateTime startDate = LocalDateTime.of(2019,05, 01, 00, 00, 00);
//        LocalDateTime endDate = LocalDateTime.of(2019,05, 10, 23, 59, 59);
//        if (now.isAfter(startDate) && now.isBefore(endDate)) {
//            System.out.println(now);
//            System.out.println(startDate);
//            System.out.println(endDate);
//            System.out.println(11111);
//        }else{
//            System.out.println(now);
//            System.out.println(startDate);
//            System.out.println(endDate);
//            System.out.println(22222);
//        }

        Set<String> s = new HashSet<>();
        s.add("11111");
        s.add("222");
        s.add("333");
        s.add("444");
        s.stream().forEach(s1 -> {

        });

        System.out.println(s.toString());
    }
}
