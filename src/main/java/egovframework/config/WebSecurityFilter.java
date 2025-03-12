package egovframework.config;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;
import org.owasp.esapi.ESAPI;

/**
 * 웹 애플리케이션 방화벽(WAF) 필터
 * - XSS, SQL 인젝션, 경로 순회 등 공격 차단
 */
@Component
@Order(1)
public class WebSecurityFilter extends OncePerRequestFilter {

    // XSS 공격 패턴
    private static final Pattern XSS_PATTERN = Pattern.compile(
        ".*[<>\"';]+.*|" +
        ".*script.*|" +
        ".*javascript:.*|" +
        ".*vbscript:.*|" +
        ".*onload=.*"
    );

    // SQL 인젝션 패턴
    private static final Pattern SQL_INJECTION_PATTERN = Pattern.compile(
        ".*[;].*|" +
        ".*(union|select|insert|update|delete|drop|alter).*|" +
        ".*'.*'.*"
    );

    // 경로 순회 패턴
    private static final Pattern PATH_TRAVERSAL_PATTERN = Pattern.compile(
        ".*\\.\\./.*|" +
        ".*\\.\\.\\\\.*|" +
        ".*%2e%2e%2f.*|" +
        ".*%2e%2e%5c.*"
    );

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        
        // 요청 검증
        if (!isValidRequest(request)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 요청입니다.");
            return;
        }

        // 보안 헤더 설정
        setSecurityHeaders(response);

        // 요청 및 응답 인코딩 설정
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 다음 필터로 전달
        filterChain.doFilter(request, response);
    }

    /**
     * 요청의 유효성을 검사합니다.
     */
    private boolean isValidRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String userAgent = request.getHeader("User-Agent");

        // URI 검사
        if (!isValidUri(uri)) {
            logger.warn("잘못된 URI가 감지되었습니다: " + uri);
            return false;
        }

        // 쿼리 스트링 검사
        if (queryString != null && !isValidQueryString(queryString)) {
            logger.warn("잘못된 쿼리 스트링이 감지되었습니다: " + queryString);
            return false;
        }

        // User-Agent 검사
        if (userAgent != null && !isValidUserAgent(userAgent)) {
            logger.warn("잘못된 User-Agent가 감지되었습니다: " + userAgent);
            return false;
        }

        return true;
    }

    /**
     * URI의 유효성을 검사합니다.
     */
    private boolean isValidUri(String uri) {
        if (uri == null) return false;

        // XSS 패턴 검사
        if (XSS_PATTERN.matcher(uri.toLowerCase()).matches()) {
            return false;
        }

        // 경로 순회 패턴 검사
        if (PATH_TRAVERSAL_PATTERN.matcher(uri.toLowerCase()).matches()) {
            return false;
        }

        // ESAPI를 사용한 추가 검증
        return ESAPI.validator().isValidInput("URI", uri, "HTTPParameterValue", 2000, false);
    }

    /**
     * 쿼리 스트링의 유효성을 검사합니다.
     */
    private boolean isValidQueryString(String queryString) {
        if (queryString == null) return true;

        // XSS 패턴 검사
        if (XSS_PATTERN.matcher(queryString.toLowerCase()).matches()) {
            return false;
        }

        // SQL 인젝션 패턴 검사
        if (SQL_INJECTION_PATTERN.matcher(queryString.toLowerCase()).matches()) {
            return false;
        }

        // ESAPI를 사용한 추가 검증
        return ESAPI.validator().isValidInput("QueryString", queryString, "HTTPParameterValue", 2000, false);
    }

    /**
     * User-Agent의 유효성을 검사합니다.
     */
    private boolean isValidUserAgent(String userAgent) {
        if (userAgent == null) return true;

        // XSS 패턴 검사
        if (XSS_PATTERN.matcher(userAgent.toLowerCase()).matches()) {
            return false;
        }

        // ESAPI를 사용한 추가 검증
        return ESAPI.validator().isValidInput("UserAgent", userAgent, "HTTPHeaderValue", 1000, false);
    }

    /**
     * 보안 헤더를 설정합니다.
     */
    private void setSecurityHeaders(HttpServletResponse response) {
        // XSS 방지
        response.setHeader("X-XSS-Protection", "1; mode=block");
        
        // MIME 스니핑 방지
        response.setHeader("X-Content-Type-Options", "nosniff");
        
        // 클릭재킹 방지
        response.setHeader("X-Frame-Options", "DENY");
        
        // HSTS
        response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
        
        // 참조자 정책
        response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
        
        // CSP
        response.setHeader("Content-Security-Policy", 
            "default-src 'self'; " +
            "script-src 'self' 'unsafe-inline' 'unsafe-eval'; " +
            "style-src 'self' 'unsafe-inline'; " +
            "img-src 'self' data: https:; " +
            "font-src 'self'; " +
            "connect-src 'self'"
        );
    }
}
