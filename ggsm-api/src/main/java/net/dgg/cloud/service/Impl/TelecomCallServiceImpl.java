package net.dgg.cloud.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import net.dgg.cloud.constant.ApiConstants;
import net.dgg.cloud.dao.XdSalesmanMapper;
import net.dgg.cloud.entity.XdSalesman;
import net.dgg.cloud.entity.call.TelecomCallDto;
import net.dgg.cloud.service.TelecomCallService;
import net.dgg.framework.utils.HttpUtils;
import net.dgg.framework.utils.RedisUtils;
import net.dgg.framework.utils.ResourceUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * V2
 *
 * @author ytz
 * @date 2018/7/23.
 * @desc
 */
@Service
public class TelecomCallServiceImpl implements TelecomCallService {

    private Logger logger = LoggerFactory.getLogger(TelecomCallServiceImpl.class);

    @Resource
    private XdSalesmanMapper salesmanMapper;


    @Override
    public Map<String, Object> telecomCall(TelecomCallDto dto, String token) {
        Map<String, Object> retMap = new HashMap<>();
        String customerPhone = dto.getCustomerPhone();
        if (StringUtils.isEmpty(token) || "".equals(token.trim())) {
            retMap.put("code", -1);
            retMap.put("msg", "非法请求！");
            return retMap;
        }
        String phoneNumber = RedisUtils.get(ApiConstants.TOKEN_PHONE + token);
        if (StringUtils.isEmpty(phoneNumber) || "".equals(phoneNumber.trim())) {
            retMap.put("code", -1);
            retMap.put("msg", "登录异常！");
            return retMap;
        }
        if (StringUtils.isEmpty(customerPhone) || "".equals(customerPhone.trim())) {
            retMap.put("code", -1);
            retMap.put("msg", "参数异常！");
            return retMap;
        }
        int phoneLen = 11;
        if (customerPhone.length() != phoneLen) {
            retMap.put("code", -1);
            retMap.put("msg", "参数异常【号码错误】！");
            return retMap;
        }

        XdSalesman salesman;
        try {
            salesman = salesmanMapper.selectSalesmanByPhone(phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("code", -1);
            retMap.put("msg", "系统异常！");
            return retMap;
        }
        if (salesman == null) {
            retMap.put("code", -1);
            retMap.put("msg", "商务状态异常！");
            return retMap;
        }

        Map<String, String> configMap = ResourceUtils.getResource(ApiConstants.CONFIG_FILE_NAME).getMap();
        String host = configMap.get(ApiConstants.TELECOM_AXB_HOST);
        String path = configMap.get(ApiConstants.TELECOM_AXB_PATH);

        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json;charset=utf-8");

        String employeeNo = salesman.getEmployeeNo();
        if (StringUtils.isEmpty(employeeNo) || "".equals(employeeNo.trim())) {
            retMap.put("code", -1);
            retMap.put("msg", "未设置工号！");
            return retMap;
        }

        HashMap<String, Object> reqParam = new HashMap<>();
        reqParam.put("userIdentity", employeeNo);
        reqParam.put("bNumber", customerPhone);

        String body = JSON.toJSONString(reqParam);
        try {

            logger.info("电信拨号地址：" + host + path);
            logger.info("电信拨号参数：" + JSON.toJSONString(reqParam, SerializerFeature.WriteMapNullValue));
            HttpResponse response = HttpUtils.doPost(host, path, null, headers, null, body);
            String str = EntityUtils.toString(response.getEntity(), "UTF-8");
            logger.info("电信拨号接口返回：" + str);

            Map dataMap = JSON.parseObject(str, Map.class);
            int code = Integer.parseInt(String.valueOf(dataMap.get("code")));
            String msg = String.valueOf(dataMap.get("msg"));
            if (code != 0) {
                retMap.put("code", -1);
                retMap.put("msg", "拨号失败【" + msg + "】！");
                return retMap;
            }
            return dataMap;
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("code", -1);
            retMap.put("msg", "拨号失败！");
            return retMap;
        }
    }
}
