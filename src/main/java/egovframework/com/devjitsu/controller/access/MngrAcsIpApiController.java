package egovframework.com.devjitsu.controller.access;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.access.TblMngrAcsIp;
import egovframework.com.devjitsu.model.bbs.TblPst;
import egovframework.com.devjitsu.model.bbs.TblPstCmnt;
import egovframework.com.devjitsu.model.bbs.TblPstEvl;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.service.access.MngrAcsIpApiService;
import egovframework.com.devjitsu.service.bbs.PstApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class MngrAcsIpApiController {

    @Autowired
    private MngrAcsIpApiService mngrAcsIpApiService;

    /**
     * 관리자 접근 아이피 리스트 조회
     * params
     * {
     *    searchType       : 검색유형
     *    searchVal : 검색어
     * }
     * @return
     */
    @PostMapping("/mngrAcsIpApi/getMngrAcsIpList.do")
    public ResultVO getMngrAcsIpList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return mngrAcsIpApiService.getMngrAcsIpList(dto);
    }

    /**
     * 관리자 접근 아이피 단일 조회
     * params
     * {
     *    mngrAcsSn(필수)      : 관리자접근 일련번호
     * }
     * @return
     */
    @PostMapping("/mngrAcsIpApi/getMngrAcsIp")
    public ResultVO getMngrAcsIp(@RequestBody TblMngrAcsIp tblMngrAcsIp) {
        return mngrAcsIpApiService.getMngrAcsIp(tblMngrAcsIp);
    }

    /**
     * 관리자 접근 아이피 저장
     * @param
     * {
     *     mngrAcsSn       : 관리자접근 일련번호 (수정시 필수)
     *     ipAddr(필수)     : 아이피(필수)
     *     plcusNm(필수)    : 사용처명(필수)
     *     creatrSn        : 생성자일련번호(등록시 필수)
     *     mdfrSn          : 생성자일련번호(수정시 필수)
     *     actvtnYn        : 사용 유무
     * }
     * @return
     */
    @PostMapping("/mngrAcsIpApi/setMngrAcsIp")
    public ResultVO setMngrAcsIp(@RequestBody TblMngrAcsIp tblMngrAcsIp){
        return mngrAcsIpApiService.setMngrAcsIp(tblMngrAcsIp);
    }

    /**
     * 관리자 접근 아이피 삭제
     * params
     * {
     *    mngrAcsSn(필수)      : 관리자접근 일련번호
     * }
     * @return
     */
    @PostMapping("/mngrAcsIpApi/setMngrAcsIpDel")
    public ResultVO setMngrAcsIpDel(@RequestBody TblMngrAcsIp tblMngrAcsIp) {
        return mngrAcsIpApiService.setMngrAcsIpDel(tblMngrAcsIp);
    }

}
