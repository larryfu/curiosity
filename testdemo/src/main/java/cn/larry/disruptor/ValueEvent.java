package cn.larry.disruptor;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;


public  class ValueEvent {
    private int value;
    public final static EventFactory EVENT_FACTORY
            = () -> new ValueEvent();

    // standard getters and setters
}
