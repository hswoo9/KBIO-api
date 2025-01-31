package egovframework.com.cmm;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import egovframework.com.devjitsu.model.common.MessageDto;
import egovframework.com.devjitsu.model.login.LettnemplyrinfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private Map<String, WebSocketSession> users = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String senderId = getMemberId(session);
        if(senderId != null){
            users.put(senderId, session);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String senderId = getMemberId(session);
        if (senderId != null) {
            users.remove(senderId);  // 세션 종료 시 세션 제거
        }
        super.afterConnectionClosed(session, status);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String msg = message.getPayload();

        if(msg != null){
            Gson gson = new Gson();
            MessageDto messageDto = gson.fromJson(msg, new TypeToken<MessageDto>() {}.getType());
            if(messageDto.getSendType().equals("all")){
                for(WebSocketSession user : users.values()){
                    if(user != null) {
                        TextMessage tmpMsg = new TextMessage(gson.toJson(messageDto));
                        user.sendMessage(tmpMsg);
                    }
                }
            }else{
                if(messageDto.getUserSn() != null){
                    String target = messageDto.getUserSn();
                    WebSocketSession targetSession = users.get(target);
                    if(targetSession != null) {
                        TextMessage tmpMsg = new TextMessage(gson.toJson(messageDto));
                        targetSession.sendMessage(tmpMsg);
                    }
                }
            }
        }
    }


    private String getMemberId(WebSocketSession session){
        Map<String, Object> httpSession = session.getAttributes();
        LettnemplyrinfoVO lettnemplyrinfoVO = (LettnemplyrinfoVO) httpSession.get("lettnemplyrinfoVO");

        if(lettnemplyrinfoVO == null) {
            return session.getId();
        } else {
            return String.valueOf(lettnemplyrinfoVO.getUserSn());
        }
    }
}
