package egovframework.com.devjitsu.controller.operational;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.service.consult.ConsultingAdminApiService;
import egovframework.com.devjitsu.service.operational.DiffAdminApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class DiffAdminApiController {

    @Autowired
    private DiffAdminApiService diffAdminApiService;


    /**
     * 애로사항 리스트 조회
     * @return
     */
    @PostMapping("/diffApi/getDfclMttrList.do")
    public ResultVO getDfclMttrList(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return diffAdminApiService.getDfclMttrList(dto);
    }
}
