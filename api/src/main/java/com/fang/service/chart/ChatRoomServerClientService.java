package com.fang.service.chart;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by fn on 2017/5/22.
 */
public interface ChatRoomServerClientService {

    void initClientService() throws IOException;

    void writeContent( String content );
}
