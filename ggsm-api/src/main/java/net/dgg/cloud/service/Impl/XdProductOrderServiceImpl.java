package net.dgg.cloud.service.Impl;

import net.dgg.cloud.dao.XdOrderNodeMapper;
import net.dgg.cloud.dao.XdProductOrderMapper;
import net.dgg.cloud.dto.XdProductOrderDto;
import net.dgg.cloud.entity.XdOrderNode;
import net.dgg.cloud.entity.XdProductOrder;
import net.dgg.cloud.service.XdProductOrderService;
import net.dgg.framework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生产订单serviceImpl
 */
@Service
public class XdProductOrderServiceImpl implements XdProductOrderService {

    private Logger log = LoggerFactory.getLogger(XdProductOrderServiceImpl.class);

    @Resource
    XdProductOrderMapper xdProductOrderMapper;

    @Resource
    XdOrderNodeMapper xdOrderNodeMapper;

    /**
     * 根据资源id获取生产订单信息
     *
     * @param record
     * @param token
     * @return
     */
    @Override
    public Map<String, Object> queryXdProductOrderList(XdProductOrderDto record, String token) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        try {

            if (StringUtils.isBlank(record.getResourceId())) {
                retMap.put("code", 1);
                retMap.put("msg", "参数错误：资源id为空！");
                return retMap;
            }

            Integer page = record.getPage();
            Integer pageSize = record.getPageSize();
            if (page != null && pageSize != null) {
                page = page < 0 ? 1 : page;
                pageSize = pageSize < 0 ? 20 : pageSize;
                page = (page - 1) * pageSize;

                record.setPage(page);
                record.setPageSize(pageSize);
            }

            List<XdProductOrder> orderList = xdProductOrderMapper.selectXdProductOrderList(record);
            Integer count = xdProductOrderMapper.selectXdProductOrderCount(record);
            count = count == null ? 0 : count;

            List dataList = new ArrayList();
            if (orderList != null && !orderList.isEmpty()) {
                for (XdProductOrder item : orderList) {
                    XdOrderNode node = new XdOrderNode();
                    node.setOrderId(item.getId());
                    List<XdOrderNode> nodes = xdOrderNodeMapper.selectOrderNodeList(node);
                    item.setOrderNode(nodes);
                    dataList.add(item);
                }
            }

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("orders", dataList);
            dataMap.put("totalCount", count);

            retMap.put("code", 0);
            retMap.put("msg", "获取订单信息成功！");
            retMap.put("data", dataMap);

        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("code", 1);
            retMap.put("msg", "请求失败！");
        }
        return retMap;
    }

    /**
     * 根据客户手机号查询订单信息
     *
     * @param dto
     * @param token
     * @return
     */
    @Override
    public Map<String, Object> queryXdProductOrderListByPhone(XdProductOrderDto dto, String token) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            if (StringUtils.isBlank(dto.getCustomerPhone())) {
                retMap.put("code", 1);
                retMap.put("msg", "客户电话不能为空！");
                return retMap;
            }

            Integer page = dto.getPage();
            Integer pageSize = dto.getPageSize();

            if (page != null && pageSize != null){
                page = page <= 0 ? 1 :page;
                pageSize = pageSize <= 0 ? 20 : pageSize;
                page = (page - 1) * pageSize;

                dto.setPage(page);
                dto.setPageSize(pageSize);

            }

            /**根据客户手机号查询订单信息*/
            List<XdProductOrder> orders = xdProductOrderMapper.selectXdProductOrderByPhone(dto);
            /**获取总条数**/
            int count = xdProductOrderMapper.selectCount(dto);
            if (orders != null && !orders.isEmpty()) {
                for (XdProductOrder xdProductOrder : orders) {
                    XdOrderNode node = new XdOrderNode();
                    node.setOrderId(xdProductOrder.getId());
                    List<XdOrderNode> nodes = xdOrderNodeMapper.selectOrderNodeList(node);
                    xdProductOrder.setOrderNode(nodes);
                }
                dataMap.put("pageSize", orders);
                dataMap.put("count", count);
                retMap.put("code", 0);
                retMap.put("msg", "获取订单信息成功！");
                retMap.put("data", dataMap);

            } else {
                retMap.put("code", 1);
                retMap.put("msg", "查询数据有误！");
                return retMap;
            }

        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("code", 1);
            retMap.put("msg", "请求失败！");
            return retMap;
        }
        return retMap;
    }
}
