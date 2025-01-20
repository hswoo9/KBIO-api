package egovframework.com.devjitsu.controller.bbs;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.bbs.TblPst;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.service.bbs.BbsAdminApiService;
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
public class PstApiController {

    @Autowired
    private PstApiService pstApiService;

    /**
     * 게시글 리스트 조회
     * params
     * {
     *    bbsSn(필수)      : 게시판키
     *    searchType       : 검색유형
     *    searchVal : 검색어
     * }
     * @return
     */
    @PostMapping("/pstApi/getPstList.do")
    public ResultVO getPstList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return pstApiService.getPstList(dto);
    }

    /**
     * 게시글 단일 조회
     * params
     * {
     *    pstSn(필수)      : 게시글키
     * }
     * @return
     */
    @PostMapping("/pstApi/getPst")
    public ResultVO getPst(@RequestBody TblPst tblPst) {
        return pstApiService.getPst(tblPst);
    }

    /**
     * 게시글 저장
     * @param
     * {
     *     bbsSn         : 게시판키(필수)
     *     pstTtl(필수)   : 게시글 제목(필수)
     *     pstCn(필수)    : 게시글 내용(필수)
     *     creatrSn      : 작성자 키(필수)
     *
     *     pstSn         : 게시글 키 (수정시 필수)
     *     upendNtcYn    : 공지 유무(기본값 N)
     *     ntcBgngDt     : 공지 시작일 (공지 유무 Y일시 필수)
     *     ntcEndDate    : 공지 종료일 (공지 유무 Y일시 필수)
     *     otsdLink      : 관련링크
     *     orgnlPstSn    : 원글키(답글시 필수)
     *     cmntPicSn     : 답글 작성 담당자 키
     *     rlsYn         : 비공개 유무
     *     prvtPswd      : 비공개 비밀번호 (비공개인 경우 필수)
     * }
     * @param files (파일)
     * @return
     */
    @PostMapping("/pstApi/setPst")
    public ResultVO setPst(
            @ModelAttribute TblPst tblPst,
            @RequestParam(value = "files", required = false) List<MultipartFile> files){
        return pstApiService.setPst(tblPst, files);
    }

    /**
     * 게시글 삭제
     * params
     * {
     *    pstSn(필수)      : 게시글키
     * }
     * @return
     */
    @PostMapping("/pstApi/setPstDel")
    public ResultVO setPstDel(@RequestBody TblPst tblPst) {
        return pstApiService.setPstDel(tblPst);
    }
}
