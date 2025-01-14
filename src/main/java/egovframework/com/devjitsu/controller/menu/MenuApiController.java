package egovframework.com.devjitsu.controller.menu;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.menu.TblMenu;
import egovframework.com.devjitsu.service.menu.MenuApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
     * {
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

    @PostMapping("/menuApi/setMenu")
    public ResultVO setMenu(@RequestBody TblMenu tblMenu) {
        return menuApiService.setMenu(tblMenu);
    }
}
