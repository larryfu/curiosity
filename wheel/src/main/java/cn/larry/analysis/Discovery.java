package cn.larry.analysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by larry on 16-12-27.
 */
public class Discovery {
    public static void main(String[] args) {
        try (InputStream is = Analyzer.class.getResourceAsStream("/main.dic");
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            List<String> strs = br.lines().collect(Collectors.toList());
            List<String> single = strs.stream().filter(s->s.length()==1).collect(Collectors.toList());
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
