package com.common.utils.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ZookeeperUtil{
    public static int port = 2181;
    private ZooKeeper zk = null;
    public static void main(String[] args) {
        String ip = args[0];
        String ip1 = args[1];
        String nodeString = args[2];
        String fromPath = "/SystemGlobals/"+nodeString;
        String targetPath = "/SystemGlobals/"+nodeString;
        String targetFile = "d:/SystemGlobals.properties";
        try {
            ZookeeperUtil util = new ZookeeperUtil(ip,port);
            ZooKeeper zk1 = util.zk;


            util = new ZookeeperUtil(ip1,port);

            ZooKeeper zk2 = util.zk;
            Stat stat = zk2.exists(targetPath,true);
            if(stat==null){
                zk2.create(targetPath,new String("0").getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }

            List<String> nodeList = zk1.getChildren(fromPath, true);
            for(String node : nodeList){
                String newPath = fromPath+"/"+node;
                Stat stattmp = zk2.exists(newPath,true);
                if(stattmp==null){
                    String nodeTmp = fromPath+"/"+node;
                    byte[] bytes = zk1.getData(nodeTmp,null,null);
                    zk2.create(newPath,bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
            }
            //writeFile(mapList,targetFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeFile(List<Map> ls,String filePath)throws Exception{
        File file = new File(filePath);
        if(!file.exists()){
            file.createNewFile();
        }
        System.out.println(filePath);

        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        for(Map map : ls){
            String key = map.get("key").toString();
            String val = map.get("val").toString();
            String data = key+"="+val+"\r\n";
            System.out.println(data);
            bw.write(data);
        }
        bw.close();
    }
    public ZookeeperUtil(String ipAddress,int hostPort) throws KeeperException, IOException {
        this.zk = new ZooKeeper(ipAddress+":"+hostPort,
                10000, new Watcher() {
            // 监控所有被触发的事件
            public void process(WatchedEvent event) {
                // TODO Auto-generated method stub
                System.out.println(event.getPath()+"stat " + event.getType() + " change！");
            }
        });
    }


}
