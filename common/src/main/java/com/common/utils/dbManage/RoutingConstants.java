package com.common.utils.dbManage;

/**
 * Db Routing常量类
 * @author pgf
 *
 */
public class RoutingConstants {
    //集群类型，READONLY只读，READWRITE读写,READMUTIWRITE 读并且多写 
	public static final String CLUSTER_TYPE_READONLY = "READONLY";
	public static final String CLUSTER_TYPE_READWRITE= "READWRITE";
	public static final String CLUSTER_TYPE_READMUTIWRITE = "READMUTIWRITE";
	
	// 负载均衡 策略，最小连接数MINCONNECT、最高性能MAXPERFORMANCE、随机数 RANDOM、Hash写 HASHWRITE，尾号写 NUMBERWRITE
	public static final String CLUSTER_STRATEGY_MINCONNECT = "MINCONNECT";
	public static final String CLUSTER_STRATEGY_MAXPERFORMANCE = "MAXPERFORMANCE";
	public static final String CLUSTER_STRATEGY_RANDOM = "RANDOM";
	public static final String CLUSTER_STRATEGY_HASHWRITE = "HASHWRITE";
	public static final String CLUSTER_STRATEGY_NUMBERWRITE = "NUMBERWRITE";
}
