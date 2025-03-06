package egovframework.com.devjitsu.controller.user;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.user.TblMvnEnt;
import egovframework.com.devjitsu.model.user.TblOrgcht;
import egovframework.com.devjitsu.model.user.TblUser;
import egovframework.com.devjitsu.service.menu.OrgchtApiService;
import egovframework.com.devjitsu.service.user.MvnEntApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class OrgchtApiController {

    @Autowired
    private OrgchtApiService orgchtApiService;

    /**
     * 조직도 저장
     * @param tblOrgcht
     * @return
     */
    @PostMapping("/orgchtApi/setOrgcht")
    public ResultVO setOrgcht(@RequestBody TblOrgcht tblOrgcht){
        return orgchtApiService.setOrgcht(tblOrgcht);
    }

    /**
     * 조직도 삭제
     * @param tblOrgcht
     * @return
     */
    @PostMapping("/orgchtApi/delOrgcht")
    public ResultVO delOrgcht(@RequestBody TblOrgcht tblOrgcht){
        return orgchtApiService.delOrgcht(tblOrgcht);
    }

    /**
     * 조직도 리스트 조회 (페이징)
     * @param request
     * @return
     */
    @PostMapping("/orgchtApi/getOrgchtList.do")
    public ResultVO getOrgchtList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return orgchtApiService.getOrgchtList(dto);
    }

    /**
     * 조직도 정보 조회 ( 단일 )
     * orgchtSn 필수
     * @param tblOrgcht
     * @return
     */
    @PostMapping("/orgchtApi/getOrgcht")
    public ResultVO getOrgcht(@RequestBody TblOrgcht tblOrgcht){
        return orgchtApiService.getOrgcht(tblOrgcht);
    }
}
