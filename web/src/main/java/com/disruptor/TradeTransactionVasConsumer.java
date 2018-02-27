package com.disruptor;/**
 * Created by Admin on 2018/1/30.
 */

import com.lmax.disruptor.EventHandler;

/**
 * @company: 东方
 * @author: FANGNAN
 * @date: 2018/1/30
 **/
public class TradeTransactionVasConsumer implements EventHandler<TradeTransaction> {

    @Override
    public void onEvent(TradeTransaction event, long sequence,
                        boolean endOfBatch) throws Exception {
        //do something....
        System.out.println( "消费消息："  + event.getId() + ",:" + sequence );
    }


}
