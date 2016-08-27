package cn.larry.demo.guice.Player;

import com.google.inject.Provider;

/**
 * Created by larry on 16-8-28.
 */
public class PlayProvider implements Provider<Player> {
    @Override
    public Player get() {
        return new GoodPlayer();
    }
}
