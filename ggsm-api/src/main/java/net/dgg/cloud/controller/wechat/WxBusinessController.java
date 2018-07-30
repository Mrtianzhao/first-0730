package net.dgg.cloud.controller.wechat;

import net.dgg.cloud.constant.ApiConstants;
import net.dgg.cloud.constant.WxConstants;
import net.dgg.cloud.dao.XdClueMapper;
import net.dgg.cloud.dto.WxUserDto;
import net.dgg.cloud.dto.XdProductOrderDto;
import net.dgg.cloud.entity.WxUser;
import net.dgg.cloud.service.WxUserService;
import net.dgg.cloud.service.XdClueService;
import net.dgg.cloud.service.XdProductOrderService;
import net.dgg.cloud.utils.ValidateCodeUtil;
import net.dgg.framework.PTConst;
import net.dgg.framework.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ytz
 * @date 2018/5/20.
 * @desc 微信业务
 */
@Controller
@RequestMapping(value = "/api/ggWx")
public class WxBusinessController {


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WxUserService wxUserService;

    @Autowired
    private XdProductOrderService xdProductOrderService;

    @Autowired
    private XdClueService clueService;

    /**
     * 获取图片验证码
     *
     * @return
     */
    @RequestMapping(value = "/getCode")
    @ResponseBody
    public Map<String, Object> getImgCode(@RequestBody WxUserDto dto, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> retMap = new HashMap<>();

        String phoneNo = dto.getPhoneNo();
        if (StringUtils.isBlank(phoneNo)) {
            retMap.put("code", -1);
            retMap.put("msg", "请输入手机号！");
            return retMap;
        }

        if (phoneNo.length() != 11) {
            retMap.put("code", -1);
            retMap.put("msg", "请输入正确的手机号！");
            return retMap;
        }

        ValidateCodeUtil validateCodeUtil = new ValidateCodeUtil(80, 35, 4, 40);
        validateCodeUtil.createCode();
        BufferedImage bufImg = validateCodeUtil.getBuffImg();
        String imgCode = validateCodeUtil.getCode();
        logger.info("validateCode :" + imgCode);

        // 禁止图像缓存
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        try {

            RedisUtils.set(ApiConstants.WX_IMG_CODE + phoneNo, imgCode);
            RedisUtils.expire(ApiConstants.WX_IMG_CODE + phoneNo, Integer.parseInt(RedisUtils.getRedisPriperty(PTConst.IMG_VERIFY_EXPIRE)));

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufImg, "jpeg", outputStream);

            BASE64Encoder encoder = new BASE64Encoder();
            String imgEncode = encoder.encode(outputStream.toByteArray());

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("imgCode", imgEncode);

            retMap.put("code", 0);
            retMap.put("msg", "获取图片验证码成功！");
            retMap.put("data", dataMap);
            // todo 删除
//            retMap.put("imgCode", imgCode);
        } catch (IOException e) {
            e.printStackTrace();
            retMap.put("code", -1);
            retMap.put("msg", "获取图片验证码失败！");
        }
        return retMap;
    }

    /**
     * 发送手机短信验证码 (必传：phoneNo,code)
     *
     * @return
     */
    @RequestMapping("/sendMsg")
    @ResponseBody
    public Map message(@RequestBody WxUserDto dto, HttpServletRequest request) {
        Map map = new HashMap();
        String phoneNo = dto.getPhoneNo();
        String code = dto.getCode();
        if (StringUtils.isBlank(phoneNo)) {
            map.put("code", -1);
            map.put("msg", "请输入手机号码！");
            return map;
        }
        if (phoneNo.length() != 11) {
            map.put("code", -1);
            map.put("msg", "手机号码格式不正确！");
            return map;
        }

        if (StringUtils.isNullOrEmpty(code) || StringUtils.isNullOrEmpty(code.trim())) {
            map.put("code", -1);
            map.put("msg", "请输入验证码！");
            return map;
        }

        String loginCode = RedisUtils.get(ApiConstants.WX_IMG_CODE + phoneNo);
        if (StringUtils.isNullOrEmpty(loginCode)) {
            map.put("code", -1);
            map.put("msg", "验证码失效，请重新获取！");
            return map;
        }
        if (!loginCode.equalsIgnoreCase(code)) {
            map.put("code", -1);
            map.put("msg", "验证码错误，请重新输入！");
            return map;
        }

        try {
            String msgCodeKey = ApiConstants.WX_MSG_CODE_PREFIX + phoneNo;
            String msgCode = RedisUtils.get(msgCodeKey);
            String phoneCode = "";
            if (StringUtils.isBlank(msgCode)) {
                phoneCode = String.valueOf((int) ((Math.random() * 9 + 1) * 1000));
            } else {
                phoneCode = msgCode;
            }

            StringBuffer smsContent = new StringBuffer();
            smsContent.append("【小顶上门】您的手机验证码为：").append(phoneCode);
            smsContent.append("，验证码有效期5分钟。");
            logger.info(" 验证码 :" + phoneCode);
//            new Client().sendMsgBatch(phoneNo, smsContent.toString(), 1);
            BDShortMessage.shortMessage(phoneNo, smsContent.toString());
            RedisUtils.set(msgCodeKey, phoneCode);
            RedisUtils.expire(msgCodeKey, Integer.parseInt(RedisUtils.getRedisPriperty(PTConst.SMS_VERIFY_EXPIRE)));
            map.put("code", 0);
            map.put("msg", "发送成功");
            // todo 删除
//            map.put("data", phoneCode);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", -1);
            map.put("msg", "系统异常！");
        }
        return map;
    }

    /**
     * 绑定手机号
     *
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/bindPhone")
    @ResponseBody
    public Map<String, Object> bindPhoneNo(@RequestBody WxUserDto dto, HttpServletRequest request) {
        Map<String, Object> retMap = new HashMap<>();
        String phoneNo = dto.getPhoneNo();
        String openId = dto.getOpenId();

        logger.info(phoneNo + " bindPhone ----> fromUserName " + openId);
        if (StringUtils.isBlank(openId)) {
            retMap.put("code", -1);
            retMap.put("msg", "未获得用户的微信号！");
            return retMap;
        }
        /*if (StringUtils.isBlank(fromUserName)) {
            retMap.put("code", -1);
            retMap.put("msg", "未获得用户的微信号！");
            return retMap;
        }*/
        return wxUserService.bindPhoneNo(dto);
    }

    @RequestMapping(value = "/validate")
    @ResponseBody
    public void validate(HttpServletRequest request, HttpServletResponse response) {
        String fromUserName = (String) request.getSession().getAttribute(WxConstants.OPENID_KEY);
        logger.info(" validate -----> openId " + fromUserName);
        WxUserDto dto = new WxUserDto();
        dto.setOpenId(fromUserName);
        Map<String, Object> retMap = wxUserService.validate(dto);
        int code = (int) retMap.get("code");
        String wxPageDomainUrl = ResourceUtils.getResource("constants").getMap().get(PTConst.WX_PAGE_PATH);
        String url;
        if (WxConstants.UN_BIND == code) {
            url = wxPageDomainUrl + WxConstants.BIND_URL + "?fromUserName=" + fromUserName;
            try {
                logger.info(code + " validate -----> " + url);
                response.sendRedirect(url);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (code == 0) {
            url = wxPageDomainUrl + WxConstants.LIST_URL + "?fromUserName=" + fromUserName;
            try {
                logger.info(code + " validate -----> " + url);
                response.sendRedirect(url);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取资源列表
     *
     * @param dto
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/getResourceList")
    @ResponseBody
    public Map<String, Object> getResourceListByPhone(@RequestBody WxUserDto dto, HttpServletRequest request, HttpServletResponse response, Model model) {
        String openId = dto.getOpenId();
        logger.info("getResourceList:openId " + openId);

        Map<String, Object> dataMap = wxUserService.getResourceList(dto, model);
        return dataMap;
    }

    /**
     * 资源详情
     *
     * @param dto      (resourceId)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/getOrderList")
    @ResponseBody
    public Map<String, Object> getResourceDetail(@RequestBody WxUserDto dto, HttpServletRequest request, HttpServletResponse response) {
        String resourceId = dto.getResourceId();
        Integer page = dto.getPage();
        Integer pageSize = dto.getPageSize();

        XdProductOrderDto xdProductOrderDto = new XdProductOrderDto();
        xdProductOrderDto.setResourceId(resourceId);
        xdProductOrderDto.setPage(page);
        xdProductOrderDto.setPageSize(pageSize);

        return xdProductOrderService.queryXdProductOrderList(xdProductOrderDto, "");
    }

    /**
     * 查看商务地址
     *
     * @param dto
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/getSalesmanLocation")
    @ResponseBody
    public Map<String, Object> getSalesmanLocation(@RequestBody WxUserDto dto, HttpServletRequest request, HttpServletResponse response) {
        return wxUserService.getSalesmanLocation(dto);
    }


    // V2 =====================================


    /**
     * 下单 获取图片验证码
     *
     * @return
     */
    @RequestMapping(value = "/getOrderCode")
    @ResponseBody
    public Map<String, Object> getImgCodeByOrder(@RequestBody WxUserDto dto, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> retMap = new HashMap<>();

        String phoneNo = dto.getPhoneNo();
        if (StringUtils.isBlank(phoneNo)) {
            retMap.put("code", -1);
            retMap.put("msg", "请输入手机号！");
            return retMap;
        }

        if (phoneNo.length() != 11) {
            retMap.put("code", -1);
            retMap.put("msg", "请输入正确的手机号！");
            return retMap;
        }

        ValidateCodeUtil validateCodeUtil = new ValidateCodeUtil(80, 35, 4, 40);
        validateCodeUtil.createCode();
        BufferedImage bufImg = validateCodeUtil.getBuffImg();
        String imgCode = validateCodeUtil.getCode();
        logger.info(" validateCode order : " + imgCode);

        // 禁止图像缓存
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        try {

            RedisUtils.set(ApiConstants.WX_ORDER_IMG_PREFIX + phoneNo, imgCode);
            RedisUtils.expire(ApiConstants.WX_ORDER_IMG_PREFIX + phoneNo, Integer.parseInt(RedisUtils.getRedisPriperty(PTConst.IMG_VERIFY_EXPIRE)));

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufImg, "jpeg", outputStream);

            BASE64Encoder encoder = new BASE64Encoder();
            String imgEncode = encoder.encode(outputStream.toByteArray());

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("imgCode", imgEncode);

            retMap.put("code", 0);
            retMap.put("msg", "获取图片验证码成功！");
            retMap.put("data", dataMap);
        } catch (IOException e) {
            e.printStackTrace();
            retMap.put("code", -1);
            retMap.put("msg", "获取图片验证码失败！");
        }
        return retMap;
    }

    /**
     * 下单 发送手机短信验证码 (必传：phoneNo,code)
     *
     * @return
     */
    @RequestMapping("/sendOrderMsg")
    @ResponseBody
    public Map sendMagOrder(@RequestBody WxUserDto dto) {
        Map map = new HashMap();
        String phoneNo = dto.getPhoneNo();
        logger.info("order: " + phoneNo);
        if (StringUtils.isBlank(phoneNo)) {
            map.put("code", -1);
            map.put("msg", "请输入手机号码！");
            return map;
        }
        if (phoneNo.length() != 11) {
            map.put("code", -1);
            map.put("msg", "手机号码格式不正确！");
            return map;
        }

        try {
            String msgCodeKey = ApiConstants.WX_ORDER_MSG_PREFIX + phoneNo;
            String msgCode = RedisUtils.get(msgCodeKey);
            String phoneCode;
            if (StringUtils.isBlank(msgCode)) {
                phoneCode = String.valueOf((int) ((Math.random() * 9 + 1) * 1000));
            } else {
                phoneCode = msgCode;
            }

            StringBuffer smsContent = new StringBuffer();
            smsContent.append("【小顶上门】您的手机验证码为：").append(phoneCode);
            smsContent.append("，验证码有效期5分钟。");
            logger.info("order:" + phoneNo + " -----> " + phoneCode);
            BDShortMessage.shortMessage(phoneNo, smsContent.toString());
            RedisUtils.set(msgCodeKey, phoneCode);
            RedisUtils.expire(msgCodeKey, Integer.parseInt(RedisUtils.getRedisPriperty(PTConst.SMS_VERIFY_EXPIRE)));
            map.put("code", 0);
            map.put("msg", "发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", -1);
            map.put("msg", "系统异常！");
        }
        return map;
    }

    /**
     * 微信 预约下单
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/reservation")
    @ResponseBody
    public Map<String, Object> reservation(@RequestBody WxUserDto dto) {
        return clueService.reservation(dto);
    }


}
