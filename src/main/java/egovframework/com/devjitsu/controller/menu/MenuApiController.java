package egovframework.com.devjitsu.controller.menu;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.bbs.TblPst;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.menu.TblContent;
import egovframework.com.devjitsu.model.menu.TblMenu;
import egovframework.com.devjitsu.service.menu.MenuApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class MenuApiController {

    @Autowired
    private MenuApiService menuApiService;

    /**
     * 메뉴 전체 조회(TREE List)
     * @param
     * {
     *     upperMenuSn : 상위메뉴키
     *     actvtnYn : 기본값 전체
     * }
     * @return
     */
    @PostMapping("/menuApi/getMenuTreeList.do")
    public ResultVO getMenuTreeList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return menuApiService.getMenuTreeList(dto);
    }

    /**
     * 메뉴 조회
     * {
     *     menuSn : 메뉴키(필수)
     * }
     * @return
     */
    @PostMapping("/menuApi/getMenu")
    public ResultVO getMenu(@RequestBody TblMenu tblMenu) {
        return menuApiService.getMenu(tblMenu);
    }

    /**
     *
     * @param tblMenu
     * {
     *     menuSn       : 메뉴키(수정시 필수)
     *     upperMenuSn  : 상위 메뉴(필수) [최상위 메뉴일시 0]
     *     menuNm       : 메뉴 이름 (필수)
     *     menuType     : 메뉴 타입 (필수) [ 기본 D, 게시판 B]
     *     menuPathNm   : 메뉴 경로 (필수)
     *     menuSeq      : 메뉴 깊이 (필수)
     *     menuSortSeq  : 정렬 순서(필수)
     *     creatrSn     : 작성자 키(등록시 필수)
     *     mofrSn       : 수정자 키(수정시 필수)
     *     actvtnYn     : 사용 유무 (필수)
     * }
     * @return
     */
    @PostMapping("/menuApi/setMenu")
    public ResultVO setMenu(@RequestBody TblMenu tblMenu) {
        return menuApiService.setMenu(tblMenu);
    }

    /**
     * 메뉴 삭제
     * @param
     * {
     *     menuSns(필수)      : 메뉴키 (여러개 일시 쉼표로 분리)
     * }
     * @return
     */
    @PostMapping("/menuApi/setMenuDel.do")
    public ResultVO setMenuDel(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return menuApiService.setMenuDel(dto);
    }

    /**
     * 메뉴 컨텐츠 저장
     * {
     *     menuSn       : 메뉴키(필수)
     *     contsSn      : 메뉴컨텐츠키(수정시 필수)
     *     creatrSn     : 작성자 키(등록시 필수)
     *     mofrSn       : 수정자 키(수정시 필수)
     *     actvtnYn     : 사용 유무 (필수)
     * }
     * @return
     */
    @PostMapping("/menuApi/setMenuContent")
    public ResultVO setMenuContent(@ModelAttribute TblContent tblContent) {
        return menuApiService.setMenuContent(tblContent);
    }

}
