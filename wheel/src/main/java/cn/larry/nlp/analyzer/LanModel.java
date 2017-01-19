package cn.larry.nlp.analyzer;

/**
 * Created by larry on 16-12-25.
 */
public interface LanModel {

    double combineProbability(String start, String end);

    double wordProbability(String word);
}
