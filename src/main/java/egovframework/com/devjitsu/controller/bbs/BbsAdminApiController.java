package egovframework.com.devjitsu.controller.bbs;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.service.bbs.BbsAdminApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class BbsAdminApiController {

    @Autowired
    private BbsAdminApiService bbsAdminApiService;

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
        return bbsAdminApiService.getBbsList(dto);
    }

}
