package cn.larry.search.book.index;

import cn.larry.search.book.bean.Book;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by larryfu on 16-6-8.
 */
public class Indexer {

    private String dataDir;

    public static void mains(String[] args) throws IOException {
        String indexDir = "/opt/search/book/index";
        String dataDir = "/home/larryfu/books";
        Indexer indexer = new Indexer();
        indexer.dataDir = indexDir;
        List<Book> books = new TransferData().getBooksFromDir(dataDir);
        indexer.index(books);
    }

    public static void maines(String[] args) throws IOException {
        //analyzeWithIK("新华社北京6月25日电（记者李忠发）国家主席习近平25日在人民大会堂同俄罗斯总统普京举行会谈。两国元首一致同意，坚持战略协作精神和世代友好理念，加大相互支持，增进政治和战略互信，坚定不移致力于深化中俄全面战略协作伙伴关系。中共中央政治局常委、国务院副总理张高丽出席。");
        List<String> strs = analyzeWithIK("两国元首一致同意，坚持战略协作精神和世代友好理念，加大相互支持，增进政治和战略互信，坚定不移致力于深化中俄全面战略协作伙伴关系");
        System.out.println(analyzeWithIK("我们的爱情"));
        List<String> nstrs = new ArrayList<>();
        String current = "";
        for (String s : strs) {
            if (current.contains(s)) continue;
            current = s;
            nstrs.add(s);
        }
        System.out.println(String.join("",nstrs));

    }

    public static void main(String[] args) throws IOException, ParseException {
        String indexDir = "/opt/search/book/index";
        // Now search the index:
        DirectoryReader ireader = DirectoryReader.open(FSDirectory.open(Paths.get(indexDir)));
        IndexSearcher isearcher = new IndexSearcher(ireader);
        QueryParser parser = new QueryParser("description", new WhitespaceAnalyzer());
        Query query = parser.parse("计算机 网络");
        ScoreDoc[] hits = isearcher.search(query, 100, Sort.RELEVANCE).scoreDocs;
        // Iterate through the results:
        for (int i = 0; i < hits.length; i++) {
            Document hitDoc = isearcher.doc(hits[i].doc);
            System.out.println(hitDoc.get("name"));
            // System.out.println(hitDoc.get("description"));
            // assertEquals("This is the text to be indexed.", hitDoc.get("fieldname"));
        }
    }

    private IndexWriter getIndexWriter() throws IOException {
        Directory directory = FSDirectory.open(Paths.get(dataDir));
        IndexWriterConfig config = new IndexWriterConfig(new WhitespaceAnalyzer());
        return new IndexWriter(directory, config);
    }

    public String analyzeDes(String text) {
        try {
            List<String> words = analyzeWithIK(text);
            return String.join(" ", words);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }


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

    private void index(List<Book> books) throws IOException {
        try (IndexWriter writer = getIndexWriter()) {
            List<Document> documents = new ArrayList<>();
            for (Book book : books) {
                Document document = new Document();
                document.add(new IntPoint("id", book.getId()));
                document.add(new TextField("description", analyzeDes(book.getDescription()), Field.Store.YES));
                document.add(new StringField("detailHref", book.getDetailHref(), Field.Store.YES));
                document.add(new DoublePoint("price", book.getPrice()));
                document.add(new DoublePoint("mark", book.getMark()));
                document.add(new TextField("press", book.getPress(), Field.Store.YES));
                document.add(new IntPoint("pressYear", book.getPressTime()));
                document.add(new TextField("name", book.getName(), Field.Store.YES));
                document.add(new IntPoint("evaluators", book.getEvaluaters()));
                for (String author : book.getAuthors())
                    document.add(new StringField("author", author, Field.Store.YES));
                document.add(new StringField("href", book.getDetailHref(), Field.Store.YES));
                documents.add(document);
            }
            writer.addDocuments(documents);
            writer.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
