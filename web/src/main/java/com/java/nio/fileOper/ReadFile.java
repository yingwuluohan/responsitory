package com.java.nio.fileOper;

import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 */
public class ReadFile {

    private final byte[] bytes = new byte[ 1024 * 1024 * 5 ];

    public String readFile( String fileName ){

        FileInputStream fileInputStream=null;
        try{
            File file = new File( fileName );
            MappedByteBuffer inputBuffer = new RandomAccessFile(file, "r").getChannel().map(
                     FileChannel.MapMode.READ_ONLY,
                    file.length() / 2,
                    file.length() / 2);

            int allLang = inputBuffer.capacity();
        }catch ( Exception e ){

        }
        return null;

    }






}
