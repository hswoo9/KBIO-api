package egovframework.com.devjitsu.controller.bbs;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.bbs.TblBbs;
import egovframework.com.devjitsu.model.bbs.TblPst;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.service.bbs.BbsAdminApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class BbsAdminApiController {

    @Autowired
    private BbsAdminApiService bbsAdminApiService;

    /**
     * 게시판 리스트 조회
     * params
     * {
     *    bbsNm       : 게시판 명
     *    bbsType       : 게시판 유형
     *    actvtnYn      : 사용 여부
     * }
     * @return
     */
    @PostMapping("/bbsApi/getBbsList.do")
    public ResultVO getBbsList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return bbsAdminApiService.getBbsList(dto);
    }

    /**
     * 게시판 조회
     * params
     * {
     *     bbsSn(필수) : 게시판키
     * }
     * @param
     * @return
     */
    @PostMapping("/bbsApi/getBbs")
    public ResultVO getBbs(@RequestBody TblBbs tblBbs) {
        return bbsAdminApiService.getBbs(tblBbs);
    }

    /**
     * 게시판 저장
     * @param
     * {
     *     bbsNm(필수)        : 게시판 이름(필수)
     *     bbsType(필수)      : 게시판 타입(필수)
     *     creatrSn          : 작성자 키(신규 등록시 필수)
     *     pstCtgryYn        : 게시글 카테고리 사용유무 (기본 값 N)
     *     atchFileYn        : 첨부파일 사용여부 (기본 값 N)
     *     atchFileKndNm     : 첨부허용 확장자 (atchFileYn = Y 일때 쉼표로 분리)
     *     wrtrRlsYn         : 작성자 익명 여부 (기본 값 N)
     *     cmntPsbltyYn      : 댓글 사용 유무 (기본 값 N)
     *     replyPsbltyYn     : 답글 사용 유무 (기본 값 N)
     *     actvtnYn          : 사용 여부 (기본 값 Y)
     *     mdfrSn            : 수정자 키
     * }
     * @return
     */
    @PostMapping("/bbsApi/setBbs")
    public ResultVO setBbs(@RequestBody TblBbs tblBbs){
        return bbsAdminApiService.setBbs(tblBbs);
    }

    /**
     * 게시판 삭제
     * params
     * {
     *     bbsSn(필수)      : 게시판키
     * }
     * @param
     * @return
     */
    @PostMapping("/bbsApi/setBbsDel")
    public ResultVO setBbsDel(@RequestBody TblBbs tblBbs){
        return bbsAdminApiService.setBbsDel(tblBbs);
    }

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
    @PostMapping("/bbsApi/getPstList.do")
    public ResultVO getPstList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return bbsAdminApiService.getPstList(dto);
    }

    /**
     * 게시글 단일 조회
     * params
     * {
     *    pstSn(필수)      : 게시글키
     * }
     * @return
     */
    @PostMapping("/bbsApi/getPst")
    public ResultVO getPst(@RequestBody TblPst tblPst) {
        return bbsAdminApiService.getPst(tblPst);
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
    @PostMapping("/bbsApi/setPst")
    public ResultVO setPst(
            @ModelAttribute TblPst tblPst,
            @RequestParam(value = "files") List<MultipartFile> files){
        return bbsAdminApiService.setPst(tblPst, files);
    }

    /**
     * 게시글 삭제
     * params
     * {
     *    pstSn(필수)      : 게시글키
     * }
     * @return
     */
    @PostMapping("/bbsApi/setPstDel")
    public ResultVO setPstDel(@RequestBody TblPst tblPst) {
        return bbsAdminApiService.setPstDel(tblPst);
    }
}
