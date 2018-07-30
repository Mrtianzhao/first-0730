package net.dgg.cloud.controller;

import net.dgg.cloud.entity.XdContacts;
import net.dgg.cloud.service.XdContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 联系人信息controller
 * @author
 */
@RestController
@RequestMapping("/contacts")
public class XdContactsController {

    @Autowired
    private XdContactsService xdContactsService;

    /**
     * 根据资源id获取联系人信息列表
     */
    @RequestMapping(value = "/getContactsList")
    @ResponseBody
    public Map<String,Object> getContactsList(@RequestBody XdContacts record,@RequestHeader String token){
        return xdContactsService.queryContactsList(record,token);
    }

    /**
     * 插入联系人信息
     */
    @RequestMapping(value = "/saveContactsInfo")
    @ResponseBody
    public Map<String,Object> saveContactsInfo(@RequestBody XdContacts record,@RequestHeader String token){
        return xdContactsService.addContactsInfo(record,token);
    }

}
