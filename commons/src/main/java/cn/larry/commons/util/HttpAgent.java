package cn.larry.commons.util;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static cn.larry.commons.util.NetUtils.urlEncodeWithUTF8;


/**
 * @author larryfu
 */
public class HttpAgent {

    private static final Logger logger = LogManager.getLogger();


    private Map<String, String> cookieMap = new HashMap<>();

    private String cookie;

    private String currentURL;

    private Map<String, String> headMap = new HashMap<>();

    protected void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public HttpAgent() {
        headMap.put("Accept", "*/*");
        headMap.put("Accept-Language", "en-US,en;q=0.8");
        headMap.put("Connection", "keep-alive");
        // headMap.put("Content-Type", "application/x-www-form-urlencoded");
        // headMap.put("Host", "www.liepin.com");
        //  headMap.put("Origin", "http://www.liepin.com");
        //  headMap.put("Referer", "http://www.liepin.com/user/login/");
        headMap.put("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.97 Safari/537.36");
        //    headMap.put("X-Requested-With", "XMLHttpRequest");
    }

    public String getPage(String url, Map<String, String> pm) {
        StringBuilder sb = new StringBuilder();
        pm.forEach((k, v) -> {
            logger.info("key:" + k + ",value:" + v);
            sb.append(urlEncodeWithUTF8(k) + "=" + urlEncodeWithUTF8(v));
            sb.append("&");
        });
        return getPage(url + "?" + sb.toString());
    }

    public String sendPost(String url, Map<String, String> pm) {
        StringBuilder sb = new StringBuilder();
        pm.forEach((k, v) -> {
            logger.info("key:" + k + ",value:" + v);
            sb.append(urlEncodeWithUTF8(k) + "=" + urlEncodeWithUTF8(v));
            sb.append("&");
        });
        return sendPost(url, sb.toString());
    }

    public String getCurrentURL() {
        return currentURL;
    }

    public String getCurrentPage() {
        return getPage(currentURL);
    }

    /**
     * @param url
     * @param param 请求参数
     * @return 请求的页面

     */

    public String sendPost(String url, String param)  {
        try {
            currentURL = url;

            URL realUrl = new URL(url);
            URLConnection conn;
            logger.info("send http post url:" + url + ",data:" + param);
            if (url.startsWith("https"))
                HttpUtils.truseAllHttpsCert();
            conn = realUrl.openConnection();
            // 设置通用的请求属性
            setHead(conn);
            // HttpUtils.setCommonHead(conn);
            setCookie(conn);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);

            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                // logger.info("send post param:" + param);

                byte[] bytes = param.getBytes(StandardCharsets.UTF_8);
                logger.info(bytes.length);
                for (int i = 0; i < bytes.length; i++)
                    wr.write(bytes, i, 1);
                wr.flush();
            }

            saveCookie(conn.getHeaderFields());
            if (is302(conn))
                return redirect(conn);
            return getResponseContent(conn);

        } catch (MalformedURLException e) {
            logger.info("url 解析失败");
            throw new HttpRequestException(e);
        } catch (FileNotFoundException e) {
            logger.info("request url not found http code 404 or 410");
            throw new HttpRequestException(e);
        } catch (IOException e) {
            logger.info("io 异常");
            throw new HttpRequestException(e);
        }
    }

    public URLConnection getImage(String url) {
        logger.info("send http get url:" + url);
        try {
            currentURL = url;
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            HttpUtils.setCommonHead(connection);
            setCookie(connection);
            // 建立实际的连接
            connection.connect();
            // 保存cookie
            saveCookie(connection.getHeaderFields());
            // 定义 BufferedReader输入流来读取URL的响应

            //if(is302(connection))
            //	return redirect(connection);
            return connection;
            //result = getResponseContent(connection);
        } catch (Exception e) {
            logger.info("发送GET请求出现异常" + e);
            e.printStackTrace();
            return null;
        }
        //	logger.info("url "+url+" response:"+result);
        //return result;
    }

    public String getPage(String url) {
        String result = "";
        logger.info("send http get url:" + url);
        try {
            currentURL = url;
            URL realUrl = new URL(url);

            if (url.startsWith("https"))
                HttpUtils.truseAllHttpsCert();
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            //HttpUtils.setCommonHead(connection);
            setHead(connection);
            setCookie(connection);
            // 建立实际的连接
            connection.connect();
            // 保存cookie
            saveCookie(connection.getHeaderFields());
            // 定义 BufferedReader输入流来读取URL的响应

            if (is302(connection))
                return redirect(connection);

            result = getResponseContent(connection);
        } catch (Exception e) {
            logger.info("发送GET请求出现异常" + e);
            e.printStackTrace();
        }
        logger.info("url " + url + " response:" + result);
        return result;
    }

    private boolean is302(URLConnection conn) {
        String field0 = conn.getHeaderField(0);
        return field0.indexOf("302") != -1;
    }

    private String redirect(URLConnection conn) {
        String location = conn.getHeaderField("Location");
        logger.info("request get 302 redirect to :" + location);
        return getPage(location);
    }

    private String getResponseContent(URLConnection conn) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "ISO-8859-1"))) {
            in.lines().forEach(sb::append);
        }
        return transferCharset(sb.toString());
    }


    private String transferCharset(String html) {
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

        logger.info("transfer charset:" + charset);
        byte[] bytes = html.getBytes(StandardCharsets.ISO_8859_1);
        try {
            return new String(bytes, charset);
        } catch (UnsupportedEncodingException e) {
            logger.info("unsuppoted  charset:" + charset + ",return as UTF-8");
            e.printStackTrace();
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }

    private void saveCookie(Map<String, List<String>> headMap) {
        for (String key : headMap.keySet()) {
            if ("Set-Cookie".equals(key)) {
                List<String> cookies = headMap.get(key);
                for (String str : cookies)
                    addCookie(str);
            }
        }
    }

    protected void addHead(String key, String value) {
        headMap.put(key, value);
    }

    protected void setCookie(String key, String value) {
        cookieMap.put(key, value);
    }

    protected void addCookie(String cookieStr) {
        String[] cookies = cookieStr.split(";");
        for (String cookie : cookies) {

            //将cookie从第一个=号分割为键值
            String[] cookieEntry = cookie.replaceFirst("=", ";").split(";");
            if (cookieEntry.length == 1) {
                logger.info("save single cookie " + cookieEntry[0]);
                cookieMap.put(cookieEntry[0], "");
            }
            if (cookieEntry.length >= 2) {
                logger.info("save cookie " + cookieEntry[0] + ":" + cookieEntry[1]);
                cookieMap.put(cookieEntry[0], cookieEntry[1]);
            }

        }
    }

    protected void setCookie(URLConnection conn) {
        StringBuilder sb = new StringBuilder();
        Set<Entry<String, String>> entrys = cookieMap.entrySet();
        for (Entry<String, String> entry : entrys) {
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            sb.append(";");
        }
        sb.append(cookie);
        logger.info("set cookie:" + sb);
        conn.setRequestProperty("Cookie", sb.toString());
    }


    public void setHead(URLConnection conn) {
        headMap.forEach((k, v) -> {
            conn.setRequestProperty(k, v);
        });
    }
}
