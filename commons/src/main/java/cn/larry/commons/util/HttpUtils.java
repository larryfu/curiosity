package cn.larry.commons.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.net.URLConnection;

/**
 * @author larryfu
 */

public class HttpUtils {

    static Logger logger = LogManager.getLogger();

    public static void setCommonHead(URLConnection conn) {
        conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
        conn.setRequestProperty("Cache-Control", "max-age=0");
        conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.124 Safari/537.36");
    }

    /**
     * @throws HttpsTrustException 当信任失败
     */

    public static void truseAllHttpsCert() {
        SSLUtils.trustAllHttpsCertificates();
        HostnameVerifier hv = new HostnameVerifier() {
            @Override
            public boolean verify(String urlHostName, SSLSession session) {
                logger.warn("Warning: URL Host: " + urlHostName + " vs. "
                        + session.getPeerHost());
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }
}
