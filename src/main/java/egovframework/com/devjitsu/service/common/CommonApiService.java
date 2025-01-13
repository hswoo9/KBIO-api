package egovframework.com.devjitsu.service.common;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.querydsl.core.BooleanBuilder;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.QTblComCdGroup;
import egovframework.com.devjitsu.repository.common.TblComCdGroupRepository;
import egovframework.com.devjitsu.repository.common.TblComCdRepository;
import egovframework.com.devjitsu.repository.common.TblComFileRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class CommonApiService {

    private final EntityManager em;
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
    private final TblComCdRepository tblComCdRepository;
    private final TblComCdGroupRepository tblComCdGroupRepository;
    private final TblComFileRepository tblComFileRepository;

    @Value("${Api.naver.clintid}")
    private String NAVER_API_KEY;

    @Value("${Api.naver.tokenUrl}")
    private String NAVER_TOKEN_URL;

    @Value("${Api.naver.infoUrl}")
    private String NAVER_INFO_URL;

    @Value("${Api.kakao.clintid}")
    private String KAKAO_API_KEY;

    @Value("${Api.kakao.tokenUrl}")
    private String KAKAO_TOKEN_URL;

    @Value("${Api.kakao.infoUrl}")
    private String KAKAO_INFO_URL;


    public ResultVO getComCdGroupList(Map<String, Object> params) {
        ResultVO resultVO = new ResultVO();

        QTblComCdGroup qTblComCdGroup = QTblComCdGroup.tblComCdGroup;
        JPAQueryFactory q = new JPAQueryFactory(em);

        /** query DSL 조건 추가하는 방법 */
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qTblComCdGroup.actvtnYn.eq("Y"));

        resultVO.putResult("rs",  q.selectFrom(qTblComCdGroup).where(builder).orderBy(qTblComCdGroup.frstCrtDt.desc()).fetch());
        resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        return resultVO;
    }

    public ResultVO callNaverLoginApi(Map<String, Object> params){
        ResultVO resultVO = new ResultVO();
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("client_id", NAVER_API_KEY);
        map.add("client_secret", "7yAhvzbtMb");
        map.add("code", params.get("code") == null ? "" : params.get("code").toString());
        map.add("state", params.get("state") == null ? "" : params.get("state").toString());

        try {
            HttpEntity<MultiValueMap<String, String>> naverTokenRequest = new HttpEntity<>(map, headers);
            // reqUrl로 Http 요청, POST 방식
            ResponseEntity<String> response = restTemplate.exchange(NAVER_TOKEN_URL,
                    HttpMethod.POST,
                    naverTokenRequest,
                    String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String responseBody = response.getBody();
                Gson gson = new Gson();
                Map<String, String> getUserInfo = gson.fromJson(responseBody, new TypeToken<Map<String, String>>() {}.getType());
                headers.setBearerAuth(getUserInfo.get("access_token"));
                naverTokenRequest = new HttpEntity<>(null, headers);
                ResponseEntity<String> infoResponse = restTemplate.exchange(NAVER_INFO_URL,
                        HttpMethod.POST,
                        naverTokenRequest,
                        String.class);
                if (infoResponse.getStatusCode() == HttpStatus.OK && infoResponse.getBody() != null) {
                    String infoResponseBody = infoResponse.getBody();
                    Map<String, Object> naverUserInfo = gson.fromJson(infoResponseBody, new TypeToken<Map<String, Object>>() {}.getType());
                    if(naverUserInfo != null){
                        Map<String, Object> resultMap = (Map<String, Object>) naverUserInfo.get("response");
                        resultVO.putResult("info", resultMap);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVO;
    }

    public ResultVO callKakaoLoginApi(Map<String, Object> params){
        ResultVO resultVO = new ResultVO();
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("client_id", KAKAO_API_KEY);
        map.add("code", params.get("code") == null ? "" : params.get("code").toString());

        try {
            HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(map, headers);
            // reqUrl로 Http 요청, POST 방식
            ResponseEntity<String> response = restTemplate.exchange(KAKAO_TOKEN_URL,
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String responseBody = response.getBody();
                Gson gson = new Gson();
                Map<String, String> getUserInfo = gson.fromJson(responseBody, new TypeToken<Map<String, String>>() {}.getType());
                headers.setBearerAuth(getUserInfo.get("access_token"));
                kakaoTokenRequest = new HttpEntity<>(null, headers);
                ResponseEntity<String> infoResponse = restTemplate.exchange(KAKAO_INFO_URL,
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
                        System.out.println(resultInfo);
                        resultVO.putResult("info", resultInfo);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVO;
    }

    public static Map<String, Object> getMapFromJsonObject(JSONObject jsonObj){
        Map<String, Object> map = null;

        try {
            map = new ObjectMapper().readValue(jsonObj.toString(), Map.class);
        } catch (JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return map;
    }
}
