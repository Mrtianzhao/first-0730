package net.dgg.system.controller.login;

import net.dgg.cloud.constant.ApiConstants;
import net.dgg.cloud.entity.XdSalesman;
import net.dgg.cloud.utils.ValidateCodeUtil;
import net.dgg.framework.PTConst;
import net.dgg.framework.utils.*;
import net.dgg.system.dto.LoginDto;
import net.dgg.system.service.XdSalesmanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ytz
 * @date 2018/4/28.
 * @desc 登录
 */
@Controller
@RequestMapping(value = "/api/login")
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private XdSalesmanService xdSalesmanService;

    /**
     * 获取验证码 (暂未用)
     *
     * @return
     */
    @RequestMapping(value = "/getCode")
    @ResponseBody
    public Map<String, Object> getCode(HttpServletRequest request) {
        String code = xdSalesmanService.getCode();
        request.getSession().setAttribute("login_code", code);
        request.getSession().setMaxInactiveInterval(5000);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("code", code);
        return JsonUtils.getResponseForMap(0, "SUCCESSS", dataMap);
    }

    /**
     * 获取图片验证码
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/getImgCode")
    @ResponseBody
    public /*void*/ Map<String, Object> getImgCode(@RequestBody LoginDto dto, HttpServletRequest request, HttpServletResponse response) {
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

        XdSalesman salesman = xdSalesmanService.getSalesmanByPhone(phoneNo);
        if (salesman == null) {
            retMap.put("code", -1);
            retMap.put("msg", "用户不存在！");
            return retMap;
        }
        Integer isOn = salesman.getIsOn();
        if (isOn == 1) {
            retMap.put("code", -1);
            retMap.put("msg", "已离职，禁止登录！");
            return retMap;
        }

        ValidateCodeUtil validateCodeUtil = new ValidateCodeUtil(80, 35, 4, 40);
        validateCodeUtil.createCode();
        BufferedImage bufImg = validateCodeUtil.getBuffImg();
        String imgCode = validateCodeUtil.getCode();
        logger.info("图形验证码： " + imgCode);

        // 禁止图像缓存
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        try {

            RedisUtils.set(ApiConstants.IMG_CODE + phoneNo, imgCode);
            RedisUtils.expire(ApiConstants.IMG_CODE + phoneNo, Integer.parseInt(RedisUtils.getRedisPriperty(PTConst.IMG_VERIFY_EXPIRE)));

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufImg, "jpeg", outputStream);

            BASE64Encoder encoder = new BASE64Encoder();
            String imgEncode = encoder.encode(outputStream.toByteArray());

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("imgCode", imgEncode);

            retMap.put("code", 0);
            retMap.put("msg", "获取图片验证码成功！");
            retMap.put("data", dataMap);
            // TODO 删除图形验证码
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
    @RequestMapping("/message")
    @ResponseBody
    public Map message(@RequestBody LoginDto loginDto, HttpServletRequest request) {
        Map map = new HashMap();
        String phoneNo = loginDto.getPhoneNo();
        String code = loginDto.getCode();
        logger.info("电话" + phoneNo);
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

        String loginCode = RedisUtils.get(ApiConstants.IMG_CODE + phoneNo);
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
            String msgCodeKey = ApiConstants.MSG_CODE_PREFIX + phoneNo;
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
            logger.info("短信验证码： " + phoneCode);
//            new Client().sendMsgBatch(phoneNo, smsContent.toString(), 1);
            BDShortMessage.shortMessage(phoneNo, smsContent.toString());
            RedisUtils.set(msgCodeKey, phoneCode);
            RedisUtils.expire(msgCodeKey, Integer.parseInt(RedisUtils.getRedisPriperty(PTConst.SMS_VERIFY_EXPIRE)));
            map.put("code", 0);
            map.put("msg", "发送成功");
            // todo 删除短信验证码
//            map.put("data", phoneCode);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", -1);
            map.put("msg", "系统异常！");
        }
        return map;
    }


    /**
     * 登录
     *
     * @param loginDto (必传：phoneNo、msgCode)
     * @param request
     * @return
     */
    @RequestMapping(value = "/signIn")
    @ResponseBody
    public Map<String, Object> login(@RequestBody LoginDto loginDto, HttpServletRequest request) {
        String phoneNo = loginDto.getPhoneNo();
        String msgCode = loginDto.getMsgCode();
        if (StringUtils.isBlank(phoneNo)) {
            return JsonUtils.getResponseForMap(-1, "请输入手机号码！", new HashMap<>());
        }
        if (StringUtils.isBlank(msgCode)) {
            return JsonUtils.getResponseForMap(-1, "请输入验证码！", new HashMap<>());
        }

        return xdSalesmanService.login(loginDto);
    }


    /**
     * 登出
     *
     * @param request
     * @param response
     * @param token
     * @return
     */
    @RequestMapping(value = "/signOut")
    @ResponseBody
    public Map<String, Object> signOut(HttpServletRequest request, HttpServletResponse response, @RequestHeader String token) {
        Map<String, Object> retMap = new HashMap<>();
        if (StringUtils.isBlank(token)) {
            retMap.put("code", -1);
            retMap.put("msg", "系统异常！");
            return retMap;
        }
        return xdSalesmanService.signOut(token);
    }

}
