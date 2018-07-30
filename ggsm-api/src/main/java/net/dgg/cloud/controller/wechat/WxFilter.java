package net.dgg.cloud.controller.wechat;

import com.alibaba.fastjson.JSONObject;
import net.dgg.cloud.constant.WxConstants;
import net.dgg.cloud.utils.HttpClientUtil;
import net.dgg.framework.PTConst;
import net.dgg.framework.utils.ResourceUtils;
import net.dgg.framework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author ytz
 * @date 2018/5/19.
 * @desc 微信過濾器
 */
//@WebFilter(urlPatterns = {"/api/ggWx/bindPhone","/api/ggWx/getResourceList"},filterName = "WxFilter")
@WebFilter(urlPatterns = {"/api/ggWx/validate"},filterName = "WxFilter")
public class WxFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String fromUserName = (String) request.getSession().getAttribute(WxConstants.OPENID_KEY);
        if (StringUtils.isBlank(fromUserName)) {
            String agent = request.getHeader("User-Agent");
            logger.info("agent: " + agent);
            if (agent != null && agent.toLowerCase().indexOf(WxConstants.USER_AGENT) >= 0) {
                String code = request.getParameter("code");
                String state = request.getParameter("state");
                logger.info("code: " + code);
                logger.info("state: " + state);

                String appId = ResourceUtils.getResource("constants").getMap().get(PTConst.APPID_KEY);
                String appSecret = ResourceUtils.getResource("constants").getMap().get(PTConst.APP_SECRET_KEY);

                if (!StringUtils.isBlank(code) && !StringUtils.isBlank(state)) {
                    String url = WxConstants.AUTH_ACCESS_TOKEN_URL.replace("APPID",appId)
                            .replace("SECRET", appSecret)
                            .replace("CODE", code);
                    String json = HttpClientUtil.doGet(url);
                    logger.info("WxFilter: " + json);
                    String openid = (String) JSONObject.parseObject(json).get("openid");
                    request.getSession().setAttribute(WxConstants.OPENID_KEY, openid);
                    logger.info("openid: " + openid);
                } else {
                    String path = request.getRequestURL().toString();
                    String query = request.getQueryString();
                    if (!StringUtils.isBlank(query)) {
                        path = path + "?" + query;
                    }
                    try {
                        String url = WxConstants.CODE_URL.replace("APPID", appId)
                                .replace("REDIRECT_URI", URLEncoder.encode(path, "UTF-8"))
                                .replace("SCOPE", WxConstants.AUTH_SCOPE_BASE).replace("STATE", WxConstants.STATE);
                        logger.info("WxFilter重定向: " + url);
                        response.sendRedirect(url);
                        return;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

}
