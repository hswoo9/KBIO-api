package egovframework.com.devjitsu.controller.index;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.service.bbs.PstApiService;
import egovframework.com.devjitsu.service.main.MainApiService;
import egovframework.com.devjitsu.service.main.ManagerMainApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class ManagerMainApiController {

    @Autowired
    private ManagerMainApiService managerMainApiService;

    /**
     * 관리자 메인 현황
     * @return
     */
    @PostMapping("/mMainApi/getStatus")
    public ResultVO getStatus() {
        return managerMainApiService.getStatus();
    }

    /**
     * 관리자 메인 캘린더 현황
     * @return
     */
    @PostMapping("/mMainApi/getCalendarData.do")
    public ResultVO getCalendarData(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return managerMainApiService.getCalendarData(dto);
    }


}
