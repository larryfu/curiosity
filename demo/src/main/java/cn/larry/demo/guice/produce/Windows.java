package cn.larry.demo.guice.produce;

import com.google.inject.Singleton;

/**
 * Created by larry on 16-8-27.
 */

public class Windows implements SoftWare {
    @Override
    public void doWork() {
        System.out.println("run game on windows");
    }

    @Override
    public void runGame() {
        System.out.println("run game on windows");
    }
}
