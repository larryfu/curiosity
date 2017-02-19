package cn.larry;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.Collections;
import java.util.List;

/**
 * Hello world!
 */
public class App {


    public static void main(String[] args) {
        JavaSparkContext sc = new JavaSparkContext("local[2]", "First Spark App");
        JavaRDD<String[]> data = sc.textFile("data/UserPurchaseHistory.csv").map(s -> s.split(","));
        long numberPurchases = data.count();
        long uniqueUsers = data.map(ss -> ss[0]).distinct().count();
        double totalRevenue = data.map(ss -> Double.parseDouble(ss[2])).reduce((x, y) -> x + y);
        List<Tuple2<String, Integer>> pairs = data.mapToPair(ss -> new Tuple2<>(ss[1], 1))
                .reduceByKey((x, y) -> x + y).collect();
        Collections.sort(pairs, (t1, t2) -> -(t1._2() - t2._2()));
        String mostPopular = pairs.get(0)._1();
        int purchases = pairs.get(0)._2();
        System.out.println("total purchases :" + numberPurchases);
        System.out.println("unique User:" + uniqueUsers);
        System.out.println("total revenue:" + totalRevenue);
        System.out.printf("most popular product %s with %d purchases", mostPopular, purchases);
    }


}
