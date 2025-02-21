package egovframework.com.devjitsu.controller.member;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.consult.*;
import egovframework.com.devjitsu.model.terms.TblUtztnTrms;
import egovframework.com.devjitsu.model.user.*;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    /*
    @PostMapping(value = "/memberApi/insertMember.do")
    public ResultVO insertMember(HttpServletRequest request)throws Exception {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return memberApiService.insertMember(dto);
    }*/
    @PostMapping(value = "/memberApi/insertMember")
    public ResultVO insertMember(@RequestParam(value = "userInfo") String userInfoJson,
                                 @RequestParam(value = "certInfo", required = false) String certInfoJson,
                                 @RequestParam(value = "careerInfo", required = false) String careerInfoJson,
                                 @RequestParam(value = "acbgInfo", required = false) String acbgInfoJson,
                                 @RequestParam(value = "profileImgFiles", required = false) List<MultipartFile> cnsltProfileFiles,
                                 @RequestParam(value = "certFiles", required = false)List<MultipartFile> certFiles,
                                 @RequestParam(value = "careerFiles", required = false)List<MultipartFile> careerFiles,
                                 @RequestParam(value = "acbgFiles", required = false)List<MultipartFile> acbgFiles
    )throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        SearchDto dto = new SearchDto();
        System.out.println("****userInfo****"+userInfoJson);
        System.out.println("****certInfo****"+certInfoJson);
        System.out.println("****careerInfo****"+careerInfoJson);
        System.out.println("****acbgInfo****"+acbgInfoJson);

        Map<String, Object> userInfoMap = objectMapper.readValue(userInfoJson, Map.class);
        for (Map.Entry<String, Object> entry : userInfoMap.entrySet()) {
            dto.put(entry.getKey(), entry.getValue());
        }
        System.out.println(dto);


        List<Map<String, Object>> certInfoList = new ArrayList<>();
        List<Map<String, Object>> careerInfoList = new ArrayList<>();
        List<Map<String, Object>> acbgInfoList = new ArrayList<>();

        // certInfoJson이 null이 아니고 비어있지 않을 때만 JSON 파싱
        if (StringUtils.hasText(certInfoJson)) {
            certInfoList = objectMapper.readValue(certInfoJson, new TypeReference<List<Map<String, Object>>>() {});
        }
        if (StringUtils.hasText(careerInfoJson)) {
            careerInfoList = objectMapper.readValue(careerInfoJson, new TypeReference<List<Map<String, Object>>>() {});
        }
        if (StringUtils.hasText(acbgInfoJson)) {
            acbgInfoList = objectMapper.readValue(acbgInfoJson, new TypeReference<List<Map<String, Object>>>() {});
        }





        return memberApiService.insertMember(dto, certInfoList, careerInfoList, acbgInfoList, cnsltProfileFiles, certFiles, careerFiles, acbgFiles );
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
    public ResultVO setMemberMyPageModfiy(
            @ModelAttribute TblUser tblUser,
            @ModelAttribute TblCnslttMbr tblCnslttMbr,
            @RequestParam(value = "hasCertData", required = false) List<String> hasCertData,
            @RequestParam(value = "hasCrrData", required = false) List<String> hasCrrData,
            @RequestParam(value = "hasAcbgData", required = false) List<String> hasAcbgData) {

        List<TblQlfcLcns> certList = hasCertData != null ? hasCertData.stream()
                .map(data -> new Gson().fromJson(data, TblQlfcLcns.class))
                .collect(Collectors.toList()) : null;

        List<TblCrr> crrList = hasCrrData != null ? hasCrrData.stream()
                .map(data -> new Gson().fromJson(data, TblCrr.class))
                .collect(Collectors.toList()) : null;

        List<TblAcbg> acbgList = hasAcbgData != null ? hasAcbgData.stream()
                .map(data -> new Gson().fromJson(data, TblAcbg.class))
                .collect(Collectors.toList()) : null;

        return memberApiService.setMemberMyPageModfiy(tblUser, tblCnslttMbr, certList, crrList, acbgList);
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
    @PostMapping("/memeberApi/getMyPageDfclMttrList.do")
    public ResultVO getMyPageDfclMttrList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return memberApiService.getMyPageDfclMttrList(dto);
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
    
    @Operation(
            summary = "마이페이지 애로사항 수정",
            description = "마이페이지 애로사항 수정",
            tags = {"MemberController"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "애로사항 수정 성공"),
            @ApiResponse(responseCode = "400", description = "애로사항 수정 실패")
    })
    @PostMapping("/memberApi/setDifficultiesData")
    public ResultVO setDifficultiesData(
            @ModelAttribute TblDfclMttr tblDfclMttr,
            @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        return memberApiService.setDifficultiesData(tblDfclMttr, files);
    }


    @Operation(
            summary = "마이페이지 간편상담 조회",
            description = "마이페이지 간편상담 조회",
            tags = {"MemberController"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "간편상담 조회 성공"),
            @ApiResponse(responseCode = "400", description = "간편상담 조회 실패")
    })
    @PostMapping("/memberApi/getMyPageSimpleList.do")
    public ResultVO getMyPageSimpleList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return memberApiService.getMyPageSimpleList(dto);
    }

    @Operation(
            summary = "마이페이지 간편상담 상세보기 조회",
            description = "마이페이지 간편상담 상세보기 조회",
            tags = {"MemberController"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "간편상담 상세보기 조회 성공"),
            @ApiResponse(responseCode = "400", description = "간편상담 상세보기 조회 실패")
    })
    @PostMapping("/memberApi/getSimpleDetail.do")
    public ResultVO getSimpleDetail(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return memberApiService.getSimpleDetail(dto);
    }

    @Operation(
            summary = "마이페이지 간편상담 답변 수정",
            description = "마이페이지 간편상담 답변 수정",
            tags = {"MemberController"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "간편상담 답변 수정 성공"),
            @ApiResponse(responseCode = "400", description = "간편상담 답변 수정 실패")
    })
    @PostMapping("/memberApi/setSimpleData")
    public ResultVO setSimpleData(
            @ModelAttribute TblCnsltDsctn tblCnsltDsctn,
            @RequestParam(value = "simpleFiles", required = false) List<MultipartFile> files) {
        return memberApiService.setSimpleData(tblCnsltDsctn, files);
    }

    @Operation(
            summary = "마이페이지 간편상담 답변 등록",
            description = "마이페이지 간편상담 답변 등록",
            tags = {"MemberController"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "간편상담 답변 등록 성공"),
            @ApiResponse(responseCode = "400", description = "간편상담 답변 등록 실패")
    })
    @PostMapping("/memberApi/setCreateSimpleData")
    public ResultVO setCreateSimpleData(
            @ModelAttribute TblCnsltDsctn tblCnsltDsctn,
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            @RequestParam(value = "cnsltSttsCd", required = false) String cnsltSttsCd) {
        return memberApiService.setCreateSimpleData(tblCnsltDsctn, files, cnsltSttsCd);
    }


    @PostMapping("/memberApi/setComSimple")
    public ResultVO setComSimple(@RequestBody TblCnsltDtl tblCnsltDtl){
        return memberApiService.setComSimple(tblCnsltDtl);
    }

    @PostMapping("/memberApi/setCancelSimple")
    public ResultVO setCancelSimple(@RequestBody TblCnsltDtl tblCnsltDtl){
        return memberApiService.setCancelSimple(tblCnsltDtl);
    }

    @PostMapping("/memberApi/setSatisSimpleData")
    public ResponseEntity<ResultVO> setSatisSimpleData(@RequestParam Map<String, String> params) {
        String dgstfnArtcl = params.get("dgstfnArtcl");
        String chcScr = params.get("chcScr");
        Long cnsltAplySn = Long.valueOf(params.get("cnsltAplySn"));
        Long creatrSn = Long.valueOf(params.get("creatrSn"));

        TblCnsltDgstfn tblCnsltDgstfn = new TblCnsltDgstfn();
        tblCnsltDgstfn.setDgstfnArtcl(dgstfnArtcl);
        tblCnsltDgstfn.setChcScr(chcScr);
        tblCnsltDgstfn.setCnsltAplySn(cnsltAplySn);
        tblCnsltDgstfn.setCreatrSn(creatrSn);

        ResultVO result = memberApiService.setSatisSimpleData(tblCnsltDgstfn);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/memberApi/getSatisPopup")
    public ResultVO getSatisPopup(@RequestBody TblCnsltDgstfn tblCnsltDgstfn) {
        return memberApiService.getSatisPopup(tblCnsltDgstfn);
    }

    @PostMapping(value = "/memberApi/checkPassword.do")
    public ResultVO checkPassword(HttpServletRequest request) throws Exception {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return memberApiService.checkPassword(dto);
    }
}