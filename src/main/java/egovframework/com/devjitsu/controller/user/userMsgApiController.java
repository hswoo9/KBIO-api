package egovframework.com.devjitsu.controller.user;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.user.TblOrgcht;
import egovframework.com.devjitsu.model.user.TblUserMsg;
import egovframework.com.devjitsu.service.menu.OrgchtApiService;
import egovframework.com.devjitsu.service.user.UserMsgApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class userMsgApiController {

    @Autowired
    private UserMsgApiService userMsgApiService;

    /**
     * 사용자 상단 메세지 조회
     * @return
     */
    @PostMapping("/userMsgApi/getUserMsgTopList.do")
    public ResultVO getUserMsgTopList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return userMsgApiService.getUserMsgTopList(dto);
    }

    /**
     * 사용자 메세지 조회
     * @return
     */
    @PostMapping("/userMsgApi/getUserMsgList.do")
    public ResultVO getUserMsgList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return userMsgApiService.getUserMsgList(dto);
    }

    /**
     * 사용자 알림 노출 여부 수정
     * @return
     */
    @PostMapping("/userMsgApi/setUserMsgExpsrYn")
    public ResultVO setUserMsgExpsrYn(@RequestBody TblUserMsg tblUserMsg) {
        return userMsgApiService.setUserMsgExpsrYn(tblUserMsg);
    }

    /**
     * 사용자 알림 확인 수정
     * @return
     */
    @PostMapping("/userMsgApi/setMsgConfirm")
    public ResultVO setMsgConfirm(@RequestBody TblUserMsg tblUserMsg) {
        return userMsgApiService.setMsgConfirm(tblUserMsg);
    }
}
