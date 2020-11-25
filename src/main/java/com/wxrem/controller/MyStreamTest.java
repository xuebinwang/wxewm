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
        ShopProperty p0 = new ShopProperty("叫了个鸡", 1000, 500, 2);
        ShopProperty p1 = new ShopProperty("叫了个鸡2", 1002, 100, 2);
        ShopProperty p2 = new ShopProperty("张三丰饺子馆", 2300, 1500, 3);
        ShopProperty p3 = new ShopProperty("永和大王", 580, 3000, 1);
        ShopProperty p4 = new ShopProperty("肯德基", 6000, 200, 4);
        List<ShopProperty> shopProperties = Arrays.asList(p0,p1,p2,p3,p4);
//        System.out.println(shopProperties);
        //筛选距离我最近的店铺
//        getCloseShop(shopProperties);

//        getMapShop(shopProperties);
//        getFlatMap();
//        getMaxPriceLevel(shopProperties);
//        getShopNameAndPriceLevel(shopProperties);
        getShopGroupByPriceLevel(shopProperties);

    }
    //参数和返回值不必属于同一种类型，但是lambda表达式必须是Function接口的一个实例
    private static void getMapShop(List<ShopProperty> shopProperties) {
        Stream<Integer> integerStream = shopProperties.stream().map(shopProperty -> shopProperty.distance);
        System.out.println(integerStream.collect(Collectors.toList()));

        Stream<ShopProperty> shopPropertyStream = shopProperties.stream().map(shopProperty -> {
            Integer distance = shopProperty.distance;
            String name = shopProperty.name;
            ShopProperty s = new ShopProperty(name ,distance ,0,0);
            return s;
        });
        System.out.println(shopPropertyStream.collect(Collectors.toList()));

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
    //为了让Stream对象按照价格等级进行排序，需要传给它一个Comparator对象。
    //Java8提供了一个新的静态方法comparingInt，使用它可以方便地实现一个比较器。
    //放在以前，我们需要比较两个对象的某项属性的值，现在只需要提供一个存取方法就够了
    public static void getMaxPriceLevel(List<ShopProperty> shopProperties){
        Optional<ShopProperty> max = shopProperties.stream().max(Comparator.comparingInt(s -> s.priceLevel));
        System.out.println(max.get());
    }
    public static void getShopNameAndPriceLevel(List<ShopProperty> shopProperties){
        Map<String, Integer> shopNameAndPriceLevel = shopProperties.stream().
                collect(Collectors.toMap(ShopProperty::getName, ShopProperty::getPriceLevel));
        System.out.println(shopNameAndPriceLevel);
    }
    //所有价格等级的店铺：类似group by
    public static void getShopGroupByPriceLevel(List<ShopProperty> shopProperties){
        Map<Integer, List<ShopProperty>> getShopGroupByPriceLevel = shopProperties.stream().
                collect(Collectors.groupingBy(ShopProperty::getPriceLevel));
        System.out.println(getShopGroupByPriceLevel);

    }
    //Stream并行流
    //流使得计算变得容易，它的操作也非常简单，但你需要遵守一些约定。默认情况下我们使用集合的stream方法
    //创建的是一个串行流，你有两种办法让他变成并行流。
    //
    //调用Stream对象的parallel方法
    //创建流的时候调用parallelStream而不是stream方法
    //我们来用具体的例子来解释串行和并行流


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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getDistance() {
            return distance;
        }

        public void setDistance(Integer distance) {
            this.distance = distance;
        }

        public Integer getSales() {
            return sales;
        }

        public void setSales(Integer sales) {
            this.sales = sales;
        }

        public Integer getPriceLevel() {
            return priceLevel;
        }

        public void setPriceLevel(Integer priceLevel) {
            this.priceLevel = priceLevel;
        }
    }
}
