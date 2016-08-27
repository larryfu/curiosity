package cn.larry.demo.guice.produce;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * Created by larry on 16-8-27.
 */
public class Gamer {

    public static void main(String[] args) {

        Injector injector = Guice.createInjector();
        Producer producer = injector.getInstance(Producer.class);
        producer.produce();

    }
}
