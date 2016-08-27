package cn.larry.demo.guice.produce;

import com.google.inject.ImplementedBy;

/**
 * Created by larry on 16-8-27.
 */
@ImplementedBy(IBMPC.class)
public interface HardWare {

    void startup();

    void shutdown();
}
