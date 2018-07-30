package net.dgg.framework.interceptor;

import net.dgg.cloud.constant.ApiConstants;
import net.dgg.framework.PTConst;
import net.dgg.framework.redis.RedisFactory;
import net.dgg.framework.redis.dao.RedisDao;
import net.dgg.framework.utils.JsonUtils;
import net.dgg.framework.utils.RedisUtils;
import net.dgg.framework.utils.SwapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录验证拦截器
 * Created by wu on 2017-09-04.
 */
public class AuthcInteceptor implements HandlerInterceptor {

    Logger logger = LoggerFactory.getLogger(AuthcInteceptor.class);

    @Resource
    RedisDao redisDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        // 用户token,从header和param中都取，使用不为空的token
        String token=null;
        if (request.getHeader(PTConst.USER_TOKEN)!=null){
            token=request.getHeader(PTConst.USER_TOKEN);
        }else {
            if (request.getParameter(PTConst.USER_TOKEN)!=null)
                token=request.getParameter(PTConst.USER_TOKEN);
        }
        logger.info("拦截器：" + token);
        // 请求资源路径
        String reqUrl = request.getRequestURL().toString();
        logger.info("请求地址：" + reqUrl);
        String servletPath = request.getServletPath();
        String[] urls = {"/api/zh/twoWayRedial","/api/login/getImgCode","/api/ggWx/validate","/api/login/message","/api/login/signIn","/api/ggWx/","/session/gettoken","/mobile/userlogin","/sysuser/login","/conRegion/recentversion","/file/"};
        for(String url : urls){
            if(servletPath.startsWith(url)){
                return true;
            }
        }
        logger.info(reqUrl);

        //TODO 这种例外应该以配置的形式存在，否则我们无法便利的管理例外的uri
        //TODO 非空的时候也应该验证token的有效性
        if (StringUtils.isEmpty(token)) {
            Map retMap = new HashMap();
            retMap.put(PTConst.RSP_CODE, -1030);
            retMap.put(PTConst.RSP_MSG, "用户未登录");
            PrintWriter printWriter = response.getWriter();
            printWriter.write(JsonUtils.obj2Json(retMap));
            printWriter.close();
            response.flushBuffer();
            response.setCharacterEncoding("UTF-8");
            return false;
        }else{
            if(!RedisUtils.exists(ApiConstants.TOKEN_PHONE + token)){
                Map retMap = new HashMap();
                retMap.put(PTConst.RSP_CODE, -1030);
                retMap.put(PTConst.RSP_MSG, "用户未登录");
                PrintWriter printWriter = response.getWriter();
                printWriter.write(JsonUtils.obj2Json(retMap));
                printWriter.close();
                response.flushBuffer();
                response.setCharacterEncoding("UTF-8");
                return false;
            }
        }

        String username =RedisFactory.getJedisCluster().get(ApiConstants.TOKEN_PHONE + token);
        SwapUtils.put("USER_NAME", username);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
