package cn.larry.demo.guice.Player;

import com.google.inject.Singleton;

/**
 * Created by larry on 16-8-28.
 */

public class GoodPlayer implements Player {
    @Override
    public void play() {
        System.out.println("good player get high score");
    }
}
