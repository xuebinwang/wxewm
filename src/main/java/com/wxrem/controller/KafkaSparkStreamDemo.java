package com.wxrem.controller;

import kafka.common.TopicAndPartition;
import kafka.message.MessageAndMetadata;
import kafka.serializer.StringDecoder;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.HasOffsetRanges;
import org.apache.spark.streaming.kafka.KafkaCluster;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.apache.spark.streaming.kafka.OffsetRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Predef;
import scala.Tuple2;
import scala.collection.JavaConversions;

import java.util.*;

/**
 * kafka + spark-streaming
 */

public class KafkaSparkStreamDemo {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaSparkStreamDemo.class);

    public static void main(String[] args) {

        String topics = "hello"; // 主题
        long seconds = 10; // 批次时间（单位：秒）

        SparkConf sparkConf = new SparkConf().setAppName("JavaDirectKafkaWordCount")
                //本地调试
                .setMaster("local[4]");
        JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(seconds));

        HashSet<String> topicsSet = new HashSet<>(Arrays.asList(topics.split(",")));
        Map<String, String> kafkaParams = getKafkaParams();
        //earliest 
        //当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费 
        //latest 
        //当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据 
//        kafkaParams.put("kafka.consumer.auto-offset-reset","earliest");
        final String groupId = kafkaParams.get("group.id");
        // 创建kafka管理对象
        final KafkaCluster kafkaCluster = getKafkaCluster(kafkaParams);

        // 初始化offsets
        Map<TopicAndPartition, Long> fromOffsets = fromOffsets(topicsSet, kafkaParams, groupId, kafkaCluster, null);

        // 创建kafkaStream
        JavaInputDStream<String> stream = KafkaUtils.createDirectStream(jssc,
                String.class, String.class, StringDecoder.class,
                StringDecoder.class, String.class, kafkaParams,
                fromOffsets,
                new Function<MessageAndMetadata<String, String>, String>() {
                    /**
                     *
                     */
                    private static final long serialVersionUID = 1L;

                    public String call(MessageAndMetadata<String, String> v1)
                            throws Exception {
                        return v1.message();
                    }
                });

        stream.print();

        stream.foreachRDD(record ->{
            LOG.info("start consumer*****************！");
            List<String> collect = record.collect();
            System.out.println(collect.toString());


        });
        // 存储offsets
        storeConsumerOffsets(groupId, kafkaCluster, stream);

        // Start the computation
        jssc.start();
        try {
            jssc.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> getKafkaParams() {
        Map<String, String> kafkaParams = new HashMap<String, String>();
        kafkaParams.put(ConsumerConfig.GROUP_ID_CONFIG, "tpsc01");
        kafkaParams.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        kafkaParams.put("zookeeper.connect", "127.0.0.1:2181");
        return kafkaParams;
    }

    /**
     * @param groupId      消费者 组id
     * @param kafkaCluster kafka管理对象
     * @param stream       kafkaStreamRdd
     */
    private static <T> void storeConsumerOffsets(final String groupId, final KafkaCluster kafkaCluster, JavaInputDStream<T> stream) {

        long l = System.currentTimeMillis();

        stream.foreachRDD(new VoidFunction<JavaRDD<T>>() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void call(JavaRDD<T> javaRDD) throws Exception {

                // 根据group.id 存储每个partition消费的位置
                OffsetRange[] offsets = ((HasOffsetRanges) javaRDD.rdd()).offsetRanges();
                for (OffsetRange o : offsets) {
                    // 封装topic.partition 与 offset对应关系 java Map
                    TopicAndPartition topicAndPartition = new TopicAndPartition(o.topic(), o.partition());
                    Map<TopicAndPartition, Object> topicAndPartitionObjectMap = new HashMap<>();
                    topicAndPartitionObjectMap.put(topicAndPartition, o.untilOffset());

                    // 转换java map to scala immutable.map
                    scala.collection.immutable.Map<TopicAndPartition, Object> scalaTopicAndPartitionObjectMap =
                            JavaConversions.mapAsScalaMap(topicAndPartitionObjectMap).toMap(new Predef.$less$colon$less<Tuple2<TopicAndPartition, Object>, Tuple2<TopicAndPartition, Object>>() {
                                /**
                                 *
                                 */
                                private static final long serialVersionUID = 1L;

                                public Tuple2<TopicAndPartition, Object> apply(Tuple2<TopicAndPartition, Object> v1) {
                                    return v1;
                                }
                            });

                    // 更新offset到kafkaCluster
                    kafkaCluster.setConsumerOffsets(groupId, scalaTopicAndPartitionObjectMap);
                }
            }
        });

        // 记录处理时间
        LOG.info("storeConsumerOffsets time:" + (System.currentTimeMillis() - l));
    }



    /**
     * 将kafkaParams转换成scala map，用于创建kafkaCluster
     *
     * @param kafkaParams kafka参数配置
     * @return kafkaCluster管理工具类
     */
    private static KafkaCluster getKafkaCluster(Map<String, String> kafkaParams) {
        // 类型转换
        scala.collection.immutable.Map<String, String> immutableKafkaParam = JavaConversions
                .mapAsScalaMap(kafkaParams)
                .toMap(new Predef.$less$colon$less<Tuple2<String, String>, Tuple2<String, String>>() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public Tuple2<String, String> apply(Tuple2<String, String> v1) {
                        return v1;
                    }
                });

        return new KafkaCluster(immutableKafkaParam);
    }

    /**
     * 获取partition信息，并设置各分区的offsets
     *
     * @param topicsSet    所有topic
     * @param kafkaParams  kafka参数配置
     * @param groupId      消费者 组id
     * @param kafkaCluster kafka管理对象
     * @param offset       自定义offset
     * @return offsets
     */
    private static Map<TopicAndPartition, Long> fromOffsets(HashSet<String> topicsSet, Map<String, String> kafkaParams, String groupId, KafkaCluster kafkaCluster, Long offset) {

        long l = System.currentTimeMillis();

        // 所有partition offset
        Map<TopicAndPartition, Long> fromOffsets = new HashMap<>();

        // util.set 转 scala.set
        scala.collection.immutable.Set<String> immutableTopics = JavaConversions
                .asScalaSet(topicsSet)
                .toSet();

        // kafkaCluster获取topic分区信息,这里的topic的分区大于1为right，否则 java.util.NoSuchElementException: Either.right.value on Left
        scala.collection.immutable.Set<TopicAndPartition> scalaTopicAndPartitionSet = kafkaCluster
                .getPartitions(immutableTopics)
                .right()
                .get();

        if (offset != null || kafkaCluster.getConsumerOffsets(kafkaParams.get("group.id"),
                scalaTopicAndPartitionSet).isLeft()) {

            // 等于空则设置为0
            offset = (offset == null ? 0L : offset);

            // 设置每个分区的offset
            scala.collection.Iterator<TopicAndPartition> iterator = scalaTopicAndPartitionSet.iterator();
            while (iterator.hasNext()) {
                fromOffsets.put(iterator.next(), offset);
            }
        } else {
            // 往后继续读取
            scala.collection.Map<TopicAndPartition, Object> consumerOffsets = kafkaCluster
                    .getConsumerOffsets(groupId,
                            scalaTopicAndPartitionSet).right().get();

            scala.collection.Iterator<Tuple2<TopicAndPartition, Object>> iterator = consumerOffsets.iterator();
            while (iterator.hasNext()) {
                Tuple2<TopicAndPartition, Object> next = iterator.next();
                offset = (long) next._2();
                fromOffsets.put(next._1(), offset);
            }
        }

        // 记录处理时间
        LOG.info("fromOffsets time:" + (System.currentTimeMillis() - l));

        return fromOffsets;
    }
}
