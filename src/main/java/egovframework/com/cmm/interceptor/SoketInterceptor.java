package egovframework.com.cmm.interceptor;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.Map;

@Component
public class SoketInterceptor extends HttpSessionHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        HttpSession session = getSession(request);
        if (session != null) {
            if (isCopyHttpSessionId()) {
                attributes.put(HTTP_SESSION_ID_ATTR_NAME, session.getId());
            }
            Enumeration<String> names = session.getAttributeNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                if (isCopyAllAttributes() || getAttributeNames().contains(name)) {
                    attributes.put(name, session.getAttribute(name));
                }
            }
        }
        return true;
    }

    private HttpSession getSession(ServerHttpRequest request) {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
            return serverRequest.getServletRequest().getSession(isCreateSession());
        }
        return null;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception ex) {
    }
}
