package cn.larry.demo.guice;

import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * Created by larry on 16-8-27.
 */
public class AddModule implements Module {

    public void configure(Binder binder){
        binder.bind(Add.class).to(SimpleAdd.class);
    }
}
