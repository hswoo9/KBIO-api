package egovframework.com.devjitsu.controller.operational;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.consult.TblDfclMttr;
import egovframework.com.devjitsu.service.consult.ConsultingAdminApiService;
import egovframework.com.devjitsu.service.operational.DiffAdminApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    /**
     * 애로사항 단일 조회
     * @return
     */
    @PostMapping("/diffApi/getDfclMttr")
    public ResultVO getDfclMttr(@RequestBody TblDfclMttr tblDfclMttr) {
        return diffAdminApiService.getDfclMttr(tblDfclMttr);
    }


    /**
     * 애로사항 답변등록
     * @param files
     * @return
     */
    @PostMapping("/diffApi/setDfclMttrAnswer")
    public ResultVO setDfclMttrAnswer(
            @ModelAttribute TblDfclMttr tblDfclMttr,
            @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        return diffAdminApiService.setDfclMttrAnswer(tblDfclMttr, files);
    }
}
