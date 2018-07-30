package net.dgg.cloud.controller;

import net.dgg.cloud.dto.XdResourcesFollowDto;
import net.dgg.cloud.entity.XdResourceFollow;
import net.dgg.cloud.service.XdResourceFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 跟进信息controller
 */
@RestController
@RequestMapping("/resourceFollow")
public class XdResourceFollowController {

    @Autowired
    private XdResourceFollowService xdResourceFollowService;

    /**
     * 获取跟进信息记录
     */
    @RequestMapping(value = "/getResourceFollow")
    @ResponseBody
    public Map<String,Object> getResourceFollow(@RequestBody XdResourcesFollowDto dto,@RequestHeader String token){
        return xdResourceFollowService.queryResourcesFollowList(dto,token);
    }

    /**
     * 插入跟进记录信息
     */
    @RequestMapping(value = "/saveResourceFollow")
    @ResponseBody
    public Map<String,Object> saveResourceFollow(@RequestBody XdResourcesFollowDto dto,@RequestHeader String token){
        return xdResourceFollowService.addResourceFollow(dto,token);
    }
}
