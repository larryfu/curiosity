package cn.larry.demo.guice.Player;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Named;


/**
 * Created by larry on 16-8-28.
 */
public class PlayerTest {

    @Inject

    @Named("good")
    private  Player goodPlayer ;

    @Inject
   @Named("bad")
    private Player badPlayer;

    public static void main(String[] args) {
        PlayerModule module = new PlayerModule();
        Injector injector =Guice.createInjector(new Module[]{module});


//        @Good
//        Player player = injector.getInstance(Player.class);
//        System.out.println(player);
        PlayerTest test = injector.getInstance(PlayerTest.class);
        System.out.println(test.goodPlayer);
        System.out.println(test.badPlayer);
    }
}
