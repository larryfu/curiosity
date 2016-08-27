package cn.larry.demo.guice.produce;

import com.google.inject.ImplementedBy;
import com.google.inject.ProvidedBy;

/**
 * Created by larry on 16-8-27.
 */
@ProvidedBy(SoftwareProvider.class)
public interface SoftWare {

    void doWork();

    void runGame();
}
