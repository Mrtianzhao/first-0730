package net.dgg.cloud.controller;

import net.dgg.cloud.constant.ApiConstants;
import net.dgg.cloud.dto.BaiduDistanceReqDto;
import net.dgg.cloud.dto.XdSalesmanDto;
import net.dgg.cloud.service.BaiduMapService;
import net.dgg.framework.utils.StringUtils;
import net.dgg.system.service.XdSalesmanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


/**
 * @author ytz
 * @date 2018/5/11.
 * @desc 商务
 */
@Controller
@RequestMapping(value = "/api/salesman")
public class XdSalesmanController {

    @Autowired
    private XdSalesmanService xdSalesmanService;

    @Autowired
    private BaiduMapService baiduMapService;

    /**
     * 统计商务数据
     *
     * @param dto
     * @param token
     * @return
     */
    @RequestMapping(value = "/countData")
    @ResponseBody
    public Map<String,Object> getSalesmanStatistics(@RequestBody XdSalesmanDto dto,@RequestHeader String token){
        Map<String,Object> retMap = new HashMap<>();
        if (StringUtils.isBlank(token)){
            retMap.put("code", ApiConstants.LOGIN_ERROR_CODE);
            retMap.put("msg","登录异常，请重新登录！");
            return retMap;
        }
        return xdSalesmanService.getSalesmanStatistics(dto,token);
    }

    /**
     * 修改状态
     *
     * @param dto
     * @param token
     * @return
     */
    @RequestMapping(value = "/changeState")
    @ResponseBody
    public Map<String,Object> changeState(@RequestBody XdSalesmanDto dto, @RequestHeader String token){
        Map<String,Object> retMap = new HashMap<>();
        if (StringUtils.isBlank(token)){
            retMap.put("code",ApiConstants.LOGIN_ERROR_CODE);
            retMap.put("msg","登录异常，请重新登录！");
            return retMap;
        }
        return xdSalesmanService.changeState(dto,token);
    }

    @RequestMapping(value = "/distance")
    @ResponseBody
    public BigDecimal getDistance() {
        BaiduDistanceReqDto dto = new BaiduDistanceReqDto();
        dto.setEntityName("0984b844-e83a-4126-987e-4e8d2d535c7e");
        //1532512800000
        //1532521671541
        dto.setStartTime(1532512800000L/1000);
        dto.setEndTime(1532521671541L/1000);
        return baiduMapService.getDistance(dto);
    }

}
