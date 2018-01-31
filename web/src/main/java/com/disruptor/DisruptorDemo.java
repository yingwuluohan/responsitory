package com.disruptor;/**
 * Created by Admin on 2018/1/30.
 */

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @company: 北京云知声信息技术有限公司
 * @author: FANGNAN
 * @date: 2018/1/30
 **/
public class DisruptorDemo {
    public static void main(String[] args) throws InterruptedException {
        long beginTime=System.currentTimeMillis();

        int bufferSize=1024;
        ExecutorService executor= Executors.newFixedThreadPool(4);
        //这个构造函数参数，相信你在了解上面2个demo之后就看下就明白了，不解释了~
        Disruptor<TradeTransaction> disruptor=new Disruptor<TradeTransaction>(
                new EventFactory<TradeTransaction>() {//事件工厂
            @Override
            public TradeTransaction newInstance() {
                return new TradeTransaction();
            }
        }, bufferSize,//必须为2的幂次方
                executor,//线程工厂
                ProducerType.SINGLE,//指定生产者为一个或多个
                new BusySpinWaitStrategy());//等待策略

        //使用disruptor创建消费者组C1,C2
        EventHandlerGroup<TradeTransaction> handlerGroup=disruptor.handleEventsWith(//指定处理器
                new TradeTransactionVasConsumer(),
                new TradeTransactionInDBHandler()
        );

        tradeTransactionJmsnNotifyHandler jmsConsumer=new tradeTransactionJmsnNotifyHandler();
        //声明在C1,C2完事之后执行JMS消息发送操作 也就是流程走到C3
        handlerGroup.then(jmsConsumer);


        disruptor.start();//启动
        CountDownLatch latch=new CountDownLatch(1);
        //生产者准备
        executor.submit(new TradeTransactionPublisher(latch, disruptor));
        latch.await();//等待生产者完事.
        System.out.println( "执行over**************************************" );
        disruptor.shutdown();
        executor.shutdown();

        System.out.println("总耗时:"+(System.currentTimeMillis()-beginTime));
    }
}
