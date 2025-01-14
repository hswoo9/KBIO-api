package egovframework.com.devjitsu.controller.menu;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.menu.TblMenuAuthrtGroup;
import egovframework.com.devjitsu.service.menu.MenuAuthGroupApiService;
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

}
