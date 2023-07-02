package cn.larry.analyzer;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class MQConsumerProxy<T,V> {


    private AtomicBoolean stoped;

    private MQConfig mqConfig;

    private Processor processor;

    private MqClient mqClient;

    private interface Processor<T> {

        void process(T tlise);

        T transfer(T str);
    }


    private Executor executor;

    private static class MQConfig {

    }

    private Mqclient build();

    private  void start() {

        mqClient = build();
        executor.execute(()->{consume();});
    }

    public MQConsumerProxy(MQConfig mqConfig, Processor processor) {
        this.mqConfig = mqConfig;
        this.processor = processor;
        start();
    }


    List<V> consumeData() {

    }

    private void consume() {
        while (!Thread.interrupted() && !stoped.get()) {
            try{

                List<String> strings = consumeData();
                if(strings!=null){
                    CountDownLatch latch = new CountDownLatch(strings.size());

                    try{
                        List<T> list = strings.stream().map(s -> processor.transfer(s)).collect(Collectors.toList());

                        processor.process(list);
                    }


                    latch.await(100);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    void onClose(){
        this.stoped.compareAndSet(false,true);

    }
}
