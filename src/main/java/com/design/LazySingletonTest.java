package com.design;

/**
 * @author wxb
 * 单例模式之懒汉模式
 * @date 2020-11-24 10:46
 */
public class LazySingletonTest {
    public static void main(String[] args) {

//        LazySingleton l1 = LazySingleton.getInstance();
//        LazySingleton l2 = LazySingleton.getInstance();
//        System.out.println(l1 == l2); //true

        new Thread(() -> {
            LazySingleton l1 = LazySingleton.getInstance();
            System.out.println(l1);
        }).start();
        new Thread(() -> {
            LazySingleton l2 = LazySingleton.getInstance();
            System.out.println(l2);
        }).start();
        //com.design.LazySingleton@20917dd4
        //com.design.LazySingleton@8124ccf

    }
}

class LazySingleton{
    //volatile 防止指令重排
    private volatile static LazySingleton lazySingleton;
    private LazySingleton(){
    }
    public static LazySingleton getInstance(){
        if (lazySingleton == null){
            synchronized (LazySingleton.class) {
                if (lazySingleton == null) {
                    lazySingleton = new LazySingleton();
                    //字节码层，编译器（JIT）,CPU
                    //1、分配空间
                    //2、初始化
                    //3、引用赋值
                    //不加 volatile 时，2和3是没有先后顺序的，当多线程进入时先3引用赋值不为null，2没有初始化可能会有空指针，所以需要加volatile
                    //在new LazySingleton时，volatile 防止对象指令重排，按1/2/3执行
                }
            }
        }
        return lazySingleton;
    }
}
