package egovframework.com.devjitsu.controller.member;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.bbs.TblBbs;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.login.LettnemplyrinfoVO;
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
    public ResultVO getNormalMember(@RequestBody LettnemplyrinfoVO lettnemplyrinfoVO) {
        return memberAdminApiService.getNormalMember(lettnemplyrinfoVO);
    }

    /**
     * 회원 수정
     * @return
     */
    @PostMapping("/memberApi/setNormalMember")
    public ResultVO setBbs(@RequestBody LettnemplyrinfoVO lettnemplyrinfoVO){
        return memberAdminApiService.setNormalMember(lettnemplyrinfoVO);
    }

    /**
     * 회원 삭제
     * @return
     */
    @PostMapping("/memberApi/setNormalMemberDel")
    public ResultVO setNormalMemberDel(@RequestBody LettnemplyrinfoVO lettnemplyrinfoVO){
        return memberAdminApiService.setNormalMemberDel(lettnemplyrinfoVO);
    }


    /**
     * 회원 비밀번호 초기화
     * @return
     */
    @PostMapping(value = "/memberApi/resetMemberPassword")
    public ResultVO resetMemberPassword(@RequestBody LettnemplyrinfoVO lettnemplyrinfoVO) {
        return memberAdminApiService.resetMemberPassword(lettnemplyrinfoVO);
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
    public ResultVO setApprovalMemberDel(@RequestBody LettnemplyrinfoVO lettnemplyrinfoVO){
        return memberAdminApiService.setApprovalMemberDel(lettnemplyrinfoVO);
    }

    /**
     * 승인된회원 수정
     * @return
     */
    @PostMapping("/memberApi/getApprovalMember")
    public ResultVO getApprovalMember(@RequestBody LettnemplyrinfoVO lettnemplyrinfoVO) {
        return memberAdminApiService.getApprovalMember(lettnemplyrinfoVO);
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
    public ResultVO setWaitMemberApproval(@RequestBody LettnemplyrinfoVO lettnemplyrinfoVO){
        return memberAdminApiService.setWaitMemberApproval(lettnemplyrinfoVO);
    }

    /**
     * 대기회원 반려
     * @return
     */
    @PostMapping("/memberApi/setWaitMemberReject")
    public ResultVO setWaitMemberReject(@RequestBody LettnemplyrinfoVO lettnemplyrinfoVO){
        return memberAdminApiService.setWaitMemberReject(lettnemplyrinfoVO);
    }

    /**
     * 대기회원 수정
     * @return
     */
    @PostMapping("/memberApi/getWaitMember")
    public ResultVO getWaitMember(@RequestBody LettnemplyrinfoVO lettnemplyrinfoVO) {
        return memberAdminApiService.getWaitMember(lettnemplyrinfoVO);
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
    public ResultVO setRejectMemberApproval(@RequestBody LettnemplyrinfoVO lettnemplyrinfoVO){
        return memberAdminApiService.setRejectMemberApproval(lettnemplyrinfoVO);
    }

    /**
     * 반려회원 수정
     * @return
     */
    @PostMapping("/memberApi/getRejectMember")
    public ResultVO getRejectMember(@RequestBody LettnemplyrinfoVO lettnemplyrinfoVO) {
        return memberAdminApiService.getRejectMember(lettnemplyrinfoVO);
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
    public ResultVO setStopMemberApproval(@RequestBody LettnemplyrinfoVO lettnemplyrinfoVO){
        return memberAdminApiService.setStopMemberApproval(lettnemplyrinfoVO);
    }

    /**
     * 정지회원 수정
     * @return
     */
    @PostMapping("/memberApi/getStopMember")
    public ResultVO getStopMember(@RequestBody LettnemplyrinfoVO lettnemplyrinfoVO) {
        return memberAdminApiService.getStopMember(lettnemplyrinfoVO);
    }

}
