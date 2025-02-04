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
     * 게시판 전체 리스트 조회
     * params
     * {
     *    bbsNm       : 게시판 명
     *    bbsType       : 게시판 유형
     *    actvtnYn      : 사용 여부
     * }
     * @return
     */
    @PostMapping("/bbsApi/getBbsAllList.do")
    public ResultVO getBbsAllList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return bbsAdminApiService.getBbsAllList(dto);
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
     *     ansPsbltyYn     : 답글 사용 유무 (기본 값 N)
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
}
