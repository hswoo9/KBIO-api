package egovframework.com.devjitsu.controller.statistics;

import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.service.statistics.StatisticsAdminApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class StatisticsAdminApiController {

    @Autowired
    private StatisticsAdminApiService statisticsAdminApiService;

    @PostMapping("/statisticsApi/getStatistics.do")
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

    /**
     * 사용자 통계
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/statisticsApi/getStatisticsUser.do")
    public ResultVO getStatisticsUser(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return statisticsAdminApiService.getStatisticsUser(dto);
    }

    /**
     * 사용자 접속통계
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/statisticsApi/getStatisticsUserAccess.do")
    public ResultVO getStatisticsUserAccess(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return statisticsAdminApiService.getStatisticsUserAccess(dto);
    }


    /**
     * 게시물 접속통계
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/statisticsApi/getStatisticsPstAccess.do")
    public ResultVO getStatisticsPstAccess(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return statisticsAdminApiService.getStatisticsPstAccess(dto);
    }

    /**
     * 첨부파일이용 통계
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/statisticsApi/getStatisticsPstFile.do")
    public ResultVO getStatisticsPstFile(HttpServletRequest request) {
        SearchDto dto = (SearchDto) request.getAttribute("searchDto");
        return statisticsAdminApiService.getStatisticsPstFile(dto);
    }
}
