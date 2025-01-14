package egovframework.com.devjitsu.controller.menu;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.service.menu.AuthGroupApiService;
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
public class AuthGroupApiController {

    @Autowired
    private AuthGroupApiService authGroupApiService;

    /**
     * 메뉴 권한 그룹 리스트
     * @param
     * {
     *     authGroupName : 권한 그룹 이름
     * }
     * @return
     */
    @PostMapping("/menuApi/getAuthGroupList.do")
    public ResultVO getAuthGroupList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return authGroupApiService.getAuthGroupList(dto);
    }
}
