package egovframework.com.devjitsu.controller.consulting;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.consult.TblCnsltAply;
import egovframework.com.devjitsu.model.consult.TblCnsltDtl;
import egovframework.com.devjitsu.service.consult.ConsultingAdminApiService;
import egovframework.com.devjitsu.service.consult.ConsultingApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class ConsultingAdminApiController {

    @Autowired
    private ConsultingAdminApiService consultingAdminApiService;


    /**
     * 컨설팅 리스트 조회
     * @return
     */
    @PostMapping("/consultingApi/getConsultingList.do")
    public ResultVO getComCdGroupList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return consultingAdminApiService.getConsultingList(dto);
    }

    @PostMapping("/consultingApi/getConsultingDetail.do")
    public ResultVO getConsultingDetail(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return consultingAdminApiService.getConsultingDetail(dto);
    }
}
