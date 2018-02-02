package poprice.wechat.util;

import com.google.common.base.Strings;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class NotifyUtils {
    private static final Logger log = LoggerFactory.getLogger(NotifyUtils.class);

    /**
     * 连接超时:0.5s
     */
    public static final int CONNECTION_TIMEOUT = 350;

    /**
     * 读取超时:0.5s
     */
    public static final int READ_TIMEOUT = 750;

    public static boolean notifyUrl(String url, String tradeNo, String mobile, boolean ok, String result, String username, String remoteCode, String remoteMessage, String key) {
        if (Strings.isNullOrEmpty(remoteCode)) {
            remoteCode = "";
        }
        if (Strings.isNullOrEmpty(remoteMessage)) {
            remoteMessage = "";
        }
        String finalUrl = null;
        if (!url.endsWith("?")) {
            if (!url.endsWith("&")) {
                url = url + "?";
            }
        }
        try {
            finalUrl = url + "tradeNo=" + tradeNo + "&mobile=" + mobile + "&ok=" + ok + "&result=" + result
                    + "&remoteCode=" + URLEncoder.encode(remoteCode, "UTF-8") + "&remoteMessage=" + URLEncoder.encode(remoteMessage, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("编码URL错误:{}/{}", remoteCode, remoteMessage);
            return false;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(tradeNo).append(mobile).append(ok).append(result).append(username).append(key);
        String signature = DigestUtils.md5Hex(sb.toString());
        finalUrl = finalUrl + "&signature=" + signature;

        //只试验1次,失败了不管了,还有定时任务去处理.
        for (int i = 0; i < 1; i++) {
            String text = checkUrl(finalUrl).toLowerCase();
            if (text.equals("")) {
                continue;
            }
            if (text.equals("true") || text.contains("true")) {
                return true;
            }
            if (text.equals("false") || text.contains("false")) {
                log.error("Notify failed 1:" + finalUrl);
                return false;
            }
        }
        log.error("Notify failed 2:" + finalUrl);
        return false;
    }

    public static String checkUrl(String urlvalue) {
        String inputLine = "";
        HttpURLConnection urlConnection = null;
        BufferedReader in = null;
        try {
            URL url = new URL(urlvalue);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(READ_TIMEOUT);

            StringBuilder sb = new StringBuilder();
            String line = null;
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            inputLine = sb.toString().trim();
        } catch (Exception e) {
            //log.error("Notify failed:" + urlvalue + ", Message:" + e.getMessage());
            log.error("Error Message:" + e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }

            if (urlConnection != null) {
                try {
                    urlConnection.disconnect();
                } catch (Exception e) {
                }
            }
        }

        return inputLine;
    }

    public static void main1(String[] args) {
        String url = "http://www.yolly.cn/third/lingdianFlow/Result.do?tradeNo=2016062400000000000000244438&mobile=18088244830&ok=true&result=s&remoteCode=&remoteMessage=&signature=849802705258be55acd9eb7dff21d692";
        url = "http://115.28.67.155/5A/Result.aspx?tradeNo=16062437056000637105&mobile=18946538531&ok=true&result=s&remoteCode=&remoteMessage=&signature=5098338507ea9edc3501dc0536fc0c44";
        String result = checkUrl(url);
        System.out.println(result);
    }
}
