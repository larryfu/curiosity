package cn.larry;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.Set;

/**
 * Created by lucas on 17-2-28.
 */
public class RedisTest {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("127.0.0.1", 6380);
        Set<Tuple> resultSet = jedis.zrevrangeWithScores("rank_1", 1, 5);
        for(Tuple tuple :resultSet){
            System.out.println(tuple.getElement());
            System.out.println( tuple.getScore());
        }
        System.out.printf("");
    }
}
