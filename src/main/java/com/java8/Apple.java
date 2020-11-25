package com.java8;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/**
 * @author wxb
 * @date 2020-11-24 17:35
 */
public class Apple {
    private Integer id;
    private String name;
    private BigDecimal money;
    private Integer num;
    public Apple(Integer id, String name, BigDecimal money, Integer num) {
        this.id = id;
        this.name = name;
        this.money = money;
        this.num = num;
    }

    public static void main(String[] args) {
        List<Apple> appleList = new ArrayList<>();//存放apple对象集合

        Apple apple1 =  new Apple(1,"苹果1",new BigDecimal("3.25"),10);
        Apple apple12 = new Apple(1,"苹果2",new BigDecimal("1.35"),20);
        Apple apple2 =  new Apple(2,"香蕉",new BigDecimal("2.89"),30);
        Apple apple3 =  new Apple(3,"荔枝",new BigDecimal("9.99"),40);

        appleList.add(apple1);
        appleList.add(apple12);
        appleList.add(apple2);
        appleList.add(apple3);

        //分组
        Map<Integer, List<Apple>> collect = appleList.stream().collect(Collectors.groupingBy(Apple::getId));
        System.out.println(collect);
        //{1=[Apple{id=1, name='苹果1', money=3.25, num=10}, Apple{id=1, name='苹果2', money=1.35, num=20}], 2=[Apple{id=2, name='香蕉', money=2.89, num=30}], 3=[Apple{id=3, name='荔枝', money=9.99, num=40}]}

        //List转Map
        /**
         * List -> Map
         * 需要注意的是：
         * toMap 如果集合对象有重复的key，会报错Duplicate key ....
         *  apple1,apple12的id都为1。
         *  可以用 (k1,k2)->k1 来设置，如果有重复的key,则保留key1,舍弃key2
         */
        Map<Integer, Apple> collect1 = appleList.stream().collect(Collectors.toMap(Apple::getId, a -> a, (k1, k2) -> k1));
        System.out.println(collect1);

        //过滤出符合条件的数据
        List<Apple> filterList = appleList.stream().filter(a -> a.getName().equals("香蕉")).collect(Collectors.toList());

        System.err.println("filterList:"+filterList);
        //[Apple{id=2, name='香蕉', money=2.89, num=30}]

        //计算求和 总金额
        BigDecimal totalMoney = appleList.stream().map(Apple::getMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
        System.err.println("totalMoney:"+totalMoney);  //totalMoney:17.48

        //查找流中最大 最小值
        Optional<Apple> maxDish = appleList.stream().
                collect(Collectors.maxBy(Comparator.comparing(Apple::getMoney)));
        maxDish.ifPresent(System.out::println);

        Optional<Apple> minDish = appleList.stream().
                collect(Collectors.minBy(Comparator.comparing(Apple::getMoney)));
        minDish.ifPresent(System.out::println);

        //去重
        // 根据id去重
        List<Apple> unique = appleList.stream().collect(
                collectingAndThen(
                        toCollection(
                                () -> new TreeSet<Apple>(
                                        Comparator.comparingInt(Apple::getNum))), ArrayList::new));
        System.out.println(unique);
        //[Apple{id=1, name='苹果1', money=3.25, num=10}, Apple{id=2, name='香蕉', money=2.89, num=30}, Apple{id=3, name='荔枝', money=9.99, num=40}]
        //[Apple{id=1, name='苹果1', money=3.25, num=10}, Apple{id=1, name='苹果2', money=1.35, num=20}, Apple{id=2, name='香蕉', money=2.89, num=30}, Apple{id=3, name='荔枝', money=9.99, num=40}]

    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "Apple{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", money=" + money +
                ", num=" + num +
                '}';
    }
}
