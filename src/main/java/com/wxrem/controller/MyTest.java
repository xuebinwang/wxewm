package com.wxrem.controller;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyTest{

    public static void main(String[] args) {
        //JOptionPane.showConfirmDialog(null,"请求控制","我亲爱的宝贝专属",JOptionPane.YES_NO_OPTION);

        List<String>  stringList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            stringList.add(String.valueOf(i));
        }
        System.out.println(stringList);

        Stream<String> stringStream = stringList.stream().filter(e ->  e.equals("4") || e.equals("3"));
        System.out.println(stringList);

//        System.out.println(stringStream.reduce(new BinaryOperator<String>() {
//            @Override
//            public String apply(String s, String s2) {
//                return s+s2;
//            }
//        }).get());

        ArrayList<String> res = stringStream.collect(() -> new ArrayList<String>(),
                (list, item) -> list.add(item),
                (list, list2) -> list.addAll(list2));
        System.out.println(res);


    }



}
