package egovframework.com.devjitsu.controller.common;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.common.TblComFile;
import egovframework.com.devjitsu.service.common.CommonApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class CommonApiController {

    @Autowired
    private CommonApiService commonApiService;

    /**
     * 사용자 메뉴 조회
     * @return
     */
    @PostMapping("/commonApi/getMenu.do")
    public ResultVO getMenu(HttpServletRequest request) {
        return commonApiService.getMenu(request);
    }


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
     * 첨부파일 다운로드 (공통)
     * {
     *     atchFileSn : 첨부파일 키
     * }
     * @return
     */
    @PostMapping("/commonApi/getFileDownLoad")
    public ResponseEntity<Resource> getFileDownLoad(@RequestBody TblComFile tblComFile, HttpServletRequest request, HttpServletResponse response) throws IOException {
        return commonApiService.getFileDownLoad(tblComFile, request, response);
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
