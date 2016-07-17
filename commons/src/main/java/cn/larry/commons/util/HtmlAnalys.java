package cn.larry.commons.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 解析html文档 抽取出带段落格式文本内容
 *
 * @author larryfu
 */
public class HtmlAnalys {

    private static final String[] inlineElements = {"APPLET", "BUTTON", "DEL", "IFRAME", "INS", "MAP", "OBJECT", "SCRIPT", "A", "ABBR", "ACRONYM", "B", "BASEFONT", "BDO", "BIG", "BR", "CITE", "CODE", "DFN", "EM", "FONT", "I", "IMG", "INPUT", "KBD", "LABEL", "Q", "S", "SAMP", "SELECT", "SMALL", "SPAN", "STRIKE", "STRONG", "SUB", "SUP", "TEXTAREA", "TT", "U", "VAR"};

    private static final String[] EXCLUDE_ELES = {"script", "link", "style"};

    static final Logger logger = LogManager.getLogger();

    private static final Map<String, String> transferMap = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {
            put("&quot;", "\"");
            put("&amp;", "&");
            put("&lt;", "<");
            put("&gt;", ">");
            put("&nbsp;", " ");
            put("\u3000", ""); //中文空格
        }
    };

    public static String replaceTransferChar(String html) {
        for (Map.Entry<String, String> entry : transferMap.entrySet())
            html = html.replaceAll(entry.getKey(), entry.getValue());
        return html;
    }


    public static String parseHtml(InputStream is) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.ISO_8859_1))) {
            String str = in.lines().collect(Collectors.joining());
            String html = FileUtils.transferHtmlCharset(StandardCharsets.ISO_8859_1, str);
            return parseHtml(html);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


    public static String removeExecludeTag(String html) {
        //将包含在<script>和</script>之间的所有内容去除
        for (String ele : EXCLUDE_ELES)
            html = html.replaceAll("(?i)<" + ele + "[^>]*>((?!<\\/" + ele + ">).)*</" + ele + "\\s*>", "");
        return html;
    }
    /*
     * 去除html标签，得到文本格式的简历
	 */

    public static String parseHtml(String html) {
        html = html.replaceAll("\\s+|\\t+|\n", "");

        //将包含在<script>和</script>之间的所有内容去除
        for (String ele : EXCLUDE_ELES)
            html = html.replaceAll("(?i)<" + ele + "[^>]*>((?!<\\/" + ele + ">).)*</" + ele + "\\s*>", "");

        html = html.replaceAll("(?i)<br\\s*/?\\s*>", "\n");
        html = html.replaceAll("<[^/][^>]*>", "\u0020");

        StringBuilder sb = new StringBuilder();
        for (String tag : inlineElements)
            sb.append(tag.toLowerCase()).append("|");
        html = html.replaceAll("(?i)</(" + sb.toString() + ")\\s*>", "\u0020");

        html = html.replaceAll("</[^>]*>", "\n");
        html = replaceTransferChar(html);
        logger.debug(html);
        return html;
    }


}
