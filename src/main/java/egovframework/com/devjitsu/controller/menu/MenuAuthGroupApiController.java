package egovframework.com.devjitsu.controller.menu;

import com.fasterxml.jackson.core.JsonProcessingException;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.menu.TblMenuAuthrtGroup;
import egovframework.com.devjitsu.model.menu.TblMenuAuthrtGroupUser;
import egovframework.com.devjitsu.service.menu.MenuAuthGroupApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class MenuAuthGroupApiController {

    @Autowired
    private MenuAuthGroupApiService menuAuthGroupApiService;

    /**
     * 메뉴 권한 그룹 리스트
     * @param
     * {
     *     authGroupName : 권한 그룹 이름
     * }
     * @return
     */
    @PostMapping("/menuApi/getMenuAuthGroupList.do")
    public ResultVO getMenuAuthGroupList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return menuAuthGroupApiService.getMenuAuthGroupList(dto);
    }

    /**
     * 메뉴 권한 그룹 조회
     * @param
     * {
     *     authrtGroupSn : 권한그룹키(필수)
     * }
     * @return
     */
    @PostMapping("/menuApi/getMenuAuthGroup")
    public ResultVO getMenuAuthGroup(@RequestBody TblMenuAuthrtGroup tblMenuAuthrtGroup) {
        return menuAuthGroupApiService.getMenuAuthGroup(tblMenuAuthrtGroup);
    }

    /**
     * 메뉴 권한 그룹 저장
     * @param
     * {
     *     authrtGroupSn    : 권한 그룹 키(수정시 필수)
     *     authrtGroupNm    : 권한 그룹 이름 (필수)
     *     authrtType       : 권한 구분 (필수)
     *     inqAuthrt        : 읽기권한
     *     wrtAuthrt        : 작성권한
     *     mdfcnAuthrt      : 수정권한
     *     delAuthrt        : 삭제권한
     *     creatrSn         : 작성자 키(등록시 필수)
     *     mdfrSn           : 수정자 키(수정시 필수)
     *     actvtnYn         : 사용 유무
     *     allowAccessMenu  : JsonStringList [menuId(필수)]
     * }
     * @return
     */
    @PostMapping("/menuApi/setMenuAuthGroup")
    public ResultVO setMenuAuthGroup(@RequestBody TblMenuAuthrtGroup tblMenuAuthrtGroup) {
        return menuAuthGroupApiService.setMenuAuthGroup(tblMenuAuthrtGroup);
    }

    /**
     * 권한그룹 삭제
     * @param
     * {
     *     authrtGroupSns(필수)      : 권한그룹 키 (여러개 일시 쉼표로 분리)
     * }
     * @return
     */
    @PostMapping("/menuApi/setMenuAuthGroupDel.do")
    public ResultVO setMenuAuthGroupDel(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return menuAuthGroupApiService.setMenuAuthGroupDel(dto);
    }

    /**
     * 권한그룹 사용자 조회
     * @param
     * {
     *     authrtGroupSn      : 권한그룹 키 (필수)
     *     userId             : 사용자 아이디(검색조건)
     *     userNm             : 사용자 이름(검색조건)
     * }
     * @return
     */
    @PostMapping("/menuApi/getMenuAuthGroupUserList.do")
    public ResultVO getMenuAuthGroupUserList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return menuAuthGroupApiService.getMenuAuthGroupUserList(dto);
    }

    /**
     * 권한그룹 사용자 저장
     * @param
     * {
     *     권한부여 사용자 리스트(jsonArray)
     *     authrtGroupUsers : {
     *          authrtGroupUserSn   : 권한 부여 키(수정시 필수)
     *          authrtGroupSn       : 권한 그룹 키 (필수)
     *          userSn              : 사용자 키(필수)
     *          userNm              : 사용자 이름(필수)
     *          userId              : 사용자 아이디(필수)
     *          authrtGrntDt        : 권한 부여일 (null 일시 현재날짜)[형식 : yyyy-mm-dd]
     *          creatrSn            : 작성자 키(등록시 필수)
     *          mdfrSn              : 수정자 키(수정시 필수)
     *     }
     * }
     * @return
     */
    @PostMapping("/menuApi/setMenuAuthGroupUser.do")
    public ResultVO setMenuAuthGroupUser(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return menuAuthGroupApiService.setMenuAuthGroupUser(dto);
    }
}
