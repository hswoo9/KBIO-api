package egovframework.com.devjitsu.controller.member;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.bbs.TblBbs;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.login.LettnemplyrinfoVO;
import egovframework.com.devjitsu.service.bbs.BbsAdminApiService;
import egovframework.com.devjitsu.service.member.MemberAdminApiService;
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
/*    @PostMapping("/memberApi/setNormalMemberDel")
    public ResultVO setBbsDel(@RequestBody LettnemplyrinfoVO lettnemplyrinfoVO){
        return memberAdminApiService.setNormalMemberDel(lettnemplyrinfoVO);
    }*/


}
