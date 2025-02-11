package egovframework.com.devjitsu.controller.introduce;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.user.TblMvnEnt;
import egovframework.com.devjitsu.model.user.TblUser;
import egovframework.com.devjitsu.service.introduce.IntroduceApiService;
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
public class IntroduceApiController {

    @Autowired
    private IntroduceApiService introduceApiService;

    @PostMapping("/introduceApi/getOperationalList.do")
    public ResultVO getOperationalList(HttpServletRequest request){
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");

        return introduceApiService.getOperationalList(dto);
    }
}
