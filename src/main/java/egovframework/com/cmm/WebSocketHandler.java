package egovframework.com.cmm;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import egovframework.com.devjitsu.model.user.TblUserMsg;
import egovframework.com.devjitsu.repository.user.TblUserMsgRepository;
import egovframework.com.devjitsu.service.common.RedisApiService;
import egovframework.let.utl.fcc.service.EgovFormBasedFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private Map<String, WebSocketSession> users = new ConcurrentHashMap<>();

    private final TblUserMsgRepository tblUserMsgRepository;

    public WebSocketHandler(TblUserMsgRepository tblUserMsgRepository) {
        this.tblUserMsgRepository = tblUserMsgRepository;
    }

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
            TblUserMsg tblUserMsg = gson.fromJson(msg, new TypeToken<TblUserMsg>() {}.getType());
            if(tblUserMsg.getSendType().equals("all")){
                for(WebSocketSession user : users.values()){
                    if(user != null) {
                        TextMessage tmpMsg = new TextMessage(gson.toJson(tblUserMsg));
                        user.sendMessage(tmpMsg);
                    }
                }
            }else{
                if(tblUserMsg.getRcptnUserSn() != null){
                    String target = tblUserMsg.getRcptnUserSn().toString();
                    WebSocketSession targetSession = users.get(target);
                    if(targetSession != null) {
                        TextMessage tmpMsg = new TextMessage(gson.toJson(tblUserMsg));
                        targetSession.sendMessage(tmpMsg);
                    }
                }
            }

            tblUserMsg.setSndngYmd(EgovFormBasedFileUtil.getTodayString());
            tblUserMsg.setCreatrSn(tblUserMsg.getDsptchUserSn());
            tblUserMsgRepository.save(tblUserMsg);
        }
    }


    private String getMemberId(WebSocketSession session){
        Map<String, Object> httpSession = session.getAttributes();
        String userSn = String.valueOf(httpSession.get("userSn")) == null ? "" : String.valueOf(httpSession.get("userSn"));

        if(StringUtils.isEmpty(userSn)) {
            return session.getId();
        } else {
            return userSn;
        }
    }
}
