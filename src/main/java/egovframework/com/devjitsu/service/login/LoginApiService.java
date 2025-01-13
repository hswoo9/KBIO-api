package egovframework.com.devjitsu.service.login;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.QTblComCdGroup;
import egovframework.com.devjitsu.model.login.LettnemplyrinfoVO;
import egovframework.com.devjitsu.model.login.LoginDto;
import egovframework.com.devjitsu.model.login.QLettnemplyrinfoVO;
import egovframework.com.devjitsu.model.user.QTblUserSnsCertInfo;
import egovframework.com.devjitsu.model.user.TblUserSnsCertInfo;
import egovframework.com.devjitsu.repository.common.TblComCdGroupRepository;
import egovframework.com.devjitsu.repository.common.TblComCdRepository;
import egovframework.com.devjitsu.repository.common.TblComFileRepository;
import egovframework.com.devjitsu.repository.login.LettnemplyrinfoRepository;
import egovframework.let.utl.sim.service.EgovFileScrty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginApiService {

    @Value("${Globals.google.clientId}")
    private String googleClientId;

    @Value("${Globals.google.clientPassword}")
    private String googleClientSecret;

    @Value("${Globals.google.redirectUri}")
    private String googleRedirectUri;

    private final EntityManager em;
    private final LettnemplyrinfoRepository lettnemplyrinfoRepository;
    /**
     * jpa 부등호
     * gt : >
     * lt : <
     * goe : >=
     * loe : <=
     */
    /**
     *  query DSL 조건 추가하는 방법
     *  BooleanBuilder builder = new BooleanBuilder();
     *  builder.and(qTblComCdGroup.actvtnYn.eq("Y"));
     * */

    public LettnemplyrinfoVO actionLogin(LoginDto dto) throws Exception {
        QLettnemplyrinfoVO qLettnemplyrinfoVO = QLettnemplyrinfoVO.lettnemplyrinfoVO;
        JPAQueryFactory q = new JPAQueryFactory(em);
        BooleanBuilder builder = new BooleanBuilder();
        LettnemplyrinfoVO loginVO = new LettnemplyrinfoVO();

        if(dto.getLoginType().equals("base")){
            builder.and(qLettnemplyrinfoVO.emplyrId.eq(dto.getId()));
            builder.and(qLettnemplyrinfoVO.password.eq(EgovFileScrty.encryptPassword(dto.getPassword(), dto.getId())));

            loginVO = q.selectFrom(qLettnemplyrinfoVO).where(builder).fetchOne();
        }else{
            QTblUserSnsCertInfo qTblUserSnsCertInfo = QTblUserSnsCertInfo.tblUserSnsCertInfo;
            if(dto.getSnsType().equals("naver")){

            }else if(dto.getSnsType().equals("kakao")){

            }else if(dto.getSnsType().equals("google")){
                googleLoginAction(dto);
            }

            /** join 추가해야함 */
            builder.and(qTblUserSnsCertInfo.snsClsf.eq(dto.getSnsType()));
            builder.and(qTblUserSnsCertInfo.snsUnqNo.eq(dto.getSnsId()));
            TblUserSnsCertInfo tblUserSnsCertInfo = q.selectFrom(qTblUserSnsCertInfo).where(builder).fetchFirst();
        }

        return loginVO;
    }


    /**
     * google 로그인 체크
     * @return
     */
    public LoginDto googleLoginAction(LoginDto dto) {
        String authorizationCode = dto.getCode();
        String accessToken = getGoogleAccessToken(authorizationCode);

        if (accessToken != null) {
            String userInfo = getGoogleUserInfo(accessToken);
            if (userInfo != null) {
                Gson gson = new Gson();
                Map<String, Object> googleUserInfo = gson.fromJson(userInfo.toString(), new TypeToken<Map<String, Object>>() {}.getType());
                dto.setSnsId((String) googleUserInfo.get("sub"));
                dto.setStatusCode(ResponseCode.SUCCESS.getCode());
            }else{
                dto.setStatusCode(ResponseCode.AUTH_ERROR.getCode());
            }
        }else{
            dto.setStatusCode(ResponseCode.AUTH_ERROR.getCode());
        }

        return dto;
    }

    private String getGoogleAccessToken(String authorizationCode) {
        String tokenUrl = "https://oauth2.googleapis.com/token";

        // 요청 헤더 및 파라미터 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(tokenUrl)
                .queryParam("code", authorizationCode)
                .queryParam("client_id", googleClientId)
                .queryParam("client_secret", googleClientSecret)
                .queryParam("redirect_uri", googleRedirectUri)
                .queryParam("grant_type", "authorization_code");

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<?> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return (String) response.getBody().get("access_token");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getGoogleUserInfo(String accessToken) {
        String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    userInfoUrl,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody(); // 사용자 정보 반환
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
