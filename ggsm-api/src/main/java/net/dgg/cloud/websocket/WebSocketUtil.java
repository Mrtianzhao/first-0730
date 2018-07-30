package net.dgg.cloud.websocket;

import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


import net.dgg.framework.redis.RedisFactory;

import redis.clients.jedis.JedisCluster;

@Controller
public class WebSocketUtil {

	/**
	 * 
	 * @param sender_id 发送人
	 * @param receiver_Id 接收人
	 * @param title 标题
	 * @param content 内容
	 * @param template
	 * @throws Exception
	 */
	public static int send(String sender_id, String receiver_Id, String title, String content,
			SimpMessagingTemplate template) {
		String userId = String.valueOf(receiver_Id);
		JedisCluster cluster = RedisFactory.getJedisCluster();
		String sessionId = cluster.get(userId);
		if (sessionId != null && !sessionId.equals("")) {
			template.convertAndSendToUser(sessionId, "/topic/greetings", new OutMessage(content),
					createHeaders(sessionId));
			return 1;
		} else {
			return 0;
		}

	}

	private static MessageHeaders createHeaders(String sessionId) {
		SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
		headerAccessor.setSessionId(sessionId);
		headerAccessor.setLeaveMutable(true);
		return headerAccessor.getMessageHeaders();
	}

}
