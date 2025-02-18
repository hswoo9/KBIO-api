package egovframework.com.devjitsu.controller.user;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.user.TblMvnEnt;
import egovframework.com.devjitsu.model.user.TblRelInst;
import egovframework.com.devjitsu.model.user.TblUser;
import egovframework.com.devjitsu.service.user.MvnEntApiService;
import egovframework.com.devjitsu.service.user.RelInstApiService;
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
public class RelInstApiController {

    @Autowired
    private RelInstApiService relInstApiService;

    @PostMapping("/relatedApi/setRelInst")
    public ResultVO setRelInst(@RequestBody TblRelInst tblRelInst){
        return relInstApiService.setRelInst(tblRelInst);
    }

    @PostMapping("/relatedApi/getRelInstList.do")
    public ResultVO getRelInstList(HttpServletRequest request){
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");

        return relInstApiService.getRelInstList(dto);
    }

    @PostMapping("/relatedApi/getRc")
    public ResultVO getRc(@RequestBody TblRelInst tblRelInst){
        return relInstApiService.getRc(tblRelInst);
    }



}