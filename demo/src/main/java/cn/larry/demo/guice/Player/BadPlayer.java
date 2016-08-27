package cn.larry.demo.guice.Player;

/**
 * Created by larry on 16-8-28.
 */
public class BadPlayer implements Player {
    @Override
    public void play() {
        System.out.println("bad player get low score");
    }
}
