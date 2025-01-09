package egovframework.com.devjitsu.controller.common;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.service.common.CommonApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class CommonController {

    @Autowired
    private CommonApiService commonApiService;

    @PostMapping("/common/getComCdGroupList")
    public ResponseEntity<ResultVO> getComCdGroupList(@RequestBody Map<String, Object> params) {
        ResultVO s = commonApiService.getComCdGroupList(params);

        if(s.getResultCode() > 200){
            return new ResponseEntity<>(s, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(s, HttpStatus.OK);
        }
    }
}
