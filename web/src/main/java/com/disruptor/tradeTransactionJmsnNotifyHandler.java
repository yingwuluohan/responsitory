package com.disruptor;/**
 * Created by Admin on 2018/1/30.
 */

import com.lmax.disruptor.EventHandler;

/**
 * @company: 北京云知声信息技术有限公司
 * @author: FANGNAN
 * @date: 2018/1/30
 **/
public class tradeTransactionJmsnNotifyHandler  implements EventHandler<TradeTransaction> {

    @Override
    public void onEvent(TradeTransaction event, long sequence,
                        boolean endOfBatch) throws Exception {
        //do send jms message
        System.out.println( "最后执行***发送消息message :" + sequence + ",endOfBatch: " + endOfBatch );
    }
}
