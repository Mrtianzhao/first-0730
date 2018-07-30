package net.dgg.cloud.service;

import net.dgg.cloud.dto.BaiduDistanceReqDto;

import java.math.BigDecimal;

/**
 * @Author zhangyuzhu
 * @Description 本接口为与百度地图api调用设置
 * @Created data 2018/7/12 17:33
 */
public interface BaiduMapService {

    /**
     *  根据时间到百度鹰眼查询某个entity移动距离
     * @param baiduDistanceReqDto
     * @return 如果报错或者查询不到距离返回null，否则返回距离(单位：米)
     */
    public BigDecimal getDistance(BaiduDistanceReqDto baiduDistanceReqDto);
}
