package com.common.utils.dbManage;

import com.common.utils.hash.ConsistentHash;
//import org.logicalcobwebs.proxool.ProxoolException;
//import org.logicalcobwebs.proxool.ProxoolFacade;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * 数据库集群，包括读库，写库等多种类型，通过这个类获取到数据库集群的数据库连接.
 * 假设数据库数量是X，数据库最大连接数Y，Web服务器Z，那么每台服务器最大连接数的设置为X*Y/Z，平均每个连接池最大为：(X*Y/Z)/X=Y/Z
 * 例如假设有3个数据库，每个数据库最大连接数500，如果假设10台Web服务器，那么每个WEb数据库最大连接数为150个，平均每个连接池最大为50个
 * 建议每个连接池最大设置不小于30个，否则很容易出现连接池满的情况，当然对于读库，如果某一个连接池满，系统会自动漂移到另外的机器，但是性能会有所下降
 *
 */
public class DbRouting {
    /**
     * Cluster节点ID，这个是系统集群的唯一标识，
     在配置文件中设置，不允许重复，可以通过这个参数获取相关集群的连接
     */
    private String clusterId;

    /**
     * 数据库集群的类型，有只读，读写和读多写，具体参考配置文件dbrouting.xml
     */
    String clusterType;

    /**
     * 数据库集群的策略，包括最小连接、随机数、最高性能、尾号写策略、一致性Hash策略
     */
    String clusterStrategy;

    /**
     * 连接无法获取的重试时间，如果连接无法获取，系统会休眠retryTime的时间，然后重试一次。
     * 如果还是无法获取，如果是写库，直接抛出异常，如果是读库，系统将自动漂移到别的节点，
     * 然后再尝试获取一次，重试获取一次，如果还是没有取到，那么直接返回异常，默认是200毫秒
     */
    private long retryTime = 200;

    private Object lock = new Object();

    /**
     * 存储当前数据库集群中所有的节点属性集合，这个属性集合是从配置文件中读取的所有的节点的集合
     */
    private List<Properties> nodeProps = new ArrayList<Properties>();

    /**
     * 存储当前数据库集群中所有的运行的节点的集合
     */
    private List<String> nodes = new CopyOnWriteArrayList<String>();

    /**
     * 存储当前数据库集群中所有的所有设置了尾号的节点集合
     */
    private Map<String, Map<String, Integer>> nodeTrailNumber = new HashMap<String, Map<String, Integer>>();

    /**
     * 初始化一致性Hash算法
     */
    private ConsistentHash<String> writeHash = null;

    /**
     * 定义Log实例
     */
    //private Logging log = Logging.getLog("dbrouting");

    /**
     * 定义节点的监控，这个节点监控可以监控读集群、写集群等
     */
    private  ClusterNodeMonitor monitor;

    /**
     * 初始化一个节点队列，这个队列主要是为了读库的最大性能算法而设计的
     */
    //private final ArrayBlockingQueue<String> nodeQueue = new ArrayBlockingQueue<String>(10);

    private final ConcurrentLinkedQueue<String> nodeQueue = new ConcurrentLinkedQueue<String>();

    /**
     * 初始化一个阻塞的优先队列，这个队列主要是为了读库的最小连接数而设计的
     */
//    private final PriorityBlockingQueue<PrioryDS> dsPrioryQueue = new PriorityBlockingQueue<PrioryDS>();

    public DbRouting(String clusterId) {
        this.clusterId = clusterId;
    }

    /**
     * 阻塞的优先队列的实例，实现比较接口，比较队列中数据的大小
     */
//    class PrioryDS implements Comparable<PrioryDS> {
//        String nodeName;
//
//        public PrioryDS(String nodeId) {
//            this.nodeName = nodeId;
//        }
//
//        public String getName() {
//            return nodeName;
//        }
//
////        public int compareTo(PrioryDS arg0) {
////            try {
////                return ProxoolFacade.getSnapshot(nodeName)
////                        .getAvailableConnectionCount() > ProxoolFacade
////                        .getSnapshot(arg0.getName())
////                        .getAvailableConnectionCount() ? 0 : 1;
////            } catch (ProxoolException e) {
////                e.printStackTrace();
////            }
////            return 0;
////        }
////
////        public String toString() {
////            try {
////                return nodeName
////                        + "."
////                        + ProxoolFacade.getSnapshot(nodeName)
////                        .getAvailableConnectionCount();
////            } catch (ProxoolException e) {
////                e.printStackTrace();
////            }
////            return nodeName + ".0";
////        }
//    }
//************************************
    /**
     * 获取读写数据库集群，默认只取第一个节点的连接，除非多个连接是共享存储的，否则只需要设置一个即可。
     *
     * @return 返回数据库连接
     * @throws SQLException
     */
    private Connection getReadWriteConnection() throws SQLException {
        if (nodes.size() > 0) {
            return null;//getConnectionFromPool(nodes.get(0));
        } else {
            throw new SQLException(
                    "the size of cluster nodes is 0,can not get db connection");
        }
    }


    /**
     * 获取数据库集群中的某一个数据库连接，这些连接是根据是否是读库，读写库等类型和策略进行选择的，这个方法不支持读多写
     *
     * @return 返回一个数据库连接
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        if (RoutingConstants.CLUSTER_TYPE_READONLY.equals(this.clusterType)) {
            // 如果是读库
            return null;//getReadonlyConnection();
        } else if (RoutingConstants.CLUSTER_TYPE_READWRITE
                .equals(this.clusterType)) {
            // 如果是读写库，即普通读写，一般是一个数据库节点，那么只返回第一个节点的连接
            return getReadWriteConnection();
        } else if (RoutingConstants.CLUSTER_TYPE_READMUTIWRITE
                .equals(this.clusterType)) {
            // 如果是读，但是多写库，需要做写库策略
            throw new SQLException(
                    "the type:"
                            + this.clusterType
                            + " can not support getConnection(),please using getConection(String,String)方法");
        } else {
            throw new SQLException(
                    "the type:"
                            + this.clusterType
                            + " can not support,please set the  clustertype in dbrouting.xml correct");
        }
    }

    /**
     * 获取数据库集群中的某一个数据库连接，这些连接是根据是否是读库，读写库,读多写库等类型和策略进行选择的
     *
     * @param token
     *            写库策略值，根据这个值来判断定位到哪个库
     * @param tokenType
     *            写库策略类型，根据类型来决定如何定位
     * @return 返回一个符合连接的促销
     * @throws SQLException
     */










}
