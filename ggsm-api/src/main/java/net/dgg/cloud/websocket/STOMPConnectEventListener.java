package net.dgg.cloud.websocket;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import net.dgg.framework.utils.RedisUtils;

/**
 * STOMP监听类
 * 用于session注册 以及key值获取
 */
public class STOMPConnectEventListener  implements ApplicationListener<SessionConnectEvent> {


    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        String agentId = sha.getNativeHeader("login").get(0);
        String sessionId = sha.getSessionId();
        RedisUtils.set(agentId, sessionId);
        RedisUtils.expire(agentId, 180000);
    }
}
