package net.dgg.cloud.utils;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author zhangyuzhu
 * @Description restTemplate工具类
 * @Created data 2018/7/13 16:54
 */
@Component
public class RestUtil {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestTemplate restTemplate;


    /**
     * 发送get请求
     * @param url
     * @param param 参数键值对
     * @return
     */
    public ResponseEntity<String> sendGet(String url,HashMap<String,Object> param){
        StringBuffer allUrl = new StringBuffer();
        allUrl.append(url)
                .append("?")
                .append(this.mapToStringParam(param));
        logger.info("请求url为：{}",allUrl.toString());
        ResponseEntity<String> entity = restTemplate.getForEntity(allUrl.toString(), String.class);
        logger.info("请求结果为：{}",entity.toString());
        return entity;
    }


    /**
     * 将hashMap键值对拼接成url参数(适用于get方法)
     * @param paramMap
     * @return
     */
    private String mapToStringParam(HashMap<String,Object> paramMap){
        if(CollectionUtils.isEmpty(paramMap)){
            return "";
        }
        StringBuffer param = new StringBuffer();
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            entry.getKey();
            entry.getValue();
            param.append(entry.getKey()).append("=").append(entry.getValue().toString()).append("&");
        }
        String paramStr = param.toString();
        //去掉末尾的 & 符号
        return paramStr.substring(0,paramStr.length()-1);
    }

    /**
     * 发送post请求
     * @param url
     * @param param 参数键值对
     * @return
     */
    public ResponseEntity<String> sendPost(String url,HashMap<String,Object> param) throws Exception{
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        logger.info("请求url为：{}",url.toString());
        MultiValueMap<String, Object> map= new LinkedMultiValueMap<String, Object>();
        if(!CollectionUtils.isEmpty(param)){
            logger.info("请求参数为：{}",JSON.toJSONString(param));
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                map.add(entry.getKey(), entry.getValue());
            }
        }else{
            logger.info("无请求参数");
        }
        HttpEntity httpEntity = new HttpEntity(map,headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, httpEntity,String.class);
        logger.info("请求结果为："+ responseEntity.toString());
        return responseEntity;
    }


}
