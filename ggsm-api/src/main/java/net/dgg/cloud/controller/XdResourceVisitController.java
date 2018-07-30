package net.dgg.cloud.controller;

import net.dgg.cloud.constant.ApiConstants;
import net.dgg.cloud.dto.VisitRecordDto;
import net.dgg.cloud.dto.XdResourceVisitDto;
import net.dgg.cloud.entity.XdResourceVisit;
import net.dgg.cloud.service.XdResourceVisitService;
import net.dgg.framework.redis.RedisFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisCluster;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 上门信息controller
 *
 * @author
 */
@RestController
@RequestMapping("/resourceVisit")
public class XdResourceVisitController {

    @Autowired
    private XdResourceVisitService xdResourceVisitService;

    /**
     * 根据资源id获取上门信息列表
     */
    @RequestMapping(value = "/getResourceVisit")
    @ResponseBody
    public Map<String, Object> getResourceVisit(@RequestBody XdResourceVisitDto dto, @RequestHeader String token) {
        return xdResourceVisitService.queryResourceVisitList(dto, token);
    }

    /**
     * 插入上门信息
     */
    @RequestMapping(value = "/saveResourceVisitInfo")
    @ResponseBody
    public Map<String, Object> saveResourceVisitInfo(@RequestBody XdResourceVisitDto dto, @RequestHeader String token) {
        return xdResourceVisitService.addResourceVisitInfo(dto, token);
    }

    // V2 ========================================================================

    /**
     * 保存 / 修改上门记录
     *
     * @param dto
     * @param token
     * @return
     */
    @RequestMapping(value = "/saveVisitRecord")
    @ResponseBody
    public Map<String, Object> saveOrUpdateVisitRecord(@RequestBody VisitRecordDto dto, @RequestHeader String token) {
        return xdResourceVisitService.saveOrUpdateVisitRecord(dto,token);
    }

    public static void main(String[] args) {
//        JedisCluster jedisCluster = RedisFactory.getJedisCluster();
//        String token = jedisCluster.get(ApiConstants.PHONE_TOKEN + "15281451895");
//        System.out.println(token);
        // 崔宝文 1c642149-5bbc-44f9-bfaa-347e0c57769d

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date parse = simpleDateFormat.parse("2018-07-25 18:00:00");
            System.out.println(parse.getTime());
            System.out.println(System.currentTimeMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

}
