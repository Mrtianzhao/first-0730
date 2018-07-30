package net.dgg.cloud.service.Impl;

import cn.jpush.api.common.resp.APIConnectionException;
import net.dgg.cloud.constant.ApiConstants;
import net.dgg.cloud.dao.XdGeographyRecordsMapper;
import net.dgg.cloud.dao.XdSalesmanMapper;
import net.dgg.cloud.entity.XdGeographyRecords;
import net.dgg.cloud.entity.XdSalesman;
import net.dgg.cloud.service.XdGeographyRecordService;
import net.dgg.framework.redis.RedisFactory;
import net.dgg.framework.utils.PrimaryKeyUtils;
import net.dgg.framework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 骑手位置serviceImpl
 * @author
 */
@Service
public class XdGeographyRecordServiceImpl implements XdGeographyRecordService{

    private Logger log = LoggerFactory.getLogger(XdGeographyRecordServiceImpl.class);

    @Resource
    XdGeographyRecordsMapper xdGeographyRecordsMapper;

    @Resource
    XdSalesmanMapper salesmanMapper;

    /***
     * 骑手位置插入信息
     * @param record
     * @param token
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> addGeographyRecord(XdGeographyRecords record, String token) {
        Map<String, Object> map = new HashMap<String, Object>();

        try {

            if(StringUtils.isBlank(record.getLongitude())){
                map.put("code",1);
                map.put("msg","经度不能为空！");
                return map;
            }
            if(StringUtils.isBlank(record.getLatitude())){
                map.put("code",1);
                map.put("msg","纬度不能为空！");
                return map;
            }
            if(StringUtils.isBlank(record.getAddress())){
                map.put("code",1);
                map.put("msg","当前位置不能为空！");
                return map;
            }
            if(record.getLocDate() ==null){
                map.put("code",1);
                map.put("msg","定位时间不能为空!");
                return map;
            }

            JedisCluster jedisCluster = RedisFactory.getJedisCluster();
            String phone = jedisCluster.get(ApiConstants.TOKEN_PHONE + token);
            //通过手机号获取商务信息
            XdSalesman salesman = salesmanMapper.selectSalesmanByPhone(phone);

            if(salesman ==null){
                map.put("msg", "登录异常，请重新登录！");
                map.put("code", ApiConstants.LOGIN_ERROR_CODE);
                return map;
            }

            record.setId(PrimaryKeyUtils.getId());
            record.setCommerceId(salesman.getId());
            record.setLongitude(record.getLongitude());
            record.setLatitude(record.getLatitude());
            record.setAddress(record.getAddress());
            record.setLocTime(new Date(record.getLocDate()*1000));
            record.setCreateUserId(salesman.getId());
            record.setCreateTime(new Date());
            record.setUpdateUserId(salesman.getId());
            record.setUpdateTime(new Date());
            record.setDelFlag(0);

            /**插入骑手记录*/
            xdGeographyRecordsMapper.insert(record);

            map.put("code",0);
            map.put("msg","骑手位置插入成功！");

        }catch (Exception e){
            e.printStackTrace();
            map.put("code",1);
            map.put("msg","请求失败！");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return map;
    }
}
