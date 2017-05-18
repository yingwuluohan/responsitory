package com.java.nio;

/**
 * Created by   on 2017/5/17.
 */
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class FileServer {
    private int port = 8000;
    /* 发送数据缓冲区 */
    private static ByteBuffer revBuffer = ByteBuffer.allocate(1024);
    private static Selector selector;
    private static FileOutputStream fout;
    private static FileChannel fileChannel;
    public FileServer(){
        try{
            init();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    private void init() throws Exception{
        // 获得一个ServerSocket通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 设置通道为非阻塞
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        // 将该通道对应的ServerSocket绑定到port端口
        serverSocket.bind(new InetSocketAddress(port));
        // 获得一个通道管理器
        selector = Selector.open();
        //将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_ACCEPT事件,注册该事件后，
        //当该事件到达时，selector.select()会返回，如果该事件没到达selector.select()会一直阻塞。
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("server start on port:" + port);
        while (true) {
            try {
                int countnum = selector.select();// 返回值为本次触发的事件数
                System.out.println( countnum );
                // 获得selector中选中的项的迭代器，选中的项为注册的事件
                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                for (SelectionKey key : selectionKeys) {
                    ServerSocketChannel server = null;
                    SocketChannel socketChannel = null;
                    int count = 0;
                    if (key.isAcceptable()) {
                        server = (ServerSocketChannel) key.channel();
                        System.out.println("有客户端连接进入=============)");
                        // 获得和客户端连接的通道
                        socketChannel = server.accept();
                        socketChannel.configureBlocking(false);
                       // client.write(ByteBuffer.wrap(new String("向客户端发送了一条信息*****************").getBytes()));
                        //在和客户端连接成功之后，为了可以接收到客户端的信息，需要给通道设置读的权限。
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        fout = new FileOutputStream("D:\\" + socketChannel.hashCode() + ".rar");
                        fileChannel = fout.getChannel();
                        // 获得了可读的事件
                    } else if (key.isReadable()) {
                        socketChannel = (SocketChannel) key.channel();
                        revBuffer.clear();
                        count = socketChannel.read(revBuffer);
                        int k = 0;
                        // 循环读取缓存区的数据，
                        while(count > 0){
                            System.out.println("k=" + (k++) + " 读取到数据量:" + count);
                            revBuffer.flip();
                            fileChannel.write(revBuffer);
                            fout.flush();
                            revBuffer.clear();
                            count = socketChannel.read(revBuffer);
                        }
                        if(count == -1){
                            socketChannel.close();
                            fileChannel.close();
                            fout.close();
                        }
                    }
                    else if (key.isWritable()) {
                        System.out.println("selectionKey.isWritable()");

                    }
                }
                System.out.println("=======selectionKeys.clear()");
                selectionKeys.clear();
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }

        }
    }
    public static void main(String[] args){
        new FileServer();
    }
}
