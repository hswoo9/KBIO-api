package egovframework.com.devjitsu.controller.member;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.service.common.CommonApiService;
import egovframework.com.devjitsu.service.member.MemberApiService;
import egovframework.com.jwt.EgovJwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import java.util.Collections;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@Tag(name="MemberController",description = "회원 관련")
public class MemberApiController {

    @Resource(name = "egovMessageSource")
    EgovMessageSource egovMessageSource;

    @Resource(name = "memberApiService")
    private MemberApiService memberApiService;

    @Resource(name = "commonApiService")
    private CommonApiService commonApiService;

    @Operation(
            summary = "회원 ID 중복 확인",
            description = "회원 ID가 중복되었는지 확인",
            tags = {"MemberController"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 ID 사용 가능"),
            @ApiResponse(responseCode = "400", description = "회원 ID 중복")
    })
    @PostMapping(value = "/memberApi/membercheckid.do")
    public ResultVO checkMemberId(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return memberApiService.checkMemberId(dto);
    }

    @Operation(
            summary = "회원가입 신청",
            description = "회원가입 신청하기",
            tags = {"MemberController"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 신청이 완료되었습니다."),
            @ApiResponse(responseCode = "400", description = "회원가입 신청 실패")
    })
    @PostMapping(value = "/memberApi/insertmember.do")
    public ResultVO insertMember(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return memberApiService.insertMember(dto);
    }
}