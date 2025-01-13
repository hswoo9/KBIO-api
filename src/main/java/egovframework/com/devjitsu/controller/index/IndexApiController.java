package egovframework.com.devjitsu.controller.index;

import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.ResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class IndexApiController {

    @Resource(name = "EgovFileMngUtil")
    private EgovFileMngUtil fileUtil;

    @PostMapping("/callback")
    public ResponseEntity<Map<String, Object>> callback(@RequestBody Map<String, Object> params) {
        Map<String, Object> s = new HashMap<>();

        System.out.println("params = " + params);

        if(Integer.parseInt(params.get("code").toString()) > 200){
            return new ResponseEntity<>(s, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(s, HttpStatus.OK);
        }
    }

    @PostMapping("/fileCheck")
    public ResponseEntity<ResultVO> fileCheck(final MultipartHttpServletRequest multiRequest) throws Exception {
        ResultVO resultVO = new ResultVO();
        Map<String, Object> result = new HashMap<>();
        final Map<String, MultipartFile> files = multiRequest.getFileMap();
        if (!files.isEmpty()) {
            result.put("fileInfo", fileUtil.parseFileInf(files, "TMP_", 0, "", ""));
        }
        resultVO.setResult(result);
        resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());
        return new ResponseEntity<>(resultVO, HttpStatus.OK);
    }
}
