package net.dgg.cloud.utils;

import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ytz
 * @date 2018/5/15.
 * @desc
 */
public class WechatUtil {

    private static Logger logger = LogManager.getLogger(WechatUtil.class);

    /**
     * 全局数组
     */
    private final static String[] strDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * dom4j 解析 xml 转换为 map
     *
     * @param request
     * @return
     * @throws Exception
     */
    public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
        Map<String, String> retMap = new HashMap<>();
        InputStream inputStream = request.getInputStream();
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        Element root = document.getRootElement();
        List<Element> elementList = root.elements();

        // 遍历所有子节点
        for (Element e : elementList) {
            retMap.put(e.getName(), e.getText());
        }
        inputStream.close();
        return retMap;
    }

    /**
     * dom4j 解析 xml 转换为 map
     *
     * @param is
     * @return
     * @throws Exception
     */
    public static Map<String, String> parseXmlByInpuStream(InputStream is) throws Exception {
        Map<String, String> retMap = new HashMap<>();
        InputStream inputStream = is;
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        Element root = document.getRootElement();
        List<Element> elementList = root.elements();

        // 遍历所有子节点
        for (Element e : elementList) {
            retMap.put(e.getName(), e.getText());
        }
        inputStream.close();
        return retMap;
    }


    /**
     * @return 请求服务器接口 get
     * @throws IOException
     * @throws ClientProtocolException
     * @throws IOException
     * @String url
     */
    public static String getHttp(String url) throws ClientProtocolException, IOException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httget = new HttpGet(url);
        HttpResponse httpResp = httpclient.execute(httget);
        String charset = "UTF-8";
        String body = "";
        if (httpResp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            body = EntityUtils.toString(httpResp.getEntity(), charset);
        } else {
            logger.error("请求错误！");
        }
        logger.info("get请求----" + body);
        return body;
    }

    /**
     * @return 请求服务器接口 post
     * @throws IOException
     * @String url
     */
    public static String postHttp(String url, List<NameValuePair> nvps, String strMd5, long ttimestampr) throws ClientProtocolException, IOException {
        String time = Long.toString(ttimestampr);
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpost = new HttpPost(url);
        httpost.setHeader("tsignr", strMd5);
        httpost.setHeader("ttimestampr", time);
        String body = "";
        //long转字符串

        System.out.println(strMd5 + "-=-=-=--" + time);
        httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

        HttpResponse httpResp = httpclient.execute(httpost);

        if (httpResp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            body = EntityUtils.toString(httpResp.getEntity());
        } else {
            logger.error("请求错误！");
        }
        logger.info("post请求----" + body);
        return body;
    }

    /**
     * @param str
     * @return 字符串转json
     */
    public static JSONObject strToJson(String str) {
        return JSONObject.fromObject(str);
    }

    /**
     * @param str
     * @return 字符串转map
     */
    @SuppressWarnings("unchecked")
    public static Map strToMap(String str) {
        JSONObject jasonObject = JSONObject.fromObject(str);
        Map map = (Map) jasonObject;
        return map;
    }

    /**
     * 头部加密
     *
     * @param strs
     * @param currentTime
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String ArrToMd5(String[] strs, long currentTime) throws UnsupportedEncodingException {
        String sign = "";
        // 获取排序后参数拼接的str
        StringBuffer md5Str = new StringBuffer();
        String[] s = new String[strs.length];
        //md5分别加密
        for (int i = 0; i < strs.length; i++) {
            if (strs[i] != null && strs[i] != "") {
                strs[i] = URLEncoder.encode(strs[i], "UTF-8");
                strs[i] = strs[i].replace("+", "%20");
                s[i] = md5(strs[i]).toUpperCase();
            } else {
                s[i] = "";
            }
        }
        //加密后排序
        Arrays.sort(s);
        //拼接秘钥
        for (String str : s) {
            if (md5Str.length() == 0) {
                md5Str.append(str);
            } else {
                md5Str.append("&").append(str);
            }
        }
        md5Str.append("&tongr904&").append(currentTime);

        sign = md5(md5Str.toString()).toUpperCase();

        return sign;
    }

    /**
     * MD5转换
     *
     * @param str
     * @return
     */
    public static String md5(String str) {
        String sign = "";
        try {
            // 创建MD5算法的信息摘要   
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] results = md.digest(str.getBytes());
            sign = byteToString(results);
            // 将得到的字节数组变成字符串返回   
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return sign;
    }

    /**
     * 转换字节数组为16进制字串
     *
     * @param bByte
     * @return
     */
    public static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }

        return sBuffer.toString();
    }

    /**
     * 返回形式为数字跟字符串
     *
     * @param bByte
     * @return
     */
    public static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    /**
     * 日期类型转时间戳
     *
     * @param times
     * @return
     */
    public static long getTimeStamp(String times) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(times);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * long类型的时间戳转str类型
     *
     * @param times
     * @return
     */
    public static String dateToTimeStamp(String times) {
        if (times == "" || times == null) {
            return null;
        }
        Long longTimes = getTimeStamp(times);
        String strTimes = longTimes.toString();
        String subTimes = strTimes.substring(0, 10);

        return subTimes;
    }

    /**
     * 本方法封装了往前台设置的header,contentType等信息
     *
     * @param message  需要传给前台的数据
     * @param type     指定传给前台的数据格式,如"html","json"等
     * @param response HttpServletResponse对象
     * @throws IOException
     * @createDate 2010-12-31 17:55:41
     */
    public static void writeToWeb(String message, String type, HttpServletResponse response) throws IOException {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/" + type + "; charset=utf-8");
        response.getWriter().write(message);
        response.getWriter().close();
    }

    public static final List<String> ALLOW_TYPES = Arrays.asList(
            "image/jpg", "image/jpeg", "image/png", "image/gif"
    );

    /**
     * sha1
     *
     * @param str
     * @return
     */
    public static String sha1(String str){
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest digest = MessageDigest.getInstance("sha1");
            digest.update(str.getBytes());
            byte[] bytes = digest.digest();
            for (byte aByte : bytes) {
                sb.append(String.format("%02x", aByte));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 执行get请求
     */
    public static String httpGet(String url) throws Exception {
        // 创建执行对象
        HttpClient httpClient = new DefaultHttpClient();
        // 创建get请求对象
        HttpGet get = new HttpGet(url);
        //执行请求对象
        HttpResponse response = httpClient.execute(get);
        // 获取响应的数据
        HttpEntity entity = response.getEntity();
        // 解析
        String string = EntityUtils.toString(entity);
        return string;
    }

}
