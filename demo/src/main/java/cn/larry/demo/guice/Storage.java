package cn.larry.demo.guice;

/**
 * Created by larry on 16-8-27.
 */
public interface Storage {


     void store(String uniqueId, Object data);
     Object retrieve(String uniqueId);
}
