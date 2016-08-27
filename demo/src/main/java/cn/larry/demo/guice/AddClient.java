package cn.larry.demo.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Created by larry on 16-8-27.
 */
public class AddClient {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new AddModule());
        Add add = injector.getInstance(Add.class);

        System.out.println(add.add(10,43));
    }
}
