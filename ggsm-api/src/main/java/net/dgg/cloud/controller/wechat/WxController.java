package net.dgg.cloud.controller.wechat;

import net.dgg.cloud.constant.WxConstants;
import net.dgg.cloud.utils.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;


/**
 * @author ytz
 * @date 2018/5/15.
 * @desc 微信公众号
 */
@Controller
@RequestMapping(value = "/api/ggWx")
public class WxController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * token 认证
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public void index(HttpServletRequest request, HttpServletResponse response) {
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");
        String token = WxConstants.DIY_TOKEN;
        String[] arr = {token, timestamp, nonce};
        Arrays.sort(arr);

        StringBuffer stringBuffer = new StringBuffer();
        for (String item : arr) {
            stringBuffer.append(item);
        }
        String sha1Str = WechatUtil.sha1(stringBuffer.toString());
        try {
            if (signature.equals(sha1Str)) {
                response.getWriter().print(echostr);
                logger.info("token 认证成功！");
            } else {
                response.getWriter().print("");
                logger.info(" token 认证失败！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 事件推送
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @ResponseBody
    public void acceptPub(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        try {
            ServletInputStream inputStream = request.getInputStream();
            Map<String, String> stringMap = WechatUtil.parseXmlByInpuStream(inputStream);
            String msgType = stringMap.get("MsgType");
            String event = stringMap.get("Event");
            String fromUserName = stringMap.get("FromUserName");
            if ("event".equals(msgType) && WxConstants.SUB_EVENT.equals(event)) {
                logger.info(fromUserName + "：关注");
            } else if ("event".equals(msgType) && WxConstants.UNSUB_EVENT.equals(event)) {
                logger.info(fromUserName + "：取消关注");
            }
            logger.info("fromUserName" + fromUserName);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
