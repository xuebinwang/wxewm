package com.design;

public enum EnumSingleton {
    INSTANCE;
    public void prin(){
        System.out.println(this.hashCode());
    }
}

class EnumSingletonTest{
    public static void main(String[] args) {
        EnumSingleton instance = EnumSingleton.INSTANCE;
        EnumSingleton instance1 = EnumSingleton.INSTANCE;
        System.out.println(instance == instance1);
        instance.prin();
        instance1.prin();
        //true
        //1395089624
        //1395089624
        System.out.println(instance.getDeclaringClass().getName());
        System.out.println(instance1.getDeclaringClass().getName());
        //com.design.EnumSingleton
        //com.design.EnumSingleton
    }
}
