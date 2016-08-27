package cn.larry.demo.guice.produce;

import com.google.inject.Provider;

/**
 * Created by larry on 16-8-28.
 */
public class SoftwareProvider implements Provider<SoftWare> {
    @Override
    public SoftWare get() {
        return new Windows();
    }
}
