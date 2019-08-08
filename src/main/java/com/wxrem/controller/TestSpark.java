package com.wxrem.controller;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;

/**
 * spark-java 本地文件读取和写出
 *
 * 1.本地配置java、scala、spark+hadoop环境寄变量
 *      <scala.version>2.11.8</scala.version>
 *      <spark.version>2.4.3</spark.version>
 *      <hadoop.version>3.0.0</hadoop.version>
 *
 */

public class TestSpark {
    public static void main(String[] args) {
        System.out.println("dasadasda");
        System.out.println(System.getenv("HADOOP_USER_NAME"));
        System.out.println(System.getenv("HADOOP_HOME"));
        SparkConf conf = new SparkConf().setAppName("WortCount").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> fileRDD = sc.textFile("D:\\test.txt");
        fileRDD.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String s) throws Exception {
                return Arrays.asList(s.split(",")).iterator();
            }
        });
        JavaRDD<String> wordRdd = fileRDD.flatMap(line -> Arrays.asList(line.split(",")).iterator());
        JavaPairRDD<String, Integer> wordOneRDD = wordRdd.mapToPair(word -> new Tuple2<>(word, 1));
        JavaPairRDD<String, Integer> wordCountRDD = wordOneRDD.reduceByKey((x, y) -> x + y);
        JavaPairRDD<Integer, String> count2WordRDD = wordCountRDD.mapToPair(tuple -> new Tuple2<>(tuple._2, tuple._1));
        JavaPairRDD<Integer, String> sortRDD = count2WordRDD.sortByKey(false);
        JavaPairRDD<String, Integer> resultRDD = sortRDD.mapToPair(tuple -> new Tuple2<>(tuple._2, tuple._1));
        resultRDD.saveAsTextFile("D:\\result2");

//        resultRDD.collect();

    }
}
