package egovframework.com.devjitsu.controller.common;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.common.TblComCd;
import egovframework.com.devjitsu.model.common.TblComCdGroup;
import egovframework.com.devjitsu.model.menu.TblMenuAuthrtGroup;
import egovframework.com.devjitsu.service.common.CommonApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class CommonApiController {

    @Autowired
    private CommonApiService commonApiService;

    /**
     * 공통 코드 그룹 리스트 조회
     * @param params
     * @return
     */
    @PostMapping("/commonApi/getComCdGroupList.do")
    public ResultVO getComCdGroupList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return commonApiService.getComCdGroupList(dto);
    }

    /**
     * 공통 코드 그룹 리스트 페이징 조회
     * @return
     */
    @PostMapping("/commonApi/getComCdGroupListOnPageing.do")
    public ResultVO getComCdGroupListOnPageing(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return commonApiService.getComCdGroupListOnPageing(dto);
    }

    /**
     * 공통 코드 리스트 페이징 조회
     * @return
     */
    @PostMapping("/commonApi/getComCdListOnPageing.do")
    public ResultVO getComCdListOnPageing(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return commonApiService.getComCdListOnPageing(dto);
    }

    /**
     * 공통 코드 그룹 저장
     * @param
     * {
     *     cdGroupSn    : 코드그룹일련번호(수정시 필수)
     *     cdGroup    : 코드그룹 (필수)
     *     cdGroupNm       : CD_GROUP_NM (필수)
     *     etcMttr1        : 기타사항1
     *     etcMttr2        : 기타사항2
     *     etcMttr3        : 기타사항3
     *     etcMttr4        : 기타사항4
     *     etcMttr5        : 기타사항5
     *     actvtnYn        : 사용 유무
     *     rmrkCn          : 비고내용
     *     creatrSn        : 생성자일련번호(등록시 필수)
     *     mdfrSn          : 생성자일련번호(수정시 필수)
     * }
     * @return
     */
    @PostMapping("/commonApi/setComCdGroup")
    public ResultVO setComCdGroup(@RequestBody TblComCdGroup tblComCdGroup) {
        return commonApiService.setComCdGroup(tblComCdGroup);
    }

    /**
     * 공통 코드 저장
     * @param
     * {
     *     comCdSn    : 코드일련번호(수정시 필수)
     *     cdGroupSn    : 코드그룹일련번호(필수)
     *     cd    : 코드그룹 (필수)
     *     cdNm       : CD_GROUP_NM (필수)
     *     etcMttr1        : 기타사항1
     *     etcMttr2        : 기타사항2
     *     etcMttr3        : 기타사항3
     *     etcMttr4        : 기타사항4
     *     etcMttr5        : 기타사항5
     *     actvtnYn        : 사용 유무
     *     rmrkCn          : 비고내용
     *     sortSeq          : 정렬순서
     *     creatrSn        : 생성자일련번호(등록시 필수)
     *     mdfrSn          : 생성자일련번호(수정시 필수)
     * }
     * @return
     */
    @PostMapping("/commonApi/setComCd")
    public ResultVO setComCd(@RequestBody TblComCd tblComCd) {
        return commonApiService.setComCd(tblComCd);
    }

    /**
     * 공통 코드 그룹 삭제
     * @param
     * {
     *     cdGroupSn(필수)      : 코드그룹일련번호
     * }
     * @return
     */
    @PostMapping("/commonApi/setComCdGroupDel")
    public ResultVO setMenuAuthGroupDel(@RequestBody TblComCdGroup tblComCdGroup) {
        return commonApiService.setComCdGroupDel(tblComCdGroup);
    }

    /**
     * 공통 코드 삭제
     * @param
     * {
     *     comCdSn(필수)      : 코드일련번호
     * }
     * @return
     */
    @PostMapping("/commonApi/setComCdDel")
    public ResultVO setComCdDel(@RequestBody TblComCd tblComCd) {
        return commonApiService.setComCdDel(tblComCd);
    }

    /**
     * 공통 코드 그룹 조회
     * @param
     * {
     *     cdGroupSn : 코드그룹일련번호(필수)
     * }
     * @return
     */
    @PostMapping("/commonApi/getComCdGroup")
    public ResultVO getComCdGroup(@RequestBody TblComCdGroup tblComCdGroup) {
        return commonApiService.getComCdGroup(tblComCdGroup);
    }

    /**
     * 공통 코드 조회
     * @param
     * {
     *     comCdSn : 코드일련번호(필수)
     * }
     * @return
     */
    @PostMapping("/commonApi/getComCd")
    public ResultVO getComCd(@RequestBody TblComCd tblComCd) {
        return commonApiService.getComCd(tblComCd);
    }

    /**
     * 공통 코드 리스트 조회
     * @param
     * {
     *     cdGroupSn : 코드일련번호(필수)
     * }
     * @return
     */
    @PostMapping("/commonApi/getComCdList.do")
    public ResultVO getComCdList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return commonApiService.getComCdList(dto);
    }

    /**
     * 레디스 사용자 정보 조회 (공통)
     * @param request
     * @return
     */
    @PostMapping("/commonApi/getRedisUserInfo.do")
    public ResultVO getRedisUserInfo(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return commonApiService.getRedisUserInfo(dto);
    }
}
