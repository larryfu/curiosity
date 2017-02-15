package cn.larry.demo.dubbo;

import scala.tools.nsc.backend.icode.TypeKinds;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by larry on 17-2-14.
 */
public class IrisAnalysis {


    public static void main(String[] args) throws IOException {
        String path = "/home/larry/data/iris/iris.data.txt";

        List<String> strings = Files.readAllLines(Paths.get(path));
        List<List<String>> datas = strings.stream().map(s -> Arrays.asList(s.split(","))).collect(Collectors.toList());

        List<List<Double>> virginica = filterIrisType(datas, "Iris-virginica");
        List<List<Double>> versicolor = filterIrisType(datas, "Iris-versicolor");
        List<List<Double>> setosa = filterIrisType(datas, "Iris-setosa");
        List<Double> setosaAvg = getAvg(setosa);
        List<Double> versicolorAvg = getAvg(versicolor);
        List<Double> virginicaAvg = getAvg(virginica);

        String pathTest = "/home/larry/data/iris/bezdekIris.data.txt";
        List<String> testStrings = Files.readAllLines(Paths.get(pathTest));
        List<List<String>> testDatas = testStrings.stream().map(s -> Arrays.asList(s.split(","))).collect(Collectors.toList());
        List<List<Double>> virginicaTest = filterIrisType(testDatas, "Iris-virginica");
        List<List<Double>> versicolorTest = filterIrisType(testDatas, "Iris-versicolor");
        List<List<Double>> setosaTest = filterIrisType(testDatas, "Iris-setosa");


        int virgMiss = 0;
        for (List<Double> doubles : virginicaTest) {
            double osaCha = countDistance(doubles, setosaAvg);
            double versCha = countDistance(doubles, versicolorAvg);
            double virgCha = countDistance(doubles, virginicaAvg);
            if (virgCha > osaCha || virgCha > versCha) {
                virgMiss++;
            }
        }
        System.out.println("virgMiss:" + virgMiss);

        int osaMiss = 0;
        for (List<Double> doubles : setosaTest) {
            double osaCha = countDistance(doubles, setosaAvg);
            double versCha = countDistance(doubles, versicolorAvg);
            double virgCha = countDistance(doubles, virginicaAvg);
            if (osaCha > versCha || osaCha > virgCha) {
                osaMiss++;
            }
        }
        System.out.println("osaMiss:" + osaMiss);
        int versMiss = 0;
        for (List<Double> doubles : versicolorTest) {
            double osaCha = countDistance(doubles, setosaAvg);
            double versCha = countDistance(doubles, versicolorAvg);
            double virgCha = countDistance(doubles, virginicaAvg);
            if (versCha > osaCha || versCha > virgCha) {
                versMiss++;
            }
        }
        System.out.println("versMiss:" + versMiss);
        System.out.println();
    }


    /**
     * 计算一组花瓣数据的平均值
     *
     * @param setosa
     * @return
     */
    private static List<Double> getAvg(List<List<Double>> setosa) {

        List<Double> avg = new ArrayList<>();
        for (int i = 0; i < setosa.get(0).size(); i++)
            avg.add(i, 0.0);
        for (List<Double> doubles : setosa)
            for (int i = 0; i < doubles.size(); i++)
                avg.set(i, avg.get(i) + doubles.get(i));
        for (int i = 0; i < avg.size(); i++)
            avg.set(i, avg.get(i) / (double) setosa.size());
        return avg;
    }

    /**
     * 计算两个花瓣的差别
     *
     * @param doubles1
     * @param doubles2
     * @return
     */
    public static double countDistance(List<Double> doubles1, List<Double> doubles2) {
        if (doubles1.size() != doubles2.size())
            throw new IllegalArgumentException();
        double total = 0.0;
        for (int i = 0; i < doubles1.size(); i++) {
            double cha = doubles1.get(i) - doubles2.get(i);
            total += cha * cha;
        }
        return Math.sqrt(total);
    }


    /**
     * 过滤出花瓣类别数据
     *
     * @param datas
     * @param name
     * @return
     */
    private static List<List<Double>> filterIrisType(List<List<String>> datas, String name) {
        return datas.stream().filter(l -> l.contains(name)).map(l -> l.stream().limit(l.size() - 1).collect(Collectors.toList())).
                map(l -> l.stream().map(Double::parseDouble).collect(Collectors.toList())).collect(Collectors.toList());
    }

}

