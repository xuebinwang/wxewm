package com.design;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author wxb
 * 基于静态内部类实现单例模式，本质依赖类加载、jvm的懒加载（当调用静态内部类instance时才会进行初始化）
 * @date 2020-11-24 11:28
 */
public class InnerSingletonTest {
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
//        InnerSingleton instance = InnerSingleton.getInstance();
//        InnerSingleton instance1 = InnerSingleton.getInstance();
//        System.out.println(instance == instance1); //true

        Constructor<InnerSingleton> constr = InnerSingleton.class.getDeclaredConstructor();
        constr.setAccessible(true);
        InnerSingleton innerSingleton = constr.newInstance();

        InnerSingleton instance = InnerSingleton.getInstance();
        System.out.println(innerSingleton == instance);//false
    }
}

class InnerSingleton{
    //私有静态内部类，当调用静态内部类instance时才会进行初始化
    private static class InnerClassHolder{
        //内部类静态私有属性
        private static InnerSingleton instance = new InnerSingleton();
    }
    private InnerSingleton(){
        if (InnerClassHolder.instance != null){
            throw new RuntimeException("防止反射入侵，单例实例不允许被多个实例化");
        }
    }
    public static InnerSingleton getInstance(){
        return InnerClassHolder.instance;
    }
}
