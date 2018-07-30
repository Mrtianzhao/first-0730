package net.dgg.cloud.websocket;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.dgg.framework.redis.RedisFactory;
import redis.clients.jedis.JedisCluster;

/**
 * 控制器
 */
@Controller
public class GreetingController {
    /**消息发送工具*/
    @Autowired
    private SimpMessagingTemplate template;

    /**
     * 用户广播
     * 发送消息广播  用于内部发送使用
     * @param request
     * @return
     */
    @GetMapping(value = "/msg/sendcommuser")
    public  @ResponseBody
    OutMessage SendToCommUserMessage(HttpServletRequest request){
//        List<String> keys=webAgentSessionRegistry.getAllSessionIds().entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
//        Date date=new Date();
//        keys.forEach(x->{
//            String sessionId=webAgentSessionRegistry.getSessionIds(x).stream().findFirst().get().toString();
//            template.convertAndSendToUser(sessionId,"/topic/greetings",new OutMessage("commmsg：allsend, " + "send  comm" +date.getTime()+ "!"),createHeaders(sessionId));
//        });
         return new OutMessage("sendcommuser, " + new Date() + "!");
    }




    /**
     * 同样的发送消息   只不过是ws版本  http请求不能访问
     * 根据用户key发送消息
     * @param message
     * @return
     * @throws Exception
     */
    @MessageMapping("/msg/hellosingle")
    public void greeting2(InMessage message) throws Exception {
    	
        //这里没做校验
        String[] str = message.getId().split(",");
        for(int i=0;i<str.length;i++){
        	JedisCluster cluster = RedisFactory.getJedisCluster();
		    String sessionId = cluster.get(str[i]);
		    if(sessionId!=null&&!sessionId.equals("")){
		    	template.convertAndSendToUser(sessionId,"/topic/greetings",new OutMessage("single send to："+message.getId()+", from:" + message.getName() + "!"),createHeaders(sessionId));
		    }
		    
        }
    }
    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

}
