package com.java.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * Created by fn on 2017/5/9.
 */
public class TimeServerHandler implements Runnable {

    private Socket socket;
    public TimeServerHandler( Socket socket ){
        this.socket = socket;
    }
    public TimeServerHandler( ){
    }

    public void run(){
        BufferedReader in = null;
        PrintWriter out = null;
        try{
            in = new BufferedReader( new InputStreamReader( this.socket.getInputStream() ));
            out = new PrintWriter( this.socket.getOutputStream() ,true );
            String currentTime = null;
            String body = null;
            boolean read = true;
            while( read ){
                body = in.readLine();
                if( body == null ){
                    break;
                }
                System.out.println( "break" );
                currentTime = "".equalsIgnoreCase( body )? new Date( System.currentTimeMillis() ).toString():"bad time";
                System.out.println( currentTime );
            }


        }catch ( Exception e ){
            if( null != in ){
                try {
                    in.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if( null != out ){
                out.close();
            }
            if( socket != null ){
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
