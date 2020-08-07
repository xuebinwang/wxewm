package com.wxrem.controller;

/**
 * @author wxb
 * 自定义断言Predicate2接口，只能有一个注释@FunctionalInterface方法或者只有一个方法
 * @date 2020-07-29 11:42
 */
public class FunctionInterfaceDemo {
    @FunctionalInterface
    interface Predicate2<T> {
        boolean test(T t);
    }

    /**
     * 执行Predicate判断
     *
     * @param age       年龄
     * @param predicate Predicate函数式接口
     * @return          返回布尔类型结果
     */
    public static boolean doPredicate(int age, Predicate2<Integer> predicate) {
        return predicate.test(age);
    }

    public static void main(String[] args) {
        boolean isAdult = doPredicate(20, x -> x >= 18);
        System.out.println(isAdult);
    }

}
