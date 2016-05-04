package cn.larry.commons.util;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * @author larryfu
 */
public class FileUtils {

    private FileUtils() {
    }

    public static String transferHtmlCharset(Charset origin, String html) {
        String charset = RegexUtils.getFirstMatch("(?:charset=)[^\"^;^']*", html);
        if (charset != null && charset.length() > 8) {
            charset = charset.substring(8);
        } else {
            charset = RegexUtils.getFirstMatch("charset=\"[^\"^;^']*\"", html);
            if (charset != null && charset.length() > 10)
                charset = charset.substring(8).replace("\"", "");
        }
        if (charset == null)
            charset = "UTF-8";

        // logger.info("transfer charset:" + charset);
        byte[] bytes = html.getBytes(origin);
        try {
            return new String(bytes, charset);
        } catch (UnsupportedEncodingException e) {
            //  logger.info("unsuppoted  charset:" + charset + ",return as UTF-8");
            e.printStackTrace();
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }

    public static void saveFile(InputStream inStream, String path) throws IOException {
        File tmpFile = new File(path);
        try (FileOutputStream fStream = new FileOutputStream(tmpFile)) {
            byte[] b = new byte[4 * 1024];
            int len;
            while ((len = inStream.read(b, 0, 4 * 1024)) != -1)
                fStream.write(b, 0, len);
        }
    }

    public static String getFileSuffix( String fileName) throws IllegalArgumentException {
        if (!fileName.contains("."))
            throw new IllegalArgumentException("文件无扩展名");
        String[] parts = fileName.split("\\.");
        return parts[parts.length - 1];
    }

    public static String getFileAsString(String path, String charset) throws IOException {
        return getFileAsString(new File(path), charset);
    }

    public static String getFileAsString(File fl, String charset) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fl), charset))) {
            return br.lines().collect(Collectors.joining());
        }
    }
}
