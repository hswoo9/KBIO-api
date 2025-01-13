package egovframework.com.devjitsu.controller.statistics;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.service.common.CommonApiService;
import egovframework.com.devjitsu.service.statistics.StatisticsAdminApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class StatisticsAdminApiController {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private StatisticsAdminApiService statisticsAdminApiService;

    @PostMapping("/admin/statistics/getStatistics.do")
    public ResultVO consultStatistics(HttpServletRequest request) throws Exception {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");

//        try {
//            model.addAttribute("sts", statisticsService.getTypeStatistics(params));
//            if(params.get("page").equals("content")) {
//                model.addAttribute("content", statisticsService.getSearchTermList(params));
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        return statisticsAdminApiService.getTypeStatistics(dto);
    }
}
