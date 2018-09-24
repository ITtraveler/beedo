package com.seagull.beedo.web;

import java.util.ArrayList;
import java.util.List;

public class test {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5000000; i++) {
            list.add("data" + i);
        }

        test1(list);
    }

    private static void test1(List<String> datas) {


        long startTime1 = System.currentTimeMillis();
        for (int i = 0; i < datas.size(); i++) {
            String data = datas.get(i);
        }

        long endTime1 = System.currentTimeMillis();
        System.out.println(endTime1 - startTime1);


        long startTime2 = System.currentTimeMillis();
        for (String data:datas) {
            String data2 = data;
        }
        long endTime2 = System.currentTimeMillis();
        System.out.println(endTime2 - startTime2);

        long startTime3 = System.currentTimeMillis();
        datas.forEach(s -> {
            String data = s;
        });
        long endTime3 = System.currentTimeMillis();
        System.out.println(endTime3 - startTime3);

    }
}
