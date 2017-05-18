package com.java.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * Created by fn on 2017/5/9.
 */
public class TimeServer {


    public static void main( String[] args ){
        int port = 8080;
        if( null != args && args.length > 0 ){
            try{
                port = Integer.parseInt( args[ 0 ] );
            }catch ( Exception e ){}

        }
        ServerSocket server = null;
        try{
            server = new ServerSocket( port );
            System.out.println( port );
            Socket socket = null;
            boolean run = true;
            while( run ){
                if( null != server ){
                    socket = server.accept();
                    new Thread( new TimeServerHandler( socket ) ).start();
                }else{
                    run = false;
                }
            }
        }catch ( IOException e ){

        }finally{
            if( null != server ){
                System.out.println( "socket close" );
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                server = null;
            }
        }

    }
    public void openLine( int port){
        try {
            ServerSocketChannel accepte = ServerSocketChannel.open();
            accepte.socket().bind( new InetSocketAddress( InetAddress.getByName( "" ),port ));
            accepte.configureBlocking( false );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readerLine(){
        try {
            Selector selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






}
