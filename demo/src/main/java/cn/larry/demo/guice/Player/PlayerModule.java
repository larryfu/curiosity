package cn.larry.demo.guice.Player;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

/**
 * Created by larry on 16-8-28.
 */
public class PlayerModule implements Module {
    @Override
    public void configure(Binder binder) {
        //binder.bind(Player.class).annotatedWith(Good.class).to(GoodPlayer.class);
        //binder.bind(Player.class).annotatedWith(Bad.class).to(BadPlayer.class);
        binder.bind(Player.class).annotatedWith(Names.named("good")).to(GoodPlayer.class);
        binder.bind(Player.class).annotatedWith(Names.named("bad")).to(BadPlayer.class);
       // binder.bind(Player.class).toProvider(PlayProvider.class);
    }
}
