package com.wxrem.controller;


import javax.swing.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * java 8 新特性 lambda test
 * name	                        type	        description
 * Consumer	                    Consumer< T >	接收T对象，不返回值
 * Predicate	                Predicate< T >	接收T对象并返回boolean
 * Function	                    Function< T, R >	接收T对象，返回R对象
 * Supplier	                    Supplier< T >	提供T对象（例如工厂），不接收值
 * UnaryOperator	            UnaryOperator	接收T对象，返回T对象
 * BinaryOperator	            BinaryOperator	接收两个T对象，返回T对象
 *
 */
public class MyLambdaTest {

    public static void main(String[] args) {
        List<Integer>  list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        System.out.println(list);

        AnonymousFunctionUse(list);

        funcationTest();



    }
    /*
    java 8 的lambda处理匿名函数
     */
    static void AnonymousFunctionUse(List<Integer> integerList){
        // 转Stream
        Stream<Integer> stringStream = integerList.stream().filter(e ->  e.equals(4) || e.equals(3));

        // java 8 的lambda处理匿名函数
//        stringStream.forEach((sa)-> System.out.println(sa)); //3,4


        // java 8 的 Function函数中的 BinaryOperator<T>
        BinaryOperator<Integer> sum = BinaryOperatorUse();
        System.out.println(stringStream.reduce(sum).get());
        //等同于这里
//        System.out.println(stringStream.reduce(new BinaryOperator<Integer>() {
//            @Override
//            public Integer apply(Integer s, Integer s2) {
//                return s+s2;
//            }
//        }).get());


        //Stream 转为数组1
//        ArrayList<Integer> res1 = (ArrayList<Integer>) stringStream.collect(Collectors.toList());
//        System.out.println(res1);
        //Stream 转为数组2 ，这个可以加多个不同的数组
//        ArrayList<Integer> list2 = new ArrayList<Integer>();
//        list2.add(100);
//        ArrayList<Integer> res = stringStream.collect(() ->
//                        list2,
//                        (list, item) -> list.add(item),
//                        (list, li) -> list.addAll(li));
//        System.out.println(res);

    }
    /**
     *  java 8 的lambda基础语法 expression = (variable) -> action
     *  java Function函数中的 BinaryOperator<T>接口 用于执行lambda表达式并返回一个T类型的返回值
     */
    static BinaryOperator<Integer> BinaryOperatorUse(){
        //自定义函数
        BinaryOperator<Integer> sum = (a, b) -> a+b;
//        System.out.println(sum.apply(3, 4));
        // 自带返回大小方法 maxBy(Comparator<? super T> comparator) 、<T> BinaryOperator<T> minBy(Comparator<? super T> comparator)
        BinaryOperator<Integer> minByTest = BinaryOperator.minBy(Comparator.naturalOrder());
        System.out.println(minByTest.apply(3,4));// 3
        BinaryOperator<Integer> maxByTest = BinaryOperator.maxBy(Comparator.naturalOrder());
        System.out.println(maxByTest.apply(3,4));// 4

        return sum;
    }

    /**
     * Function< T, R >	接收T对象，返回R对象
     */
    static void funcationTest(){
        Function<Integer, Integer> name = e -> e * 2;
        Function<Integer, Integer> square = e -> e * e;
        //返回一个先执行当前函数对象('name.andThen(square)') 的 apply 方法,再执行 after 函数对象 apply 方法的函数对象。
        //先执行外面的，再执行里面的
        int value = name.andThen(square).apply(3);
        System.out.println("andThen value=" + value);

        //返回一个先执行before函数对象apply方法再执行当前函数对象apply方法的函数对象
        //先执行里面的，再执行外面的
        int value2 = name.compose(square).apply(3);
        System.out.println("compose value2=" + value2);

        //返回一个执行了apply()方法之后只会返回输入参数的函数对象
        Object identity = Function.identity().apply("huohuo");
        System.out.println(identity);
    }

    /**
     * 断言型接口示例
     */
    static void PredicateTest(){
        Predicate<Integer> predicate = a -> a>10;
        System.out.println(predicate.test(12));
    }

    /**
     * 函数式接口 test
     */
     static void funcationFourTest() {
        donation(1000, money -> System.out.println("好心的麦乐迪为Blade捐赠了"+money+"元")) ;

        List<Integer> list = supply(10,() -> (int)(Math.random()*100));
        list.forEach(System.out::println);

        Integer value = convert("28", x -> Integer.parseInt(x));

        List<String> fruit = Arrays.asList("香蕉", "哈密瓜", "榴莲", "火龙果", "水蜜桃");
        List<String> newFruit = filter(fruit, (f) -> f.length() == 2);
        System.out.println(newFruit);
    }
    //消费型接口示例
    public static void donation(Integer money, Consumer<Integer> consumer){
        consumer.accept(money);
    }

    //供给型接口示例
    public static List<Integer> supply(Integer num, Supplier<Integer> supplier){
        List<Integer> resultList = new ArrayList<Integer>()   ;
        for(int x=0;x<num;x++)
            resultList.add(supplier.get());
        return resultList ;
    }
    //函数型接口示例: 转换字符串为Integer
    public static Integer convert(String str, Function<String, Integer> function) {
        return function.apply(str);
    }
    //断言型接口示例 筛选出只有2个字的水果
    public static List<String> filter(List<String> fruit, Predicate<String> predicate){
        List<String> f = new ArrayList<>();
        for (String s : fruit) {
            if(predicate.test(s)){
                f.add(s);
            }
        }
        return f;
    }




}
