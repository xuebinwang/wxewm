package com.wxrem.common.http;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author wxb
 * 在线统计
 * @date 2020-08-11 15:03
 */
public class Monitor {

    public static class MonitorKey{
        public MonitorKey(String key, String desc){
            this.key = key;
            this.desc = desc;
        }
        String key;
        String desc;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MonitorKey that = (MonitorKey) o;
            return Objects.equals(key, that.key) &&
                    Objects.equals(desc, that.desc);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, desc);
        }
    }
    public static class MonitorValue{
        AtomicInteger count = new AtomicInteger();
        float avgTile;
        AtomicLong totalTime = new AtomicLong();
    }

    //原子的线程安全
    Map<MonitorKey, MonitorValue> keyMonitorValueMap = new ConcurrentHashMap<>();

    /**
     * 接口调用方法
     * @param url url
     * @param desc
     * @param timeCost 响应时长
     */
    public void visit(String url, String desc, long timeCost){
        MonitorKey monitorKey = new MonitorKey(url, desc);
        // 1
        MonitorValue monitorValue = keyMonitorValueMap.get(monitorKey);
        if (monitorValue == null){
            monitorValue = new MonitorValue();
            keyMonitorValueMap.put(monitorKey, monitorValue);
        }
        // 2
        //putIfAbsent(K key, V value)
        //如果指定的键尚未与值相关联，请将其与给定值相关联。
        keyMonitorValueMap.putIfAbsent(monitorKey, new MonitorValue());
        monitorValue = keyMonitorValueMap.get(monitorKey);

        //原子上增加一个当前值。
        //结果
        //以前的值
        monitorValue.count.getAndIncrement();
        monitorValue.totalTime.getAndAdd(timeCost);
        monitorValue.avgTile = monitorValue.totalTime.get()/monitorValue.count.get();
    }
}
