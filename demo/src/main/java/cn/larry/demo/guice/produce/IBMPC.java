package cn.larry.demo.guice.produce;

import com.google.inject.Singleton;

/**
 * Created by larry on 16-8-27.
 */

public class IBMPC implements HardWare {
    @Override
    public void startup() {
        System.out.println("IBM PC  start up");

    }

    @Override
    public void shutdown() {
        System.out.println("IBM PC  shut down");
    }
}
