package egovframework.com.devjitsu.controller.bbs;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.service.bbs.BbsApiService;
import egovframework.com.devjitsu.service.common.CommonApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
public class BbsApiController {

    @Autowired
    private BbsApiService bbsApiService;

    /**
     * 게시판 리스트 조회
     * params
     * {
     *    bbsNm       : 게시판 명
     *    bbsType       : 게시판 유형
     *    actvtnYn      : 사용 여부
     * }
     * @return
     */
    @PostMapping("/bbsApi/getBbsList.do")
    public ResultVO getBbsList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return bbsApiService.getBbsList(dto);
    }

}
