package cn.larry.demo.guice;

/**
 * Created by larry on 16-8-27.
 */
public class DatabaseStorage implements Storage  {


    @Override
    public void store(String uniqueId, Object data) {

    }

    @Override
    public Object retrieve(String uniqueId) {
        return null;
    }
}
