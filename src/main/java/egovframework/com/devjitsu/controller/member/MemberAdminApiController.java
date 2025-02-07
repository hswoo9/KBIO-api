package egovframework.com.devjitsu.controller.member;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.bbs.TblBbs;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.user.TblUser;
import egovframework.com.devjitsu.service.bbs.BbsAdminApiService;
import egovframework.com.devjitsu.service.member.MemberAdminApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class MemberAdminApiController {

    @Autowired
    private MemberAdminApiService memberAdminApiService;

    /*
     * 관리자 일반회원 조회 리스트
     */

    @PostMapping("/memberApi/getNormalMemberList.do")
    public ResultVO getNormalMemberList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return memberAdminApiService.getNormalMemberList(dto);
    }

    /**
     * 회원 조회
     * @return
     */
    @PostMapping("/memberApi/getNormalMember")
    public ResultVO getNormalMember(@RequestBody TblUser tblUser) {
        return memberAdminApiService.getNormalMember(tblUser);
    }

    /**
     * 회원 수정
     * @return
     */
    @PostMapping("/memberApi/setNormalMember")
    public ResultVO setNormalMember(@RequestBody TblUser tblUser){
        return memberAdminApiService.setNormalMember(tblUser);
    }

    /**
     * 회원 삭제
     * @return
     */
    @PostMapping("/memberApi/setNormalMemberDel")
    public ResultVO setNormalMemberDel(@RequestBody TblUser tblUser){
        return memberAdminApiService.setNormalMemberDel(tblUser);
    }


    /**
     * 회원 비밀번호 초기화
     * @return
     */
    @PostMapping(value = "/memberApi/resetMemberPassword")
    public ResultVO resetMemberPassword(@RequestBody TblUser tblUser) {
        return memberAdminApiService.resetMemberPassword(tblUser);
    }


    /**
     * 승인된회원 조회
     * @return
     */
    @PostMapping("/memberApi/getApprovalMemberList.do")
    public ResultVO getApprovalMemberList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return memberAdminApiService.getApprovalMemberList(dto);
    }

    /**
     * 승인된회원 정지
     * @return
     */
    @PostMapping("/memberApi/setApprovalMemberDel")
    public ResultVO setApprovalMemberDel(@RequestBody TblUser tblUser){
        return memberAdminApiService.setApprovalMemberDel(tblUser);
    }

    /**
     * 승인된회원 수정
     * @return
     */
    @PostMapping("/memberApi/getApprovalMember")
    public ResultVO getApprovalMember(@RequestBody TblUser tblUser) {
        return memberAdminApiService.getApprovalMember(tblUser);
    }

    /**
     * 대기회원 조회
     * @return
     */
    @PostMapping("/memberApi/getWaitMemberList.do")
    public ResultVO getWaitMemberList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return memberAdminApiService.getWaitMemberList(dto);
    }

    /**
     * 대기회원 승인
     * @return
     */
    @PostMapping("/memberApi/setWaitMemberApproval")
    public ResultVO setWaitMemberApproval(@RequestBody TblUser tblUser){
        return memberAdminApiService.setWaitMemberApproval(tblUser);
    }

    /**
     * 대기회원 반려
     * @return
     */
    @PostMapping("/memberApi/setWaitMemberReject")
    public ResultVO setWaitMemberReject(@RequestBody TblUser tblUser){
        return memberAdminApiService.setWaitMemberReject(tblUser);
    }

    /**
     * 대기회원 수정
     * @return
     */
    @PostMapping("/memberApi/getWaitMember")
    public ResultVO getWaitMember(@RequestBody TblUser tblUser) {
        return memberAdminApiService.getWaitMember(tblUser);
    }

    /**
     * 반려회원 조회
     * @return
     */
    @PostMapping("/memberApi/getRejectMemberList.do")
    public ResultVO getRejectMemberList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return memberAdminApiService.getRejectMemberList(dto);
    }

    /**
     * 반려회원 재승인
     * @return
     */
    @PostMapping("/memberApi/setRejectMemberApproval")
    public ResultVO setRejectMemberApproval(@RequestBody TblUser tblUser){
        return memberAdminApiService.setRejectMemberApproval(tblUser);
    }

    /**
     * 반려회원 수정
     * @return
     */
    @PostMapping("/memberApi/getRejectMember")
    public ResultVO getRejectMember(@RequestBody TblUser tblUser) {
        return memberAdminApiService.getRejectMember(tblUser);
    }

    /**
     * 정지회원 조회
     * @return
     */
    @PostMapping("/memberApi/getStopMemberList.do")
    public ResultVO getStopMemberList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return memberAdminApiService.getStopMemberList(dto);
    }

    /**
     * 정지회원 해제
     * @return
     */
    @PostMapping("/memberApi/setStopMemberApproval")
    public ResultVO setStopMemberApproval(@RequestBody TblUser tblUser){
        return memberAdminApiService.setStopMemberApproval(tblUser);
    }

    /**
     * 정지회원 수정
     * @return
     */
    @PostMapping("/memberApi/getStopMember")
    public ResultVO getStopMember(@RequestBody TblUser tblUser) {
        return memberAdminApiService.getStopMember(tblUser);
    }

    /**
     * 탈퇴회원 조회
     * @return
     */
    @PostMapping("/memberApi/getCancelMemberList.do")
    public ResultVO getCancelMemberList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return memberAdminApiService.getCancelMemberList(dto);
    }

}
