package com.design;

/**
 * @author wxb
 * 饿汉模式
 * 借助jvm加载时，初始化赋值，初始化
 * @date 2020-11-24 11:20
 */
public class HungarySingletonTest {
    public static void main(String[] args) {
        HungarySiggleton instance = HungarySiggleton.getInstance();
        HungarySiggleton instance1 = HungarySiggleton.getInstance();
        System.out.println(instance == instance1);//true
    }
}
class HungarySiggleton{
    private static HungarySiggleton hungarySiggleton = new HungarySiggleton();;
    private HungarySiggleton(){
        if (hungarySiggleton != null){
            throw new RuntimeException("防止反射入侵，单例实例不允许被多个实例化");
        }
    }
    public static HungarySiggleton getInstance(){
        return hungarySiggleton;
    }
}
