package egovframework.com.devjitsu.controller.menu;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.service.menu.MenuApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class MenuApiController {

    @Autowired
    private MenuApiService menuApiService;

    @PostMapping("/menuApi/getMenuTreeList")
    public ResultVO getMenuTreeList(@RequestBody SearchDto dto) {
        return menuApiService.getMenuTreeList(dto);
    }

    @PostMapping("/menuApi/setMenu")
    public ResponseEntity<ResultVO> setMenu(@RequestBody Map<String, Object> params) {
        ResultVO s = menuApiService.setMenu(params);
        if(s.getResultCode() > 200){
            return new ResponseEntity<>(s, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(s, HttpStatus.OK);
        }
    }
}
