package egovframework.com.devjitsu.service.login;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.cmm.util.EgovBasicLogger;
import egovframework.com.devjitsu.config.LoginUsers;
import egovframework.com.devjitsu.config.UserSessionBinding;
import egovframework.com.devjitsu.model.login.LettnemplyrinfoVO;
import egovframework.com.devjitsu.model.login.LoginDto;
import egovframework.com.devjitsu.model.login.QLettnemplyrinfoVO;
import egovframework.com.devjitsu.model.user.QTblUser;
import egovframework.com.devjitsu.model.user.QTblUserSnsCertInfo;
import egovframework.com.devjitsu.model.user.TblUser;
import egovframework.com.devjitsu.model.user.TblUserLgnHstry;
import egovframework.com.devjitsu.repository.login.LettnemplyrinfoRepository;
import egovframework.com.devjitsu.repository.user.TblUserLgnHstryRepository;
import egovframework.com.devjitsu.repository.user.TblUserRepository;
import egovframework.com.devjitsu.service.common.RedisApiService;
import egovframework.com.jwt.EgovJwtTokenUtil;
import egovframework.let.utl.sim.service.EgovFileScrty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@Slf4j
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

    @Value("${Globals.naver.clientId}")
    private String naverClintId;

    @Value("${Globals.naver.tokenUrl}")
    private String naverTokenUrl;

    @Value("${Globals.naver.infoUrl}")
    private String naverInfoUrl;

    @Value("${Globals.kakao.clientId}")
    private String kakaoClintId;

    @Value("${Globals.kakao.tokenUrl}")
    private String kakaoTokenUrl;

    @Value("${Globals.kakao.infoUrl}")
    private String kakaoInfoUrl;

    @Autowired
    private EgovJwtTokenUtil jwtTokenUtil;

    @Autowired
    private RedisApiService redisApiService;

    private final EntityManager em;
    private final TblUserRepository tblUserRepository;
    private final TblUserLgnHstryRepository tblUserLgnHstryRepository;
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

    public ResultVO actionLogin(LoginDto dto, HttpServletRequest request) throws Exception {
        ResultVO resultVO = new ResultVO();

        QTblUser qTblUser = QTblUser.tblUser;
        TblUser tblUser;

        JPAQueryFactory q = new JPAQueryFactory(em);
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qTblUser.actvtnYn.eq("Y"));

        /**ID확인*/
        if(dto.getLoginType().equals("base")){
            tblUser = q.selectFrom(qTblUser).where(qTblUser.userId.eq(dto.getId())).fetchOne();
        }else{
            QTblUserSnsCertInfo qTblUserSnsCertInfo = QTblUserSnsCertInfo.tblUserSnsCertInfo;
            if(dto.getSnsType().equals("naver")){
                naverLoginAction(dto);
            }else if(dto.getSnsType().equals("kakao")){
                kakaoLoginAction(dto);
            }else if(dto.getSnsType().equals("google")){
                googleLoginAction(dto);
            }

            /** join 추가해야함 */
            builder.and(qTblUserSnsCertInfo.snsClsf.eq(dto.getSnsType()));
            builder.and(qTblUserSnsCertInfo.snsUnqNo.eq(dto.getSnsId()));
            tblUser = q.select(qTblUser).from(qTblUserSnsCertInfo).join(qTblUser).on(qTblUserSnsCertInfo.userSn.eq(qTblUser.userSn)).where(builder).fetchFirst();
        }

        if(tblUser == null){
            if(dto.getLoginType().equals("base")){
                resultVO.setResultCode(ResponseCode.NOT_USER.getCode());
                resultVO.setResultMessage(ResponseCode.NOT_USER.getMessage());
            }else{
                resultVO.putResult("dto", dto);
                resultVO.setResultCode(ResponseCode.NOT_JOIN_USER.getCode());
                resultVO.setResultMessage(ResponseCode.NOT_JOIN_USER.getMessage());
            }
            return resultVO;
        }else {
            if(dto.getLoginType().equals("base")){
                if(tblUser.getMbrStts().equals("W")){
                    resultVO.setResultCode(ResponseCode.WAITING_FOR_APPROVAL.getCode());
                    resultVO.setResultMessage(ResponseCode.WAITING_FOR_APPROVAL.getMessage());
                    return resultVO;
                }else if(tblUser.getMbrStts().equals("R")){
                    resultVO.setResultCode(ResponseCode.REJECT_FOR_APPROVAL.getCode());
                    resultVO.setResultMessage(ResponseCode.REJECT_FOR_APPROVAL.getMessage());
                    return resultVO;
                }else if(tblUser.getMbrStts().equals("S")){
                    resultVO.setResultCode(ResponseCode.SUSPENSION_OF_USE.getCode());
                    resultVO.setResultMessage(ResponseCode.SUSPENSION_OF_USE.getMessage());
                    return resultVO;
                }if(tblUser.getMbrStts().equals("C")){
                    resultVO.setResultCode(ResponseCode.WITHDRAWN_USER.getCode());
                    resultVO.setResultMessage(ResponseCode.WITHDRAWN_USER.getMessage());
                    return resultVO;
                }else{
                    if(!tblUser.getUserPw().equals(EgovFileScrty.encryptPassword(dto.getPassword(), dto.getId()))){
                        long lgnFailNmtm = tblUser.getLgnFailNmtm() + 1;
                        tblUserRepository.setLgnFailNmtmUpd(tblUser.getUserSn(), lgnFailNmtm);

                        if(lgnFailNmtm == 5L){
                            tblUserRepository.setSuspensionOfUseUpd(tblUser.getUserSn());
                        }

                        resultVO.setResultCode(ResponseCode.NOT_EQ_PASSWORD.getCode());
                        resultVO.setResultMessage(ResponseCode.NOT_EQ_PASSWORD.getMessage());
                        return resultVO;
                    }
                }
            }


            /**
             * 중복로그인체크 ( 반영시 주석 해제 )
             */
//            String duplicateChk = redisApiService.getRedis(0, String.valueOf(tblUser.getUserSn())) == null ? "" : (String) redisApiService.getRedis(0, String.valueOf(tblUser.getUserSn()));
//            if(!StringUtils.isEmpty(duplicateChk)){
                /** 미사용

                if(dto.getConfirmPass().equals("N")){
                    resultVO.setResultCode(ResponseCode.DUPLICATE_LOGIN.getCode());
                    resultVO.setResultMessage(ResponseCode.DUPLICATE_LOGIN.getMessage());
                    return resultVO;
                }else{
                    redisApiService.delRedis(0, String.valueOf(tblUser.getUserSn()));
                }

                 */

//                resultVO.setResultCode(ResponseCode.DUPLICATE_LOGIN.getCode());
//                resultVO.setResultMessage(ResponseCode.DUPLICATE_LOGIN.getMessage());
//                return resultVO;
//            }

            String jwtToken = jwtTokenUtil.generateTokenJpa(tblUser);
            resultVO.putResult("userSn", tblUser.getUserSn());
            resultVO.putResult("mbrType", tblUser.getMbrType());
            resultVO.putResult("userId", tblUser.getUserId());
            resultVO.putResult("userName", tblUser.getKornFlnm());
            resultVO.putResult("userSe", tblUser.getUserSn() == 1 ? "ADM" : "UDR");
            resultVO.putResult("jToken", jwtToken);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
            resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());
            request.getSession().setAttribute("userSn", tblUser.getUserSn());

            TblUserLgnHstry tblUserLgnHstry = new TblUserLgnHstry();
            tblUserLgnHstry.setUserSn(tblUser.getUserSn());
            tblUserLgnHstryRepository.save(tblUserLgnHstry);

            redisApiService.setRedis(0, String.valueOf(tblUser.getUserSn()), jwtToken, null);
        }

        return resultVO;
    }

    public ResultVO actionLogout(TblUser tblUser, HttpServletRequest request, HttpServletResponse response) {
        ResultVO resultVO = new ResultVO();
        redisApiService.delRedis(0, String.valueOf(tblUser.getUserSn()));
        new SecurityContextLogoutHandler().logout(request, response, null);
        resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());
        return resultVO;

    }


    /**
     * google 로그인    시작
     * @return
     */
    public void googleLoginAction(LoginDto dto) {
        String authorizationCode = dto.getCode();
        String accessToken = getGoogleAccessToken(authorizationCode);

        if (accessToken != null) {
            String userInfo = getGoogleUserInfo(accessToken);
            if (userInfo != null) {
                Gson gson = new Gson();
                Map<String, Object> googleUserInfo = gson.fromJson(userInfo.toString(), new TypeToken<Map<String, Object>>() {}.getType());
                dto.setSnsId((String) googleUserInfo.get("sub"));
                dto.setTotalData(googleUserInfo);
                dto.setStatusCode(ResponseCode.SUCCESS.getCode());
            }else{
                dto.setStatusCode(ResponseCode.AUTH_ERROR.getCode());
            }
        }else{
            dto.setStatusCode(ResponseCode.AUTH_ERROR.getCode());
        }
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
        } catch (NullPointerException e) {
            return null;
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
        } catch (NullPointerException e) {
            return null;
        }
        return null;
    }

    /**
     * google 로그인 종료
     * @return
     */


    /**
     * naver 로그인 시작
     * @return
     */
    public void naverLoginAction(LoginDto dto){
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("client_id", naverClintId);
        map.add("client_secret", "7yAhvzbtMb");
        map.add("code", dto.getCode());
        map.add("state", dto.getState());

        HttpEntity<MultiValueMap<String, String>> naverTokenRequest = new HttpEntity<>(map, headers);
        // reqUrl로 Http 요청, POST 방식
        ResponseEntity<String> response = restTemplate.exchange(naverTokenUrl,
                HttpMethod.POST,
                naverTokenRequest,
                String.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            String responseBody = response.getBody();
            Gson gson = new Gson();
            Map<String, String> getUserInfo = gson.fromJson(responseBody, new TypeToken<Map<String, String>>() {}.getType());
            headers.setBearerAuth(getUserInfo.get("access_token"));
            naverTokenRequest = new HttpEntity<>(null, headers);
            ResponseEntity<String> infoResponse = restTemplate.exchange(naverInfoUrl,
                    HttpMethod.POST,
                    naverTokenRequest,
                    String.class);
            if (infoResponse.getStatusCode() == HttpStatus.OK && infoResponse.getBody() != null) {
                String infoResponseBody = infoResponse.getBody();
                Map<String, Object> naverUserInfo = gson.fromJson(infoResponseBody, new TypeToken<Map<String, Object>>() {}.getType());
                if(naverUserInfo != null){
                    Map<String, Object> resultMap = (Map<String, Object>) naverUserInfo.get("response");
                    dto.setSnsId((String) resultMap.get("id"));
                    dto.setTotalData(resultMap);
                    dto.setStatusCode(ResponseCode.SUCCESS.getCode());
                }else{
                    dto.setStatusCode(ResponseCode.AUTH_ERROR.getCode());
                }
            }else{
                dto.setStatusCode(ResponseCode.AUTH_ERROR.getCode());
            }
        }else {
            dto.setStatusCode(ResponseCode.AUTH_ERROR.getCode());
        }
    }
    /**
     * naver 로그인 종료
     * @return
     */

    /**
     * kakao 로그인 시작
     * @return
     */
    public void kakaoLoginAction(LoginDto dto){
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("client_id", kakaoClintId);
        map.add("code", dto.getCode());

        try {
            HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(map, headers);
            // reqUrl로 Http 요청, POST 방식
            ResponseEntity<String> response = restTemplate.exchange(kakaoTokenUrl,
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String responseBody = response.getBody();
                Gson gson = new Gson();
                Map<String, String> getUserInfo = gson.fromJson(responseBody, new TypeToken<Map<String, String>>() {}.getType());
                headers.setBearerAuth(getUserInfo.get("access_token"));
                kakaoTokenRequest = new HttpEntity<>(null, headers);
                ResponseEntity<String> infoResponse = restTemplate.exchange(kakaoInfoUrl,
                        HttpMethod.POST,
                        kakaoTokenRequest,
                        String.class);
                if (infoResponse.getStatusCode() == HttpStatus.OK && infoResponse.getBody() != null) {
                    String infoResponseBody = infoResponse.getBody();

                    JSONParser jsonParser = new JSONParser();
                    Object obj = jsonParser.parse(infoResponseBody);
                    JSONObject jsonObj = (JSONObject) obj;

                    Map<String, Object> resultInfo = getMapFromJsonObject(jsonObj);
                    if(resultInfo != null){
                        dto.setSnsId(resultInfo.get("id").toString());
                        dto.setTotalData(resultInfo);
                        dto.setStatusCode(ResponseCode.SUCCESS.getCode());
                    }else{
                        dto.setStatusCode(ResponseCode.AUTH_ERROR.getCode());
                    }
                }else{
                    dto.setStatusCode(ResponseCode.AUTH_ERROR.getCode());
                }
            }else{
                dto.setStatusCode(ResponseCode.AUTH_ERROR.getCode());
            }
        } catch (ParseException e) {
            log.error("ParseException");
        }
    }
    /**
     * kakao 로그인 종료
     */

    public static Map<String, Object> getMapFromJsonObject(JSONObject jsonObj){
        Map<String, Object> map = null;

        try {
            map = new ObjectMapper().readValue(jsonObj.toString(), Map.class);
        } catch (JsonParseException e) {
            // TODO Auto-generated catch block
            log.error("JsonParseException");
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            log.error("JsonMappingException");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            log.error("IOException");
        }
        return map;
    }
}
