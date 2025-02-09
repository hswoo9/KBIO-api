package egovframework.com.devjitsu.controller.index;

import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.service.common.CommonApiService;
import egovframework.com.devjitsu.service.main.MainApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class MainApiController {

    @Autowired
    private MainApiService mainApiService;

    /**
     * 배너팝업리스트
     * @param request
     * @return
     */
    @PostMapping("/mainApi/getBnrPopupList.do")
    public ResultVO getBnrPopupList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return mainApiService.getBnrPopupList(dto);
    }

    /**
     * 입주기관리스트
     * @return
     */
    @PostMapping("/mainApi/getMvnEntList")
    public ResultVO getMvnEntList() {
        return mainApiService.getMvnEntList();
    }

    /**
     * 게시글 리스트
     * @param request
     * @return
     */
    @PostMapping("/mainApi/getPstList.do")
    public ResultVO getPstList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return mainApiService.getPstList(dto);
    }

}
