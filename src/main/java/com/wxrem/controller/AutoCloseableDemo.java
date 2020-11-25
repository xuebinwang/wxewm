package com.wxrem.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author wxb
 * 奇妙巧妙关闭流 AutoCloseable
 * @date 2020-09-03 15:28
 */
public class AutoCloseableDemo {
    public static void main(String[] args) {
        try (AutoCloseableObjecct app = new AutoCloseableObjecct()) {
            System.out.println("--执行main方法--");
            System.out.println("--demo2--");
            app.demo2();
        } catch (Exception e) {
            System.out.println("--exception--");
        } finally {
            System.out.println("--finally--");
        }
//结果：
// --执行main方法--
//--demo2--
//--fileInputStream2--
//--close fileInputStream2--
//--finally--
    }

    //自己定义类 并实现AutoCloseable
    public static class AutoCloseableObjecct implements AutoCloseable {
        //其中close()方法是关闭流并且释放与其相关的任何方法，如果流已被关闭，那么调用此方法没有效果。
        @Override
        public void close() throws Exception {
            System.out.println("--close fileInputStream2--");
        }

        public static void demo2() {

            //JDK1.7之前,释放资源方式
//        FileInputStream fileInputStream = null;
//        try {
//            fileInputStream = new FileInputStream("");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                fileInputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

            //1.7之后，只要实现了AutoCloseable接口
            try (FileInputStream fileInputStream2 = new FileInputStream("F:\\server (2).log")) {
                System.out.println("--fileInputStream2--");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


}





