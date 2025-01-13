package egovframework.com.devjitsu.controller.login;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.login.LettnemplyrinfoVO;
import egovframework.com.devjitsu.model.login.LoginDto;
import egovframework.com.devjitsu.service.common.CommonApiService;
import egovframework.com.devjitsu.service.login.LoginApiService;
import egovframework.com.jwt.EgovJwtTokenUtil;
import egovframework.let.uat.uia.service.EgovLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@Tag(name="LoginController",description = "로그인 관련")
public class LoginController {

    /** JWT */
    @Autowired
    private EgovJwtTokenUtil jwtTokenUtil;

    @Resource(name = "egovMessageSource")
    EgovMessageSource egovMessageSource;

    @Resource(name = "loginApiService")
    private LoginApiService loginApiService;

    @Resource(name = "commonApiService")
    private CommonApiService commonApiService;

    @Operation(
            summary = "JWT 로그인",
            description = "JWT 로그인 처리",
            tags = {"LoginController"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "300", description = "로그인 실패")
    })
    @PostMapping(value = "/loginAction")
    public ResultVO loginAction(@RequestBody LoginDto loginDto, HttpServletRequest request, ModelMap model) throws Exception {
        ResultVO result = new ResultVO();
        LettnemplyrinfoVO loginResultVO = loginApiService.actionLogin(loginDto);

//        HashMap<String, Object> resultMap = new HashMap<String, Object>();
//
//        // 1. 일반 로그인 처리
//        LettnemplyrinfoVO loginResultVO = loginApiService.actionLogin(loginVO);
//
//        if (loginResultVO != null && loginResultVO.getEmplyrId() != null && !loginResultVO.getEmplyrId().equals("")) {
//
//            log.debug("===>>> loginVO.getUserSe() = "+loginVO.getUserSe());
//            log.debug("===>>> loginVO.getId() = "+loginVO.getId());
//            log.debug("===>>> loginVO.getPassword() = "+loginVO.getPassword());
//
//            String jwtToken = jwtTokenUtil.generateTokenJpa(loginResultVO);
//
//            String username = jwtTokenUtil.getUserSeFromToken(jwtToken);
//            log.debug("Dec jwtToken username = "+username);
//
//            request.getSession().setAttribute("LoginVO", loginResultVO);
//
//            resultMap.put("resultVO", loginResultVO);
//            resultMap.put("jToken", jwtToken);
//            resultMap.put("resultCode", "200");
//            resultMap.put("resultMessage", "성공 !!!");
//
//        } else {
//            resultMap.put("resultVO", loginResultVO);
//            resultMap.put("resultCode", "300");
//            resultMap.put("resultMessage", egovMessageSource.getMessage("fail.common.login"));
//        }

        return result;
    }



    /**
     * 로그아웃한다.
     * @return resultVO
     * @exception Exception
     */
    @Operation(
            summary = "로그아웃",
            description = "로그아웃 처리(JWT,일반 관계 없이)",
            security = {@SecurityRequirement(name = "Authorization")},
            tags = {"LoginController"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
    })
    @GetMapping(value = "/logoutAction")
    public ResultVO logoutAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ResultVO resultVO = new ResultVO();
        new SecurityContextLogoutHandler().logout(request, response, null);
        resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());
        return resultVO;
    }

    /**
     * 로그아웃한다.
     * @return resultVO
     * @exception Exception
     */
    @Operation(
            summary = "네이버 정보조회",
            description = "네이버 사용자 정보 조회",
            tags = {"LoginController"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정보조회 완료"),
    })
    @PostMapping(value = "/naver/callback")
    public ResultVO naverCallback(@RequestBody Map<String, Object> params) {
        ResultVO resultVO = new ResultVO();
        commonApiService.callNaverLoginApi(params);
        resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());
        return resultVO;
    }
}
