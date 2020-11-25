package com.wxrem.controller;


/**
 * @author wxb
 * @date 2020-08-28 18:05
 */
public class ClusterCanalClientTest {

static {

}
    //a. 创建SimpleCanalConnector (直连ip，不支持server/client的failover机制)
    //CanalConnector connector = CanalConnectors.newSingleConnector(
    // new InetSocketAddress(AddressUtils.getHostIp(),11111), destination, "", "");

    //b. 创建ClusterCanalConnector (基于zookeeper获取canal server ip，支持server/client的failover机制)
    //CanalConnector connector = CanalConnectors.newClusterConnector("10.20.144.51:2181", destination, "", "");

    //c. 创建ClusterCanalConnector (基于固定canal server的地址，支持固定的server ip的failover机制，不支持client的failover机制
    //CanalConnector connector = CanalConnectors.newClusterConnector(
    // Arrays.asList(new InetSocketAddress(AddressUtils.getHostIp(),11111)), destination,"", "");



}
