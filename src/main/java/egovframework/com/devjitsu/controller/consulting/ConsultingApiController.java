package egovframework.com.devjitsu.controller.consulting;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.bbs.TblBbs;
import egovframework.com.devjitsu.model.bbs.TblPst;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.consult.TblCnsltAply;
import egovframework.com.devjitsu.model.consult.TblCnsltDtl;
import egovframework.com.devjitsu.model.consult.TblDfclMttr;
import egovframework.com.devjitsu.service.bbs.BbsAdminApiService;
import egovframework.com.devjitsu.service.common.CommonApiService;
import egovframework.com.devjitsu.service.consult.ConsultingApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class ConsultingApiController {

    @Autowired
    private ConsultingApiService consultingApiService;

    /**
     * 컨설턴트 리스트 조회
     * @return
     */
    @PostMapping("/consultingApi/getConsultantList.do")
    public ResultVO getComCdGroupList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return consultingApiService.getConsultantList(dto);
    }

    /**
     * 컨설팅 신청
     * @param tblCnsltAply
     * @param files
     * @return
     */
    @PostMapping("/consultingApi/setConsulting")
    public ResultVO setConsulting(
            @ModelAttribute TblCnsltAply tblCnsltAply,
            @ModelAttribute TblCnsltDtl tblCnsltDtl,
            @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        return consultingApiService.setConsulting(tblCnsltAply, tblCnsltDtl, files);
    }

    /**
     * 애로사항 등록
     * @param files
     * @return
     */
    @PostMapping("/consultingApi/setDfclMttr")
    public ResultVO setDfclMttr(
            @ModelAttribute TblDfclMttr tblDfclMttr,
            @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        return consultingApiService.setDfclMttr(tblDfclMttr, files);
    }
}
