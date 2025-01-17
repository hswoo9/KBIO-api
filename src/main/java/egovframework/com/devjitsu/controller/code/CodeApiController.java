package egovframework.com.devjitsu.controller.code;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.common.TblComCd;
import egovframework.com.devjitsu.model.common.TblComCdGroup;
import egovframework.com.devjitsu.service.code.CodeApiService;
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
public class CodeApiController {

    @Autowired
    private CodeApiService codeApiService;

    /**
     * 공통 코드 그룹 리스트 조회
     * @param params
     * @return
     */
    @PostMapping("/codeApi/getComCdGroupList.do")
    public ResultVO getComCdGroupList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return codeApiService.getComCdGroupList(dto);
    }

    /**
     * 공통 코드 그룹 리스트 페이징 조회
     * @return
     */
    @PostMapping("/codeApi/getComCdGroupListOnPageing.do")
    public ResultVO getComCdGroupListOnPageing(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return codeApiService.getComCdGroupListOnPageing(dto);
    }

    /**
     * 공통 코드 리스트 페이징 조회
     * @return
     */
    @PostMapping("/codeApi/getComCdListOnPageing.do")
    public ResultVO getComCdListOnPageing(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return codeApiService.getComCdListOnPageing(dto);
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
    @PostMapping("/codeApi/setComCdGroup")
    public ResultVO setComCdGroup(@RequestBody TblComCdGroup tblComCdGroup) {
        return codeApiService.setComCdGroup(tblComCdGroup);
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
    @PostMapping("/codeApi/setComCd")
    public ResultVO setComCd(@RequestBody TblComCd tblComCd) {
        return codeApiService.setComCd(tblComCd);
    }

    /**
     * 공통 코드 그룹 삭제
     * @param
     * {
     *     cdGroupSn(필수)      : 코드그룹일련번호
     * }
     * @return
     */
    @PostMapping("/codeApi/setComCdGroupDel")
    public ResultVO setMenuAuthGroupDel(@RequestBody TblComCdGroup tblComCdGroup) {
        return codeApiService.setComCdGroupDel(tblComCdGroup);
    }

    /**
     * 공통 코드 삭제
     * @param
     * {
     *     comCdSn(필수)      : 코드일련번호
     * }
     * @return
     */
    @PostMapping("/codeApi/setComCdDel")
    public ResultVO setComCdDel(@RequestBody TblComCd tblComCd) {
        return codeApiService.setComCdDel(tblComCd);
    }

    /**
     * 공통 코드 그룹 조회
     * @param
     * {
     *     cdGroupSn : 코드그룹일련번호(필수)
     * }
     * @return
     */
    @PostMapping("/codeApi/getComCdGroup")
    public ResultVO getComCdGroup(@RequestBody TblComCdGroup tblComCdGroup) {
        return codeApiService.getComCdGroup(tblComCdGroup);
    }

    /**
     * 공통 코드 조회
     * @param
     * {
     *     comCdSn : 코드일련번호(필수)
     * }
     * @return
     */
    @PostMapping("/codeApi/getComCd")
    public ResultVO getComCd(@RequestBody TblComCd tblComCd) {
        return codeApiService.getComCd(tblComCd);
    }

    /**
     * 공통 코드 리스트 조회
     * @param
     * {
     *     cdGroupSn : 코드일련번호(필수)
     * }
     * @return
     */
    @PostMapping("/codeApi/getComCdList.do")
    public ResultVO getComCdList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return codeApiService.getComCdList(dto);
    }
}
