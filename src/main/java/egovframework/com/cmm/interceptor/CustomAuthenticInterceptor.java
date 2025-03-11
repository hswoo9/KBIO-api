package egovframework.com.cmm.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import egovframework.com.devjitsu.model.common.SearchDto;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 인증여부 체크 인터셉터
 * @author 공통서비스 개발팀 서준식
 * @since 2011.07.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2011.07.01  서준식          최초 생성
 *  2011.09.07  서준식          인증이 필요없는 URL을 패스하는 로직 추가
 *  </pre>
 */

@Slf4j
public class CustomAuthenticInterceptor extends HandlerInterceptorAdapter {
	
	/**
	 * 세션에 계정정보(LoginVO)가 있는지 여부로 인증 여부를 체크한다.
	 * 계정정보(LoginVO)가 없다면, 로그인 페이지로 이동한다.
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		HttpSession session = request.getSession();
		log.debug("CustomAuthenticInterceptor sessionID "+session.getId());
		log.debug("CustomAuthenticInterceptor ================== ");

		// 요청 본문을 읽어들임
		StringBuilder sb = new StringBuilder();
		try (BufferedReader reader = request.getReader()) {
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			return true; // 오류가 발생하면 그대로 진행
		}

		// JSON 문자열을 Map으로 변환
		String jsonString = sb.toString();
		Map<String, Object> requestBodyMap = convertJsonToMap(jsonString);

		// SearchDto에 유동적인 값을 추가
		SearchDto searchDto = new SearchDto();
		for (Map.Entry<String, Object> entry : requestBodyMap.entrySet()) {
			searchDto.put(entry.getKey(), entry.getValue());
		}

		// SearchDto를 request에 설정하여 컨트롤러에서 사용할 수 있게 함
		request.setAttribute("searchDto", searchDto);

		return true;
	}

	private Map<String, Object> convertJsonToMap(String jsonString) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(jsonString, Map.class);
		} catch (JsonProcessingException e) {
			return new HashMap<>();
		}
	}
}