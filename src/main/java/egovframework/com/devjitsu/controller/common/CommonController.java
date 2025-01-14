package egovframework.com.devjitsu.controller.common;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
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
public class CommonController {

    @Autowired
    private CommonApiService commonApiService;

    /**
     * 공통코드그룹 조회
     * @param params
     * @return
     */
    @PostMapping("/common/getComCdGroupList")
    public ResultVO getComCdGroupList(@RequestBody Map<String, Object> params) {
        return commonApiService.getComCdGroupList(params);
    }

    /**
     * 레디스 사용자 정보 조회 (공통)
     * @param request
     * @return
     */
    @PostMapping("/common/getRedisUserInfo")
    public ResultVO getRedisUserInfo(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return commonApiService.getRedisUserInfo(dto);
    }
}
