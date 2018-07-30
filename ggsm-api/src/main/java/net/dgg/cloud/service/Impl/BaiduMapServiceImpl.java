package net.dgg.cloud.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import net.dgg.cloud.constant.BaiduHawkEyeConstants;
import net.dgg.cloud.dto.BaiDuGetDistanceResDto;
import net.dgg.cloud.dto.BaiduDistanceReqDto;
import net.dgg.cloud.service.BaiduMapService;
import net.dgg.cloud.utils.RestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @Author zhangyuzhu
 * @Description
 * @Created data 2018/7/13 11:09
 */
@Service
public class BaiduMapServiceImpl implements BaiduMapService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestUtil restUtil;


    @Override
    public BigDecimal getDistance(BaiduDistanceReqDto baiduDistanceReqDto) {
        BaiDuGetDistanceResDto baiDuGetDistanceResDto = null;
        try{
            ResponseEntity<String> entity = restUtil.sendGet(BaiduHawkEyeConstants.GETDISTANCE_URL,baiduDistanceReqDto.toParam());
            baiDuGetDistanceResDto = JSON.parseObject(entity.getBody(), new TypeReference<BaiDuGetDistanceResDto>() {});
        }catch (Exception e){
            logger.error("getDistance error",e);
            return null;
        }
        if(baiDuGetDistanceResDto.getStatus().intValue() == 0){
            return new BigDecimal(baiDuGetDistanceResDto.getDistance());
        }
        return null;
    }
}
