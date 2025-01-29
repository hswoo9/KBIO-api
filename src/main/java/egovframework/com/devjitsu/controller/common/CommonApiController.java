package egovframework.com.devjitsu.controller.common;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.common.TblComFile;
import egovframework.com.devjitsu.service.common.CommonApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class CommonApiController {

    @Autowired
    private CommonApiService commonApiService;


    /**
     * 관리자 아이피 체크
     * @return
     */
    @PostMapping("/commonApi/getMngrAcsIpChk")
    public ResultVO getMngrAcsIpChk(HttpServletRequest request) {
        return commonApiService.getMngrAcsIpChk(request);
    }

    /**
     * 공통코드그룹 조회
     * @param params
     * @return
     */
    @PostMapping("/commonApi/getComCdGroupList")
    public ResultVO getComCdGroupList(@RequestBody Map<String, Object> params) {
        return commonApiService.getComCdGroupList(params);
    }

    /**
     * 레디스 사용자 정보 조회 (공통)
     * @param request
     * @return
     */
    @PostMapping("/commonApi/getRedisUserInfo.do")
    public ResultVO getRedisUserInfo(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return commonApiService.getRedisUserInfo(dto);
    }

    /**
     * 첨부파일 삭제 (공통)
     * {
     *     atchFileSn : 첨부파일 키
     * }
     * @return
     */
    @PostMapping("/commonApi/setFileDel")
    public ResultVO setFileDel(@RequestBody TblComFile tblComFile) {
        return commonApiService.setFileDel(tblComFile);
    }

}
