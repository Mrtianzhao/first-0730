package net.dgg.cloud.controller;

import net.dgg.cloud.constant.ApiConstants;
import net.dgg.cloud.dto.XdContactDto;
import net.dgg.cloud.dto.XdResourceDto;
import net.dgg.cloud.dto.XdResourceOptDto;
import net.dgg.cloud.dto.XdResourcesBaseDto;
import net.dgg.cloud.entity.XdResourcesBase;
import net.dgg.cloud.service.XdResourcesBaseService;
import net.dgg.framework.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @date
 * @desc 资源信息controller
 */
@RestController
@RequestMapping("/resources")
public class XdResourcesBaseController {

    @Autowired
    private XdResourcesBaseService xdResourcesBaseService;

    /**
     * 获取资源信息列表
     *
     * @param dto
     * @param token
     * @return
     */
    @RequestMapping(value = "/getResources")
    @ResponseBody
    public Map<String, Object> getResourcesList(@RequestBody XdResourcesBaseDto dto, @RequestHeader String token) {
        Map<String,Object> retMap = new HashMap<>();
        if (StringUtils.isBlank(token)) {
            retMap.put("code", ApiConstants.LOGIN_ERROR_CODE);
            retMap.put("msg", "登录异常，请重新登录！");
            return retMap;
        }
        return xdResourcesBaseService.queryResourcesBaseList(dto, token);
    }

    /**
     * 接单、拒单、抢单、不再显示、剔除、反无效
     *
     * @param dto   (resourceId、optType) 操作类型：1接单，2拒单，3抢单，4不再显示 5.剔除 6反无效
     * @param token
     * @return
     */
    @RequestMapping(value = "/handleResources")
    @ResponseBody
    public Map<String, Object> handleResources(@RequestBody XdResourceOptDto dto, @RequestHeader String token) {
        Map<String, Object> retMap = new HashMap<>();
        String resourceId = dto.getResourceId();
        Integer optType = dto.getOptType();
        if (StringUtils.isBlank(resourceId) || optType == null) {
            retMap.put("code", -1);
            retMap.put("msg", "参数错误！");
            return retMap;
        }
        if (StringUtils.isBlank(token)) {
            retMap.put("code", ApiConstants.LOGIN_ERROR_CODE);
            retMap.put("msg", "登录异常，请重新登录！");
            return retMap;
        }

        int acceptCode = ApiConstants.ACCEPT_CODE;
        int refuseCode = ApiConstants.REFUSE_CODE;
        int scrambleCode = ApiConstants.SCRAMBLE_CODE;
        int hideCode = ApiConstants.HIDE_CODE;
        int removeCode = ApiConstants.REMOVE_CODE;
        int invalidCode = ApiConstants.INVALID_CODE;

        if (acceptCode != optType && refuseCode != optType && scrambleCode != optType && hideCode != optType && removeCode != optType && invalidCode != optType) {
            retMap.put("code", -1);
            retMap.put("msg", "非法请求！");
            return retMap;
        }

        if (optType == acceptCode) {
            retMap =  xdResourcesBaseService.acceptResource(dto,token);
        } else if (optType == refuseCode) {
            retMap = xdResourcesBaseService.refuseResource(dto,token);
        } else if (optType == scrambleCode) {
            retMap = xdResourcesBaseService.scrambleResource(dto,token);
        } else if (optType == hideCode) {
            retMap = xdResourcesBaseService.hideResource(dto,token);
        } else if (optType == removeCode) {
            retMap = xdResourcesBaseService.removeResource(dto,token);
        } else if (optType == invalidCode) {
            retMap = xdResourcesBaseService.invalidResources(dto,token);
        }
        return retMap;
    }

    /**
     * 获取订单状态
     */
    @RequestMapping(value = "/getStatus")
    @ResponseBody
    public Map<String, Object> getStatus() {
        return xdResourcesBaseService.queryStatus();
    }


    /**
     * 剔除资源信息
     */
    @RequestMapping(value = "/editByLostType")
    @ResponseBody
    public Map<String, Object> editByLostType(@RequestBody XdResourceOptDto dto, @RequestHeader String token) {
        return xdResourcesBaseService.removeResource(dto, token);
    }

    /**
     * 返无效资源信息
     */
    @RequestMapping(value = "/editByResourcesStatus")
    @ResponseBody
    public Map<String, Object> editByResourcesStatus(@RequestBody XdResourceOptDto record, @RequestHeader String token) {
        return xdResourcesBaseService.invalidResources(record, token);
    }

