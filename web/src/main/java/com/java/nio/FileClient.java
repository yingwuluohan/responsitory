package com.java.nio;

/**
 * Created by  on 2017/5/17.
 */
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class FileClient {
    private int port = 8000;
    /* 发送数据缓冲区 */
    private static ByteBuffer sendBuffer = ByteBuffer.allocate(1024);
    private InetSocketAddress SERVER;
    private static Selector selector;
    private static SocketChannel client;

    public FileClient(){
        try{
            SERVER = new InetSocketAddress("localhost", port);
            init();
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
    private void init(){
        try {
            // 获得一个Socket通道
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            // 获得一个通道管理器
            selector = Selector.open();
            //将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_CONNECT事件。
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            socketChannel.connect(SERVER);
            // 轮询访问selector
            int numLink=0;
            while (true) {
                selector.select();
                // 获得selector中选中的项的迭代器
                Set<SelectionKey> keySet = selector.selectedKeys();
                for (final SelectionKey key : keySet) {
                    if(key.isConnectable()){
                        numLink++;
                        client = (SocketChannel)key.channel();
                        client.finishConnect();
                        client.register(selector, SelectionKey.OP_WRITE);
                        System.out.println( "第" + numLink + "次连接");
                    }
                    else if(key.isWritable()){
                        sendFile(client);
                    }
                }
                keySet.clear();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendFile(SocketChannel client) {
        FileInputStream fis = null;
        FileChannel channel = null;
        try {
//          fis = new FileInputStream("E:\\1.txt");
//          fis = new FileInputStream("E:\\1.rar");
            fis = new FileInputStream("E:\\nio.rar");
            channel = fis.getChannel();
            int i = 1;
            int count = 0;
            while((count = channel.read(sendBuffer)) != -1) {
                sendBuffer.flip();
                int send = client.write(sendBuffer);
                System.out.println("i===========" + (i++) + "   count:" + count + " send:" + send);
                // 服务器端可能因为缓存区满，而导致数据传输失败，需要重新发送
                while(send == 0){
                    Thread.sleep(10);
                    send = client.write(sendBuffer);
                    System.out.println("i重新传输====" + i + "   count:" + count + " send:" + send);
                }
                sendBuffer.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                channel.close();
                fis.close();
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public static void main(String[] args){
        new FileClient();
    }
}
