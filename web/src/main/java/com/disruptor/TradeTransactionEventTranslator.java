package com.disruptor;/**
 * Created by Admin on 2018/1/30.
 */

import com.lmax.disruptor.EventTranslator;

import java.util.Random;

/**
 * @company: 北京云知声信息技术有限公司
 * @author: FANGNAN
 * @date: 2018/1/30
 **/
public class TradeTransactionEventTranslator implements EventTranslator<TradeTransaction> {
    private Random random=new Random();
    @Override
    public void translateTo(TradeTransaction event, long sequence) {
        this.generateTradeTransaction(event);
    }
    private TradeTransaction generateTradeTransaction(TradeTransaction trade){
        trade.setPrice(random.nextDouble()*9999);
        return trade;
    }
}
