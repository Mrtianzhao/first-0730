package net.dgg.cloud.service;

import net.dgg.cloud.dto.XdProductOrderDto;

import java.util.Map;

/**
 * 生产订单信息service
 * @author 
 *
 */
public interface XdProductOrderService {

    /**
     * 根据资源id获取生产订单列表
     *
     * @param record
     * @param token
     * @return
     */
    Map<String, Object> queryXdProductOrderList(XdProductOrderDto record, String token);

    /**
     * 根据客户手机号查询订单信息
     *
     * @param dto
     * @param token
     * @return
     */
    Map<String, Object> queryXdProductOrderListByPhone(XdProductOrderDto dto, String token);
}
