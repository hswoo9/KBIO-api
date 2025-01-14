package egovframework.com.devjitsu.controller.login;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.login.LettnemplyrinfoVO;
import egovframework.com.devjitsu.model.login.LoginDto;
import egovframework.com.devjitsu.service.common.CommonApiService;
import egovframework.com.devjitsu.service.login.LoginApiService;
import egovframework.com.jwt.EgovJwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@Tag(name="LoginController",description = "로그인 관련")
public class LoginApiController {

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
    @PostMapping(value = "/loginApi/loginAction")
    public ResultVO loginAction(@RequestBody LoginDto loginDto, HttpServletRequest request, ModelMap model) throws Exception {
        return loginApiService.actionLogin(loginDto);
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
    @PostMapping(value = "/loginApi/logoutAction")
    public ResultVO logoutAction(@RequestBody LettnemplyrinfoVO lettnemplyrinfoVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return loginApiService.actionLogout(lettnemplyrinfoVO, request, response);
    }

}
