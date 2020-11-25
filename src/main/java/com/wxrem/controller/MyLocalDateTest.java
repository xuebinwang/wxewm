package com.wxrem.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

/**
 * @author wxb
 * @date 2020-08-25 09:37
 */
public class MyLocalDateTest {
    public static void main(String[] args) {
        test();
    }

    public static void test(){
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate.with(TemporalAdjusters.firstDayOfMonth()));
        LocalDateTime localDateTime = LocalDateTime.now();

        System.out.println(localDateTime);
        System.out.println(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        System.out.println(localDateTime.with(TemporalAdjusters.firstDayOfMonth()));
        System.out.println(localDateTime.with(TemporalAdjusters.firstDayOfMonth()).
                format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        System.out.println(LocalDateTime.parse("2020-11-01T09:40:28.276"). format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));


    }
            }
