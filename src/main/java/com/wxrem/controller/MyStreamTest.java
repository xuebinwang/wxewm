package com.wxrem.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author wxb
 * @date 2020-08-04 11:15
 */
public class MyStreamTest {

    public static void main(String[] args) {
        ShopProperty p1 = new ShopProperty("叫了个鸡", 1000, 500, 2);
        ShopProperty p2 = new ShopProperty("张三丰饺子馆", 2300, 1500, 3);
        ShopProperty p3 = new ShopProperty("永和大王", 580, 3000, 1);
        ShopProperty p4 = new ShopProperty("肯德基", 6000, 200, 4);
        List<ShopProperty> shopProperties = Arrays.asList(p1,p2,p3,p4);
        System.out.println(shopProperties);
        //筛选距离我最近的店铺
        getCloseShop(shopProperties);

    }

    private static void getCloseShop(List<ShopProperty> shopProperties) {
        String name1 = shopProperties.stream().sorted(Comparator.comparingLong(s -> s.distance))
                .findFirst().get().name;
        System.out.println("距离我最近的店铺是:" + name1);
        Collections.sort(shopProperties, (x,y) -> x.distance.compareTo(y.distance));
        String name2 = shopProperties.get(0).name;
        System.out.println("距离我最近的店铺是:" + name2);

    }


    // 店铺属性
    static class ShopProperty {
        String  name;
        // 距离，单位:米
        Integer distance;
        // 销量，月售
        Integer sales;
        // 价格，这里简单起见就写一个级别代表价格段
        Integer priceLevel;
        public ShopProperty(String name, int distance, int sales, int priceLevel) {
            this.name = name;
            this.distance = distance;
            this.sales = sales;
            this.priceLevel = priceLevel;
        }

        @Override
        public String toString() {
            return "ShopProperty{" +
                    "name='" + name + '\'' +
                    ", distance=" + distance +
                    ", sales=" + sales +
                    ", priceLevel=" + priceLevel +
                    '}';
        }
    }
}
