package egovframework.com.devjitsu.controller.terms;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.terms.TblUtztnTrms;
import egovframework.com.devjitsu.service.terms.UtztnTrmsApiService;
import egovframework.com.devjitsu.service.common.CommonApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class UtztnTrmsApiController {

    @Resource(name = "egovMessageSource")
    EgovMessageSource egovMessageSource;

    @Resource(name = "utztnTrmsApiService")
    private UtztnTrmsApiService UtztnTrmsApiService;

    /*
     *  개인정보처리방침 입력
     */
    @PostMapping(value = "/utztnApi/setPrivacyPolicy")
    public ResultVO setPrivacyPolicy(@ModelAttribute TblUtztnTrms tblUtztnTrms){
        return UtztnTrmsApiService.setPrivacyPilicy(tblUtztnTrms);
    }

}
