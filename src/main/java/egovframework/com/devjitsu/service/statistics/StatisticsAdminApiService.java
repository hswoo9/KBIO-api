package egovframework.com.devjitsu.service.statistics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import com.google.analytics.data.v1beta.*;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.statistics.StatisticsUserAccessDto;
import egovframework.com.devjitsu.model.statistics.StatisticsUserDto;
import egovframework.com.devjitsu.model.user.QTblUser;
import egovframework.com.devjitsu.model.user.QTblUserLgnHstry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class StatisticsAdminApiService {

    @Autowired
    private ResourceLoader resourceLoader;

    private final EntityManager em;
    /**
     * jpa 부등호
     * gt : >
     * lt : <
     * goe : >=
     * loe : <=
     */
    /**
     *  query DSL 조건 추가하는 방법
     *  BooleanBuilder builder = new BooleanBuilder();
     *  builder.and(qTblComCdGroup.actvtnYn.eq("Y"));
     * */

    public ResultVO getTypeStatistics(SearchDto searchDto) throws Exception {
        ResultVO resultVO = new ResultVO();

        List<Map<String, Object>> cityData = new ArrayList<>();

        if(searchDto.get("page").equals("byRegionUser")){
            addCityData(cityData, "Jeonju-si", "전주시");
            addCityData(cityData, "Iksan-si", "익산시");
            addCityData(cityData, "Gunsan-si", "군산시");
            addCityData(cityData, "Jeongeup-si", "정읍시");
            addCityData(cityData, "Gimje-si", "김제시");
            addCityData(cityData, "Namwon-si", "남원시");
            addCityData(cityData, "Wanju-gun", "완주군");
            addCityData(cityData, "Gochang-gun", "고창군");
            addCityData(cityData, "Buan-gun", "부안군");
            addCityData(cityData, "Imsil-gun", "임실군");
            addCityData(cityData, "Sunchang-gun", "순창군");
            addCityData(cityData, "Jinan-gun", "진안군");
            addCityData(cityData, "Muju-gun", "무주군");
            addCityData(cityData, "Jangsu-gun", "장수군");
        }

        List<Map<String, Object>> returnList = new ArrayList<>();

        // GoogleCredentials 생성 및 범위 설정
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(String.valueOf(resourceLoader.getResource("classpath:/static/googleApiKey/jbok-429204-507832e5f593.json").getFile().toPath())))
                .createScoped("https://www.googleapis.com/auth/analytics.readonly");

        // BetaAnalyticsDataSettings를 사용하여 클라이언트 설정
        BetaAnalyticsDataSettings settings = BetaAnalyticsDataSettings.newBuilder()
                .setCredentialsProvider(() -> credentials)
                .build();

        // BetaAnalyticsDataClient 생성
        try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create(settings)) {
            ObjectMapper objectMapper = new ObjectMapper();
            String[] dimensions = objectMapper.readValue(searchDto.get("dimensions").toString(), String[].class);
            String[] metrics = objectMapper.readValue(searchDto.get("metrics").toString(), String[].class);

            RunReportRequest.Builder requestBuilder = RunReportRequest.newBuilder()
                    .setProperty("properties/421523110")
                    .addDateRanges(DateRange.newBuilder().setStartDate(searchDto.get("startDate").toString()).setEndDate(searchDto.get("endDate").toString()));

            // 차원 추가
            for (String dimension : dimensions) {
                requestBuilder.addDimensions(Dimension.newBuilder().setName(dimension));
            }

            // 메트릭 추가
            for (String metric : metrics) {
                requestBuilder.addMetrics(Metric.newBuilder().setName(metric));
            }

            RunReportRequest request = requestBuilder.build();

            // 보고서 실행
            RunReportResponse response = analyticsData.runReport(request);

            // 결과 출력
            Map<String, Object> otherCityMap = new HashMap<>();
            otherCityMap.put("korCityName", "기타");
            otherCityMap.put("city", "Others");
            for (Row row : response.getRowsList()) {
                Map<String, Object> map = new HashMap<>();

                // 차원 값 처리
                String yearValue = "";
                String monthValue = "";

                for(int i = 0; i < dimensions.length; i++){
                    String dimensionValue = row.getDimensionValues(i).getValue();
                    if(dimensionValue.equals("(not set)")){
                        map.put(dimensions[i], "알 수 없음");
                    } else if(dimensionValue.equals("(direct)")){
                        map.put(dimensions[i], "다이렉트");
                    } else if (dimensions[i].equals("year")) {
                        yearValue = dimensionValue;
                        map.put("date", yearValue);
                        map.put("type", "year");
                    } else if (dimensions[i].equals("month")) {
                        monthValue = dimensionValue;
                        map.put("date", yearValue + monthValue + "01");
                        map.put("type", "month");
                    } else {
                        map.put("type", "date");
                        map.put(dimensions[i], dimensionValue);
                    }

                    System.out.printf(dimensions[i] + ": %s\n", row.getDimensionValues(i).getValue());
                }

                if(searchDto.get("page").equals("userSts")){
                    if(map.get("type").equals("year")){
                        searchDto.put("searchDate", yearValue);
                    }else if(map.get("type").equals("month")){
                        searchDto.put("searchDate", yearValue + monthValue);
                    }else {
                        searchDto.put("searchDate", map.get("date"));
                    }

//                    map.putAll(userManagementRepository.getUserJoinStsByDate(searchDto));
                }

                for(int i = 0; i < metrics.length; i++){
                    map.put(metrics[i], row.getMetricValues(i).getValue());
                    System.out.printf(metrics[i] + ": %s\n", row.getMetricValues(i).getValue());
                }

                if(searchDto.get("page").equals("byRegionUser")){
                    boolean cityDataInclude = false;
                    for (Map<String, Object> city : cityData) {
                        if (map.get("city").equals(city.get("engCityName"))) {
                            cityDataInclude = true;
                            searchDto.put("cityName", city.get("korCityName"));
                            map.put("korCityName", city.get("korCityName"));
//                            map.putAll(userManagementRepository.getUserJoinStsByDateBetween(searchDto));
                            returnList.add(map);
                            break;
                        }
                    }

                    if(!cityDataInclude){
                        otherCityMap.put("totalUsers",
                                (otherCityMap.get("totalUsers") == null ? 0 : Integer.parseInt(otherCityMap.get("totalUsers").toString())) +
                                        (map.get("totalUsers") == null ? 0 : Integer.parseInt(map.get("totalUsers").toString())));
                        otherCityMap.put("activeUsers",
                                (otherCityMap.get("activeUsers") == null ? 0 : Integer.parseInt(otherCityMap.get("activeUsers").toString())) +
                                        (map.get("activeUsers") == null ? 0 : Integer.parseInt(map.get("activeUsers").toString())));
//                        if(StringUtils.isEmpty(otherCityMap.get("NEW_JOIN_USER"))){
//                            searchDto.put("cityName", "others");
//                            otherCityMap.putAll(userManagementRepository.getUserJoinStsByDateBetween(params));
//                        }
                    }
                }else if(searchDto.get("page").equals("content")){
                    String pagePath = (String) map.get("pagePathPlusQueryString");
                    Map<String, Object> queryMap = new HashMap<>();
                    if(pagePath.indexOf("?") > -1){
                        String[] pairs = pagePath.substring(pagePath.indexOf("?") + 1).split("&");
                        for (String pair : pairs) {
                            String[] keyValue = pair.split("=");
                            if (keyValue.length > 1) {
                                queryMap.put(keyValue[0], keyValue[1]);
                            } else {
                                queryMap.put(keyValue[0], "");
                            }
                        }
                    }

                    if(
                            pagePath.indexOf("/index.do") == -1 &&
                                    (pagePath.indexOf("/content/contentView.do") > -1 || pagePath.indexOf("/compDiff/consttPoolList.do") > -1 || pagePath.indexOf("/compDiff/consttView.do") > -1 ||
                                            pagePath.indexOf("/board") > -1 || pagePath.indexOf("/spWork/supportBusinessList.do") > -1 || pagePath.indexOf("/spWork/supportBDetailedList.do") > -1 ||
                                            pagePath.indexOf("/spWork/supportOtherBusinessList.do") > -1 || pagePath.indexOf("/spWork/supportBusinessCalList.do") > -1 || pagePath.indexOf("/spWork/supportBusinessView.do") > -1 ||
                                            pagePath.indexOf("/join/joinTypeSel.do") > -1 || pagePath.indexOf("/login.do") > -1) &&
                                    !pagePath.equals("/logoutAction.do") && !pagePath.equals("/")
                    ){
//                        Map<String, Object> menu = menuMngrRepository.getMenu(queryMap);
//                        if(menu != null){
//                            String menuPathName = (String) menu.get("MENU_NAME_PATH");
//                            if(menuPathName.indexOf("관리") == -1){
//                                map.put("pageName", menu.get("MENU_NAME"));
//                                returnList.add(map);
//                            }
//                        }
                    }
                }else{
                    returnList.add(map);
                }
            }

            if(searchDto.get("page").equals("byRegionUser")){
                returnList.add(otherCityMap);
            }
        }

        resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        resultVO.putResult("rs", returnList);

        return resultVO;
    }

    private static void addCityData(List<Map<String, Object>> cityData, String engCityName, String korCityName) {
        Map<String, Object> cityMap = new HashMap<>();
        cityMap.put("engCityName", engCityName);
        cityMap.put("korCityName", korCityName);
        cityData.add(cityMap);
    }

    public ResultVO getStatisticsUser(SearchDto dto) {
        ResultVO resultVO = new ResultVO();

        try {
            JPAQueryFactory q = new JPAQueryFactory(em);
            QTblUser qTblUser = QTblUser.tblUser;

            BooleanBuilder builder = new BooleanBuilder();
            builder.and(
                qTblUser.mbrType.eq(1L).or(qTblUser.mbrType.eq(2L)).or(qTblUser.mbrType.eq(3L)).or(qTblUser.mbrType.eq(4L))
            ).and(
                qTblUser.joinYmd.isNotNull()
            )
            .and(
                Expressions.stringTemplate("DATE_FORMAT({0}, '%Y')", qTblUser.joinYmd).goe(
                    Expressions.stringTemplate("{0}", dto.get("searchYear"))
                )
            ).and(
                Expressions.stringTemplate("DATE_FORMAT({0}, '%m')", qTblUser.joinYmd).goe(
                    Expressions.stringTemplate("{0}", dto.get("searchMonth"))
                )
            );

            List<StatisticsUserDto> statisticsUser = q
                    .select(
                        Projections.constructor(
                            StatisticsUserDto.class,
                            qTblUser.mbrType,
                            qTblUser.count()
                        )
                    )
                    .from(qTblUser)
                    .where(builder)
                    .groupBy(qTblUser.mbrType)
                    .orderBy(
                        new CaseBuilder()
                            .when(qTblUser.mbrType.eq(1L)).then(0)
                            .when(qTblUser.mbrType.eq(3L)).then(1)
                            .when(qTblUser.mbrType.eq(4L)).then(2)
                            .when(qTblUser.mbrType.eq(2L)).then(3)
                            .otherwise(4)
                            .asc()
                    )
                    .fetch();

            resultVO.putResult("statisticsUser", statisticsUser);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getStatisticsUserAccess(SearchDto dto) {
        ResultVO resultVO = new ResultVO();

        try {
            JPAQueryFactory q = new JPAQueryFactory(em);
            QTblUser qTblUser = QTblUser.tblUser;
            QTblUserLgnHstry qTblUserLgnHstry = QTblUserLgnHstry.tblUserLgnHstry;

            BooleanBuilder builder = new BooleanBuilder();
            builder.and(
                qTblUser.mbrType.eq(1L).or(qTblUser.mbrType.eq(2L)).or(qTblUser.mbrType.eq(3L)).or(qTblUser.mbrType.eq(4L))
            ).and(
                Expressions.stringTemplate("DATE_FORMAT({0}, '%Y')", qTblUserLgnHstry.lgnDt).eq(
                    Expressions.stringTemplate("{0}", dto.get("searchYear"))
                )
            ).and(
                Expressions.stringTemplate("DATE_FORMAT({0}, '%m')", qTblUserLgnHstry.lgnDt).eq(
                    Expressions.stringTemplate("{0}", dto.get("searchMonth"))
                )
            );

            if(!StringUtils.isEmpty(dto.get("mbrType"))){
                builder.and(qTblUser.mbrType.eq(Long.valueOf(dto.get("mbrType").toString())));
            }

            StringTemplate dayFormat = Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", qTblUserLgnHstry.lgnDt);
            List<StatisticsUserAccessDto> statisticsUserAccess = q
                    .select(
                        Projections.constructor(
                            StatisticsUserAccessDto.class,
                            Expressions.numberTemplate(Long.class,
                                    "SUM(CASE WHEN {0} = 1 THEN 1 ELSE 0 END)", qTblUser.mbrType).as("mbrType1Count"),
                            Expressions.numberTemplate(Long.class,
                                    "SUM(CASE WHEN {0} = 3 THEN 1 ELSE 0 END)", qTblUser.mbrType).as("mbrType3Count"),
                            Expressions.numberTemplate(Long.class,
                                    "SUM(CASE WHEN {0} = 4 THEN 1 ELSE 0 END)", qTblUser.mbrType).as("mbrType4Count"),
                            Expressions.numberTemplate(Long.class,
                                    "SUM(CASE WHEN {0} = 2 THEN 1 ELSE 0 END)", qTblUser.mbrType).as("mbrType2Count"),
                            dayFormat
                        )
                    )
                    .from(qTblUserLgnHstry)
                    .join(qTblUser).on(qTblUser.userSn.eq(qTblUserLgnHstry.userSn))
                    .where(builder)
                    .groupBy(dayFormat)
                    .orderBy(dayFormat.asc())
                    .fetch();

            resultVO.putResult("statisticsUserAccess", statisticsUserAccess);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }
}
