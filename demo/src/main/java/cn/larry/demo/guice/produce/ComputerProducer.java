package cn.larry.demo.guice.produce;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Created by larry on 16-8-27.
 */
@Singleton
public class ComputerProducer implements Producer {

    @Inject
    private HardWare hardWare;

    @Inject
    private SoftWare softWare;

    @Override
    public void produce() {
        System.out.println("produce hardware:"+hardWare.getClass().toString()+" software:"+softWare.getClass().toString());
    }
}
