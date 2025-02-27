package egovframework.com.devjitsu.controller.user;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.user.*;
import egovframework.com.devjitsu.service.user.MvnEntApiService;
import egovframework.com.devjitsu.service.user.RelInstApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class RelInstApiController {

    @Autowired
    private RelInstApiService relInstApiService;

    @PostMapping("/relatedApi/setRelInst")
    public ResultVO setRelInst(
            @ModelAttribute TblRelInst tblRelInst,
            @RequestParam(value = "file", required = false) List<MultipartFile> files,
            @RequestParam(value= "files", required = false) List<MultipartFile> relInstAtchFiles){
        return relInstApiService.setRelInst(tblRelInst,files, relInstAtchFiles);
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

    @PostMapping("/relatedApi/getRelatedtMemberList.do")
    public ResultVO getRelatedtMemberList(HttpServletRequest request){
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");

        return relInstApiService.getRelatedtMemberList(dto);
    }

    @PostMapping("/relatedApi/setAprvYn")
    public ResultVO setAprvYn(@RequestBody TblRelInstMbr request){
        return relInstApiService.setAprvYn(request);
    }

    @PostMapping("/relatedApi/getRelatedMemberOne.do")
    public ResultVO getRelatedMemberOne(HttpServletRequest request){
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");

        return relInstApiService.getRelatedMemberOne(dto);
    }

    @PostMapping("/relatedApi/updateRelInstMbrToMng")
    public ResultVO updateRelInstMbrToMng(@RequestBody List<TblRelInstMbr> tblRelInstMbrList){
        return relInstApiService.updateRelInstMbrToMng(tblRelInstMbrList);
    }

    @PostMapping("/relatedApi/cancleMng")
    public ResultVO cancleMng(@RequestBody TblRelInstMbr tblRelInstMbr){
        return relInstApiService.cancleMng(tblRelInstMbr);
    }

}