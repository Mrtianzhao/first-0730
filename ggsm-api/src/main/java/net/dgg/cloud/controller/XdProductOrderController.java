package net.dgg.cloud.controller;

import net.dgg.cloud.dto.XdProductOrderDto;
import net.dgg.cloud.entity.XdProductOrder;
import net.dgg.cloud.service.XdProductOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @date
 * @desc 查询生产订单信息controller
 *
 */
@RestController
@RequestMapping("/productOrder")
public class XdProductOrderController {

    @Autowired
    private XdProductOrderService xdProductOrderService;

    /**
     * 根据资源id获取生成订单信息
     */
    @RequestMapping(value = "/getXdProductOrderList")
    @ResponseBody
    public Map<String,Object> getXdProductOrderList(@RequestBody XdProductOrderDto record,@RequestHeader String token){
        return xdProductOrderService.queryXdProductOrderList(record,token);
    }

    /**
     * 根据资源id获取生成订单信息
     */
    @RequestMapping(value = "/getXdProductOrderListByPhone")
    @ResponseBody
    public Map<String,Object> getXdProductOrderListByPhone(@RequestBody XdProductOrderDto dto, @RequestHeader String token){
        return xdProductOrderService.queryXdProductOrderListByPhone(dto,token);
    }
}
