package egovframework.com.devjitsu.controller.bannerPopup;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.bannerPopup.TblBnrPopup;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.service.bannerPopup.BnrPopupApiService;
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
public class BnrPopupApiController {

    @Autowired
    private BnrPopupApiService bnrPopupApiService;

    /**
     * 메뉴 권한 그룹 리스트 페이징 추가
     * @param
     * {
     *     pageIndex : 기본 값 1
     * }
     * @return
     */
    @PostMapping("/pannerPopupApi/getBnrPopupListOnPage.do")
    public ResultVO getBnrPopupListOnPage(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return bnrPopupApiService.getBnrPopupListOnPage(dto);
    }

    /**
     * 배너팝업 단일 조회
     * @param
     * {
     *     bnrPopupSn : 배너팝업일련번호 (필수)
     * }
     * @return
     */
    @PostMapping("/pannerPopupApi/getBnrPopup")
    public ResultVO getBnrPopup(@RequestBody TblBnrPopup tblBnrPopup) {
        return bnrPopupApiService.getBnrPopup(tblBnrPopup);
    }

}
