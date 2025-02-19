package egovframework.com.devjitsu.controller.common;

import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.bbs.TblPst;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.common.TblAtchFileDwnldCnt;
import egovframework.com.devjitsu.model.common.TblComFile;
import egovframework.com.devjitsu.model.user.TblUser;
import egovframework.com.devjitsu.model.user.TblUserMsg;
import egovframework.com.devjitsu.service.common.CommonApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class CommonApiController {

    @Autowired
    private CommonApiService commonApiService;

    /**
     * 사용자 전체 메뉴 조회
     * @return
     */
    @PostMapping("/commonApi/getMenu.do")
    public ResultVO getMenu(HttpServletRequest request) {
        return commonApiService.getMenu(request);
    }

    /**
     * 사용자 left 메뉴 조회
     * @return
     */
    @PostMapping("/commonApi/getLeftMenu.do")
    public ResultVO getLeftMenu(HttpServletRequest request) {
        return commonApiService.getLeftMenu(request);
    }

    /**
     * 사용자 메세지 조회
     * @return
     */
    @PostMapping("/commonApi/getUserMsgList.do")
    public ResultVO getUserMsgList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return commonApiService.getUserMsgList(dto);
    }

    /**
     * 사용자 메세지 조회
     * @return
     */
    @PostMapping("/commonApi/setUserMsgExpsrYn")
    public ResultVO setUserMsgExpsrYn(@RequestBody TblUserMsg tblUserMsg) {
        return commonApiService.setUserMsgExpsrYn(tblUserMsg);
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
     * 첨부파일 단일 조회 (공통) - 미사용
     * {
     *     atchFileSn : 첨부파일 키
     *     psnTblSn : 소유테이블데이터기본키
     * }
     * @return
     */
    @PostMapping("/commonApi/getFile")
    public ResultVO getFile(@RequestBody TblComFile tblComFile) {
        return commonApiService.getFile(tblComFile);
    }

    /**
     * CK에디터 첨부파일 업로드 (공통)
     * @param files
     * @return
     */
    @PostMapping("/commonApi/setCkEditorFiles")
    public ResultVO setCkEditorFiles(
            @RequestParam(value = "files", required = false) List<MultipartFile> files){
        return commonApiService.setCkEditorFiles(files);
    }

    /**
     * 첨부파일 다운로드 (공통)
     * {
     *     atchFileSn : 첨부파일 키
     * }
     * @return
     */
    @PostMapping("/commonApi/getFileDownLoad.do")
    public ResponseEntity<Resource> getFileDownLoad(HttpServletRequest request) throws IOException {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return commonApiService.getFileDownLoad(dto);
    }

    /**
     * 첨부파일 다운로드 (공통)
     * {
     *     atchFileSn : 첨부파일 키
     * }
     * @return
     */
    @PostMapping("/commonApi/getFileZipDownLoad.do")
    public ResponseEntity<Resource> getFileZipDownLoad(HttpServletRequest request) throws IOException {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return commonApiService.getFileZipDownLoad(dto);
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


    /**
     * 중복로그인체크 ( 반영시 주석 해제 )
     * @param request
     * @return
     */
    @PostMapping("/commonApi/getDuplicateLogin.do")
    public ResultVO getDuplicateLogin(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        String duplicateLogin = "N";

        String authHeader = request.getHeader("Authorization");
        ResultVO resultVO = commonApiService.getRedisUserInfo(dto);
//        if(resultVO.getResult("rs") != null && !StringUtils.isEmpty(authHeader)){
//            if(!resultVO.getResult("rs").equals(authHeader)){
//                duplicateLogin = "Y";
//                request.getSession().invalidate();
//                resultVO.setResultCode(ResponseCode.DUPLICATE_LOGOUT.getCode());
//                resultVO.setResultMessage(ResponseCode.DUPLICATE_LOGOUT.getMessage());
//            }
//        }

        resultVO.putResult("duplicateLogin", duplicateLogin);

        return resultVO;
    }
}
