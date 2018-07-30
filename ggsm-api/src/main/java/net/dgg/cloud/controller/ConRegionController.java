package net.dgg.cloud.controller;


import net.dgg.cloud.entity.CloundVersionControl;
import net.dgg.cloud.entity.ConRegion;
import net.dgg.cloud.service.CloundVersionControlService;
import net.dgg.cloud.service.ConRegionService;
import net.dgg.framework.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/conRegion")
public class ConRegionController {

    @Autowired
    public ConRegionService conRegionService;
    @Autowired
    CloundVersionControlService cloundVersionControlService;

    /**
     * 查询省市
     */
    @RequestMapping(value = "/getByPid")
    @ResponseBody
    public Map<String, Object> getByPid(@RequestBody ConRegion conRegion, @RequestHeader String token){
        return conRegionService.selectByPid(conRegion,token);
    }


    /**
     * 获取最APP最新版本
     */
    @RequestMapping("/recentversion")
    @ResponseBody
    public Map<String, Object> recentversion(HttpServletRequest request) {
        //return cloudJournalService.recentversion(request);
        Map<String,String[]> map11 = request.getParameterMap();
        System.out.println(map11);
        String type=request.getParameter("appType");
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if (StringUtils.isNullOrEmpty(type)) {
                map.put("code", -2);
                map.put("msg", "版本类型不能为空");
                map.put("data", "");
                return map;
            }
            if (!(type.equals("ios") || type.equals("android") || type.equals("pc64") || type.equals("pc32"))) {
                map.put("code", -2);
                map.put("msg", "版本类型不正确");
                map.put("data", "");
                return map;
            }
            CloundVersionControl cloundVersionControl = cloundVersionControlService.selectNewest(type);
            map.put("code", 0);
            map.put("msg", "成功");
            map.put("data", cloundVersionControl);
        } catch (Exception e) {
            map.put("code", -2);
            map.put("msg", "系统异常！");
            map.put("data", "");
            e.printStackTrace();
        }
        return map;
    }

}
