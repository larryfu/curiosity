package cn.larry.demo.guice.produce;

import com.google.inject.ImplementedBy;

/**
 * Created by larry on 16-8-27.
 */
@ImplementedBy(ComputerProducer.class)
public interface Producer {

    void produce();
}
