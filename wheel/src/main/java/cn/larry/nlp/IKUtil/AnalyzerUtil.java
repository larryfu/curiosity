package cn.larry.nlp.IKUtil;

import org.apache.commons.lang3.StringUtils;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by larry on 16-12-17.
 */
public class AnalyzerUtil {
    public static List<String> analyzeWithIK(String text) throws IOException {
        List<String> list = new ArrayList<>();
        if (StringUtils.isNotBlank(text)) {
            StringReader reader = new StringReader(text);
            IKSegmenter ik = new IKSegmenter(reader, false);
            Lexeme lexeme;
            while ((lexeme = ik.next()) != null) {
                String word = lexeme.getLexemeText();
                list.add(word);
            }
        }
        return list;
    }
}
