package egovframework.com.devjitsu.controller.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.consult.TblDfclMttr;
import egovframework.com.devjitsu.model.terms.TblUtztnTrms;
import egovframework.com.devjitsu.model.user.QTblUser;
import egovframework.com.devjitsu.model.user.TblMvnEnt;
import egovframework.com.devjitsu.model.user.TblUser;
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
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
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
    @PostMapping(value = "/memberApi/checkMemberId.do")
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
    @PostMapping(value = "/memberApi/insertMember.do")
    public ResultVO insertMember(HttpServletRequest request)throws Exception {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return memberApiService.insertMember(dto);
    }

    @Operation(
            summary = "회원가입시 입주기업/유관기관 조회",
            description = "입주기업 유관기관 있는지 조회",
            tags = {"MemberController"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "입주기업/유관기관 조회 완료"),
            @ApiResponse(responseCode = "400", description = "입주기업/유관기관 조회 실패")
    })
    @PostMapping(value = "/memberApi/checkBusiness.do")
    public ResultVO checkBusiness(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return memberApiService.checkBusiness(dto);
    }

    @Operation(
            summary = "회원 ID 찾기",
            description = "회원 name, email가져와서 ID 찾기",
            tags = {"MemberController"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 ID 조회 완료"),
            @ApiResponse(responseCode = "400", description = "회원 ID 조회 실패")
    })
    @PostMapping(value = "/memberApi/findId.do")
    public ResultVO findId(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return memberApiService.findId(dto);
    }

    @Operation(
            summary = "회원 PASSWORD 찾기",
            description = "회원 id, name, email가져와서 이메일로 전달",
            tags = {"MemberController"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 패스워드 변경 완료"),
            @ApiResponse(responseCode = "400", description = "회원 패스워드 변경 실패")
    })
    @PostMapping(value = "/memberApi/findPassword.do")
    public ResultVO findPassword(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return memberApiService.findPassword(dto);
    }

    @Operation(
            summary = "마이페이지 회원 수정",
            description = "회원 수정",
            tags = {"MemberController"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 수정 완료"),
            @ApiResponse(responseCode = "400", description = "회원 수정 실패")
    })
    @PostMapping(value = "/memberApi/setMemberMyPageModfiy")
    public ResultVO setMemberMyPageModfiy(@ModelAttribute TblUser tbluser){
        return memberApiService.setMemberMyPageModfiy(tbluser);
    }

    @Operation(
            summary = "탈퇴회원 조회 ",
            description = "탈퇴회원 조회",
            tags = {"MemberController"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "탈퇴회원 조회 성공"),
            @ApiResponse(responseCode = "400", description = "탈퇴회원 조회 실패")
    })
    @PostMapping(value = "/memberApi/checkUser.do")
    public ResultVO checkUser(HttpServletRequest request) throws Exception {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return memberApiService.checkUser(dto);
    }
    @Operation(
            summary = "마이페이지 회원조회 ",
            description = "마이페이지 조회",
            tags = {"MemberController"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "마이페이지 조회 성공"),
            @ApiResponse(responseCode = "400", description = "마이페이지 조회 실패")
    })
    @PostMapping("/memberApi/getMyPageNormalMember")
    public ResultVO getMyPageNormalMember(@RequestBody TblUser tblUser) {
        return memberApiService.getMyPageNormalMember(tblUser);
    }

    @Operation(
            summary = "회원탈퇴",
            description = "회원탈퇴",
            tags = {"MemberController"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원탈퇴 성공"),
            @ApiResponse(responseCode = "400", description = "회원탈퇴 실패")
    })
    @PostMapping("/memberApi/myPageCancelMember")
    public ResultVO myPageCancelMember(@RequestBody TblUser tblUser){
        return memberApiService.myPageCancelMember(tblUser);
    }


    @Operation(
            summary = "마이페이지 애로사항 조회",
            description = "마이페이지 애로사항 조회",
            tags = {"MemberController"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "애로사항 조회 성공"),
            @ApiResponse(responseCode = "400", description = "애로사항 조회 실패")
    })
    @PostMapping("/memeberApi/getMypageDfclMttrList.do")
    public ResultVO getMypageDfclMttrList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return memberApiService.getMypageDfclMttrList(dto);
    }

    @Operation(
            summary = "마이페이지 애로사항 상세보기 조회",
            description = "마이페이지 애로사항 상세보기 조회",
            tags = {"MemberController"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "애로사항 상세보기 조회 성공"),
            @ApiResponse(responseCode = "400", description = "애로사항 상세보기 조회 실패")
    })
    @PostMapping("/memberApi/getDifficultiesDetail")
    public ResultVO getDifficultiesDetail(@RequestBody TblDfclMttr tblDfclMttr) {
        return memberApiService.getDifficultiesDetail(tblDfclMttr);
    }

    @PostMapping("/memberApi/setDifficultiesData")
    public ResultVO setDifficultiesData(
            @ModelAttribute TblDfclMttr tblDfclMttr,
            @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        return memberApiService.setDifficultiesData(tblDfclMttr, files);
    }

}