    /**
     * 通过资源id获取资源信息
     */
    @RequestMapping(value = "/getResourcesBaseInfo")
    @ResponseBody
    public Map<String, Object> queryResourcesBaseInfo(@RequestBody XdResourcesBase record, @RequestHeader String token) {
        return xdResourcesBaseService.queryResourcesBaseInfo(record, token);
    }

    /**
     * 拒单
     *
     * @param xdResourceOptDto (必传：resourceId、optType 操作类型(1接单，2拒单，3抢单，4不再显示))
     * @param token
     * @param request
     * @return
     */
    @RequestMapping(value = "/refuseR")
    @ResponseBody
    public Map<String, Object> refuseResource(@RequestBody XdResourceOptDto xdResourceOptDto, @RequestHeader String token, HttpServletRequest request) {
        Map<String, Object> retMap = new HashMap<>();
        if (StringUtils.isNullOrEmpty(token)) {
            token = request.getParameter("token");
        }
        if (StringUtils.isNullOrEmpty(token)) {
            retMap.put("code", -1);
            retMap.put("mag", "非法请求！");
            return retMap;
        }
        return xdResourcesBaseService.refuseResource(xdResourceOptDto, token);
    }

    /**
     * 接单 Scramble
     *
     * @param xdResourceOptDto (必传：resourceId、optType 操作类型(1接单，2拒单，3抢单，4不再显示))
     * @param token
     * @param request
     * @return
     */
    @RequestMapping(value = "/acceptR")
    @ResponseBody
    public Map<String, Object> acceptResource(@RequestBody XdResourceOptDto xdResourceOptDto, @RequestHeader String token, HttpServletRequest request) {
        Map<String, Object> retMap = new HashMap<>();
        if (StringUtils.isNullOrEmpty(token)) {
            token = request.getParameter("token");
        }
        if (StringUtils.isNullOrEmpty(token)) {
            retMap.put("code", -1);
            retMap.put("mag", "非法请求！");
            return retMap;
        }
        return xdResourcesBaseService.acceptResource(xdResourceOptDto, token);
    }

    /**
     * 抢单
     *
     * @param xdResourceOptDto (必传：resourceId、optType 操作类型(1接单，2拒单，3抢单，4不再显示))
     * @param token
     * @param request
     * @return
     */
    @RequestMapping(value = "/scrambleR")
    @ResponseBody
    public Map<String, Object> scrambleResource(@RequestBody XdResourceOptDto xdResourceOptDto, @RequestHeader String token, HttpServletRequest request) {
        Map<String, Object> retMap = new HashMap<>();
        if (StringUtils.isNullOrEmpty(token)) {
            token = request.getParameter("token");
        }
        if (StringUtils.isNullOrEmpty(token)) {
            retMap.put("code", -1);
            retMap.put("mag", "非法请求！");
            return retMap;
        }
        return xdResourcesBaseService.scrambleResource(xdResourceOptDto, token);
    }

    /**
     * 不再显示
     *
     * @param xdResourceOptDto (必传：resourceId、optType 操作类型(1接单，2拒单，3抢单，4不再显示))
     * @param token
     * @param request
     * @return
     */
    @RequestMapping(value = "/hideResource")
    @ResponseBody
    public Map<String, Object> hideResource(@RequestBody XdResourceOptDto xdResourceOptDto, @RequestHeader String token, HttpServletRequest request) {
        Map<String, Object> retMap = new HashMap<>();
        if (StringUtils.isNullOrEmpty(token)) {
            token = request.getParameter("token");
        }
        if (StringUtils.isNullOrEmpty(token)) {
            retMap.put("code", -1);
            retMap.put("mag", "非法请求！");
            return retMap;
        }
        return xdResourcesBaseService.hideResource(xdResourceOptDto, token);
    }


    /**
     * 修改主联系人
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/changeMain")
    @ResponseBody
    public Map<String,Object> changeMainContact(@RequestBody XdContactDto dto,@RequestHeader String token){
        return xdResourcesBaseService.changeMainContact(dto,token);
    }

    /**
     * 新增自开发资源
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/selfDev")
    @ResponseBody
    public Map<String,Object> selfDevelopment(@RequestBody XdResourceDto dto, @RequestHeader String token){
        return xdResourcesBaseService.selfDevelopment(dto,token);
    }

}
