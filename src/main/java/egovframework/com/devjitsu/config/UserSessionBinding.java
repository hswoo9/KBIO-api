package egovframework.com.devjitsu.config;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.io.Serializable;

public class UserSessionBinding implements HttpSessionBindingListener, Serializable {

    private static final long serialVersionUID = 0L;
    private HttpSession session;
    private String sessionId;

    private final String userId;

    public UserSessionBinding(String userId){
        this.userId = userId;
    }

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        UserSessionBinding user = (UserSessionBinding)event.getValue();
        HttpSession session = event.getSession();
        user.sessionId = session.getId();
        user.session = session;

        LoginUsers.setUser(user.userId, user);
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        LoginUsers.removeUser(userId);
    }

    public HttpSession getSession() {
        return this.session;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public String getUserId() {
        return this.userId;
    }
}
