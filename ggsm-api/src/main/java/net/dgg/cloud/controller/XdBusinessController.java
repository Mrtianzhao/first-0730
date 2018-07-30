package net.dgg.cloud.controller;



import net.dgg.cloud.service.XdBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 联系人信息controller
 * @author
 */
@RestController
@RequestMapping("business")
public class XdBusinessController {

    @Autowired
    private XdBusinessService xdBusinessService;

    /**
     * 根据资源id获取联系人信息列表
     */
    @RequestMapping(value = "getBusinessInfos")
    @ResponseBody
    public Map<String,Object> getBusinessInfos( @RequestHeader String token){
        return xdBusinessService.getBusinessInfos(token);
    }
}
