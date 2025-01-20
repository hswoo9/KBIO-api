package egovframework.com.devjitsu.controller.bannerPopup;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.bannerPopup.TblBnrPopup;
import egovframework.com.devjitsu.model.bbs.TblPst;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.service.bannerPopup.BnrPopupApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    @PostMapping("/bannerPopupApi/getBnrPopupListOnPage.do")
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
    @PostMapping("/bannerPopupApi/getBnrPopup")
    public ResultVO getBnrPopup(@RequestBody TblBnrPopup tblBnrPopup) {
        return bnrPopupApiService.getBnrPopup(tblBnrPopup);
    }

    @PostMapping("/bannerPopupApi/setBnrPopup")
    public ResultVO setBnrPopup(
            @ModelAttribute TblBnrPopup tblBnrPopup,
            @RequestParam(value = "files") List<MultipartFile> files){
        return bnrPopupApiService.setBnrPopup(tblBnrPopup, files);
    }

    /**
     * 배너팝업 삭제
     * @param
     * {
     *     bnrPopupSn(필수)
     * }
     * @return
     */
    @PostMapping("/bannerPopupApi/setBnrPopupDel")
    public ResultVO setBnrPopupDel(@RequestBody TblBnrPopup tblBnrPopup) {
        return bnrPopupApiService.setBnrPopupDel(tblBnrPopup);
    }

}
