package cn.larry.demo.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.ja.tokenattributes.BaseFormAttribute;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;

import java.io.IOException;
import java.util.HashSet;

public class LuceneAnalyzer {

    public static void main(String[] args) throws IOException {
        EnglishAnalyzer analyzer = new EnglishAnalyzer(CharArraySet.copy(new HashSet<>()));
        TokenStream tokenStream = analyzer.tokenStream("content", "he didn't have a nice shoe ?");
        CharTermAttribute attribute = tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();
        while (tokenStream.incrementToken()){
            System.out.println(attribute);
        }
        //        Tokenizer source = new FooTokenizer(reader);
//        TokenStream filter = new FooFilter(source);
//        filter = new BarFilter(filter);
//        return new Analyzer.TokenStreamComponents(source, filter);
    }
}
