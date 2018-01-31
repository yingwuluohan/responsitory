package com.disruptor;/**
 * Created by Admin on 2018/1/30.
 */

import com.lmax.disruptor.dsl.Disruptor;

import java.util.concurrent.CountDownLatch;

/**
 * @company: 北京云知声信息技术有限公司
 * @author: FANGNAN
 * @date: 2018/1/30
 **/
public class TradeTransactionPublisher implements Runnable{
    Disruptor<TradeTransaction> disruptor;
    private CountDownLatch latch;
    private static int LOOP=10000;//模拟一千万次交易的发生

    public TradeTransactionPublisher(CountDownLatch latch,Disruptor<TradeTransaction> disruptor) {
        this.disruptor=disruptor;
        this.latch=latch;
    }

    @Override
    public void run() {
        TradeTransactionEventTranslator tradeTransloator=new TradeTransactionEventTranslator();
        for(int i=0;i<LOOP;i++){
            disruptor.publishEvent(tradeTransloator);
        }
        latch.countDown();
    }


}
