package egovframework.com.devjitsu.controller.user;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.user.TblUserMsg;
import egovframework.com.devjitsu.service.user.UserMsgAdminApiService;
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
public class userMsgAdminApiController {

    @Autowired
    private UserMsgAdminApiService userMsgAdminApiService;

    /**
     * 관리자 메세지 조회
     * @return
     */
    @PostMapping("/userMsgAdminApi/getUserMsgList.do")
    public ResultVO getUserMsgList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return userMsgAdminApiService.getUserMsgList(dto);
    }
}
