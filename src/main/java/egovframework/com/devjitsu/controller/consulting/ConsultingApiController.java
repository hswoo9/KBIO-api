package egovframework.com.devjitsu.controller.consulting;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.bbs.TblBbs;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.service.bbs.BbsAdminApiService;
import egovframework.com.devjitsu.service.common.CommonApiService;
import egovframework.com.devjitsu.service.consult.ConsultingApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class ConsultingApiController {

    @Autowired
    private ConsultingApiService consultingApiService;

    /**
     * 컨설턴트 리스트 조회
     * @return
     */
    @PostMapping("/consultingApi/getConsultantList.do")
    public ResultVO getComCdGroupList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return consultingApiService.getConsultantList(dto);
    }
}
