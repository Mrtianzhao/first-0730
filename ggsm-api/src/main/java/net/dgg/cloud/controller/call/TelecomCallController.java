package net.dgg.cloud.controller.call;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import net.dgg.cloud.entity.call.TelecomCallDto;
import net.dgg.cloud.service.TelecomCallService;
import net.dgg.cloud.utils.RestUtil;
import net.dgg.framework.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * V2
 *
 * @author ytz
 * @date 2018/7/23.
 * @desc 电信拨号
 */
@Controller
@RequestMapping(value = {"/telecom"})
public class TelecomCallController {

    @Autowired
    private TelecomCallService telecomCallService;

    /**
     * 电信拨号 绑定axb
     *
     * @param dto
     * @param token
     * @return
     */
    @RequestMapping(value = {"/call"})
    @ResponseBody
    public Map<String,Object> telecomCall(@RequestBody TelecomCallDto dto, @RequestHeader String token) {
        return this.telecomCallService.telecomCall(dto,token);
    }


}
