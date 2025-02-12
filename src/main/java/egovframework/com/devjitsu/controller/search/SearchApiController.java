package egovframework.com.devjitsu.controller.search;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.menu.TblContent;
import egovframework.com.devjitsu.model.menu.TblMenu;
import egovframework.com.devjitsu.service.menu.MenuApiService;
import egovframework.com.devjitsu.service.search.SearchApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class SearchApiController {

    @Autowired
    private SearchApiService searchApiService;

    /**
     * 공통 코드 그룹 리스트 페이징 조회
     * @return
     */
    @PostMapping("/searchApi/getSearchDataListPage.do")
    public ResultVO getSearchDataListPage(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return searchApiService.getSearchDataListPage(dto);
    }

}
