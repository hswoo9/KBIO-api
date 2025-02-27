package egovframework.com.devjitsu.controller.user;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.user.TblMvnEnt;
import egovframework.com.devjitsu.model.user.TblMvnEntMbr;
import egovframework.com.devjitsu.model.user.TblUser;
import egovframework.com.devjitsu.service.user.MvnEntApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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
                @RequestParam(value = "files", required = false) List<MultipartFile> files,
                @RequestParam(value= "mvnEntAtchFiles", required = false)List<MultipartFile> mvnEntAtchFiles){

        return mvnEntApiService.setMvnEnt(tblMvnEnt,files, mvnEntAtchFiles);
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

    @PostMapping("/mvnEntApi/setAprvYn")
    public ResultVO setAprvYn(@RequestBody TblMvnEntMbr request){
        return mvnEntApiService.setAprvYn(request);
    }

    @PostMapping("/mvnEntApi/updateMvnEntMbrToMng")
    public ResultVO updateMvnEntMbrToMng(@RequestBody List<TblMvnEntMbr> tblMvnEntMbrList){

        return mvnEntApiService.updateMvnEntMbrToMng(tblMvnEntMbrList);
    }

    //cancleMng
    @PostMapping("/mvnEntApi/cancleMng")
    public ResultVO cancleMng(@RequestBody TblMvnEntMbr tblMvnEntMbrList){

        return mvnEntApiService.cancleMng(tblMvnEntMbrList);
    }

}
