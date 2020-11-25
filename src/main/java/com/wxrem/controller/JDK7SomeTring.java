package com.wxrem.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

/**
 * @author wxb
 * @date 2020-09-03 15:02
 */
public class JDK7SomeTring implements AutoCloseable {
    private static final Logger log = LogManager.getLogger(JDK7SomeTring.class);

    public static void main(String[] args) throws Exception {
        tryWithResources();
    }
    public static void tryWithResources()  throws Exception{
        log.info("资源input.......");
        try( InputStream ins = new FileInputStream("C:\\Users\\my\\Desktop\\user-config.xml") ){
            char charStr = (char) ins.read();
            System.out.println(charStr);
            log.info("资源ok.......");
        }
    }

    @Override
    public void close() throws IOException {
        log.info("资源正字啊关闭中.......");
    }
}
