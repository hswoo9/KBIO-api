package egovframework.com.cmm;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import egovframework.com.devjitsu.model.user.TblUser;
import egovframework.com.devjitsu.model.user.TblUserMsg;
import egovframework.com.devjitsu.repository.user.TblUserMsgRepository;
import egovframework.com.devjitsu.repository.user.TblUserRepository;
import egovframework.let.utl.fcc.service.EgovFormBasedFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private Map<String, WebSocketSession> users = new ConcurrentHashMap<>();

    private final TblUserRepository tblUserRepository;
    private final TblUserMsgRepository tblUserMsgRepository;

    public WebSocketHandler(TblUserRepository tblUserRepository, TblUserMsgRepository tblUserMsgRepository) {
        this.tblUserRepository = tblUserRepository;
        this.tblUserMsgRepository = tblUserMsgRepository;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        URI uri = session.getUri();

        String userSn = null;
        if (uri != null) {
            userSn = getQueryParam(uri, "userSn");
        }

        if (userSn != null) {
            users.put(userSn, session);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        URI uri = session.getUri();

        String userSn = null;
        if (uri != null) {
            userSn = getQueryParam(uri, "userSn");
        }

        if (userSn != null) {
            users.remove(userSn);
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
                List<TblUser> tblUsers = tblUserRepository.findAll();

                for(TblUser tblUser : tblUsers){
                    if(tblUser != null) {
                        TblUserMsg saveMsg = getTblUserMsg(tblUser, tblUserMsg);
                        tblUserMsgRepository.save(saveMsg);

                        WebSocketSession targetSession = users.get(tblUser.getUserSn().toString());
                        if(targetSession != null) {
                            TextMessage tmpMsg = new TextMessage(gson.toJson(saveMsg));
                            targetSession.sendMessage(tmpMsg);
                        }
                    }
                }
            }else{
                if(tblUserMsg.getRcptnUserSn() != null){
                    TblUser tblUser = tblUserRepository.findByUserSn(tblUserMsg.getRcptnUserSn());
                    tblUserMsg.setRcptnUserSn(tblUser.getUserSn());
                    tblUserMsg.setSndngYmd(EgovFormBasedFileUtil.getTodayString());
                    tblUserMsg.setCreatrSn(tblUserMsg.getDsptchUserSn());
                    tblUserMsgRepository.save(tblUserMsg);

                    String target = tblUserMsg.getRcptnUserSn().toString();
                    WebSocketSession targetSession = users.get(target);
                    if(targetSession != null) {
                        TextMessage tmpMsg = new TextMessage(gson.toJson(tblUserMsg));
                        targetSession.sendMessage(tmpMsg);
                    }
                }
            }
        }
    }

    private static TblUserMsg getTblUserMsg(TblUser tblUser, TblUserMsg tblUserMsg) {
        TblUserMsg saveMsg = new TblUserMsg();
        saveMsg.setSendType(tblUserMsg.getSendType());
        saveMsg.setDsptchUserSn(tblUserMsg.getDsptchUserSn());
        saveMsg.setRcptnUserSn(tblUser.getUserSn());
        saveMsg.setMsgTtl(tblUserMsg.getMsgTtl());
        saveMsg.setMsgCn(tblUserMsg.getMsgCn());
        saveMsg.setSndngYmd(EgovFormBasedFileUtil.getTodayString());
        saveMsg.setCreatrSn(tblUserMsg.getDsptchUserSn());
        return saveMsg;
    }

    private String getQueryParam(URI uri, String param) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(uri);
        return builder.build().getQueryParams().getFirst(param);
    }
}
