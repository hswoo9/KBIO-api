package egovframework.com.devjitsu.controller.user;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.user.TblMvnEnt;
import egovframework.com.devjitsu.model.user.TblUser;
import egovframework.com.devjitsu.service.user.MvnEntApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class MvnEntApiController {

    @Autowired
    private MvnEntApiService mvnEntApiService;

    @PostMapping("/mvnEntApi/setMvnEnt")
    public ResultVO setMvnEnt(
                /*@RequestBody TblMvnEnt tblMvnEnt*/
                @ModelAttribute TblMvnEnt tblMvnEnt,
                @RequestParam(value = "files", required = false) List<MultipartFile> files){

        return mvnEntApiService.setMvnEnt(tblMvnEnt,files);
    }

    @PostMapping("/mvnEntApi/getMvnEntList.do")
    public ResultVO getMvnEntList(HttpServletRequest request){
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");

        return mvnEntApiService.getMvnEntList(dto);
    }

    @PostMapping("/mvnEntApi/getRc")
    public ResultVO getMvnEntOne(@RequestBody TblMvnEnt tblMvnEnt){
        return mvnEntApiService.getRc(tblMvnEnt);
    }

    @PostMapping("/mvnEntApi/getresidentMemberList.do")
    public ResultVO getResidentMemberList(HttpServletRequest request){
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");

        return mvnEntApiService.getResidentMemberList(dto);
    }

    @PostMapping("/mvnEntApi/getResidentMemberOne.do")
    public ResultVO getResidentMemberOne(HttpServletRequest request){
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");

        return mvnEntApiService.getResidentMemberOne(dto);
    }

    @PostMapping("/mvnEntApi/setMemberMbrStts")
    public ResultVO setMemberMbrStts(@RequestBody TblUser request){
        return mvnEntApiService.setMemberMbrStts(request);
    }

}
