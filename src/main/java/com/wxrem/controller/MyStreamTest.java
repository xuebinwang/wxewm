package com.wxrem.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wxb
 * 操作的是一个数组可以使用Stream.of(1, 2, 3)方法将它转换为一个流。
 * @date 2020-08-04 11:15
 */
public class MyStreamTest {

    public static void main(String[] args) {
        ShopProperty p1 = new ShopProperty("叫了个鸡", 1000, 500, 2);
        ShopProperty p2 = new ShopProperty("张三丰饺子馆", 2300, 1500, 3);
        ShopProperty p3 = new ShopProperty("永和大王", 580, 3000, 1);
        ShopProperty p4 = new ShopProperty("肯德基", 6000, 200, 4);
        List<ShopProperty> shopProperties = Arrays.asList(p1,p2,p3,p4);
//        System.out.println(shopProperties);
        //筛选距离我最近的店铺
//        getCloseShop(shopProperties);

//        getMapShop(shopProperties);
        getFlatMap();

    }
    //参数和返回值不必属于同一种类型，但是lambda表达式必须是Function接口的一个实例
    private static void getMapShop(List<ShopProperty> shopProperties) {
        Stream<Integer> integerStream = shopProperties.stream().map(shopProperty -> shopProperty.distance);
        System.out.println(integerStream.collect(Collectors.toList()));

    }
    //提取子流的操作，这种情况用的不多但是遇到flatMap将变得更容易处理。
    private static void getFlatMap() {
        List<List<String>> lists = new ArrayList<>();
        lists.add(Arrays.asList("apple", "click"));
        lists.add(Arrays.asList("boss", "dig", "qq", "vivo"));
        lists.add(Arrays.asList("c#", "biezhi"));
        List<String> collect = lists.stream().flatMap(Collection::stream)
                .filter(str -> str.length() == 2)
                .collect(Collectors.toList());
        System.out.println(collect);
    }
    private static void getFlatMap2() {
        String[] words = new String[]{"Hello", "World"};
        List<String> a = Arrays.stream(words)
                .map(word -> word.split(""))
                .flatMap(Arrays::stream)
                .distinct()
                .collect(Collectors.toList());
        a.forEach(System.out::print);
    }
    private static void getCloseShop(List<ShopProperty> shopProperties) {
        String name1 = shopProperties.stream().sorted(Comparator.comparingLong(s -> s.distance))
                .findFirst().get().name;
        System.out.println("距离我最近的店铺是:" + name1);
        Collections.sort(shopProperties, (x,y) -> x.distance.compareTo(y.distance));
        String name2 = shopProperties.get(0).name;
        System.out.println("距离我最近的店铺是:" + name2);

    }
    // jdk7 提供方法 Files.readAllLines... 返回List<String>
    public static void getFileToList() {
        try {
            String readAllLines = Files.readAllLines(Paths.get("E:\\workspace\\github\\AllLinesFile.txt"))
                    .stream().collect(Collectors.joining("\n"));
            System.out.println(readAllLines.toString());
        }catch (IOException e){
            e.printStackTrace();
        }

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
