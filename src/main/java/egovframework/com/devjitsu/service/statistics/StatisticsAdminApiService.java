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
import egovframework.com.devjitsu.model.bbs.QTblBbs;
import egovframework.com.devjitsu.model.bbs.QTblPst;
import egovframework.com.devjitsu.model.common.QTblAtchFileDwnldCnt;
import egovframework.com.devjitsu.model.common.QTblPstCntnHstry;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.statistics.StatisticsDto;
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

        List<Map<String, Object>> regionData = new ArrayList<>();

        if(searchDto.get("page").equals("regionUser")){
            addRegionData(regionData, "Seoul", "서울특별시", "서울");
            addRegionData(regionData, "Busan", "부산광역시", "부산");
            addRegionData(regionData, "Daegu", "대구광역시", "대구");
            addRegionData(regionData, "Incheon", "인천광역시", "인천");
            addRegionData(regionData, "Gwangju", "광주광역시", "광주");
            addRegionData(regionData, "Daejeon", "대전광역시", "대전");
            addRegionData(regionData, "Ulsan", "울산광역시", "울산");
            addRegionData(regionData, "Sejong City", "세종특별자치시", "세종특별자치시");
            addRegionData(regionData, "Gyeonggi-do", "경기도", "경기");
            addRegionData(regionData, "Gangwon-do", "강원특별자치도", "강원특별자치도");
            addRegionData(regionData, "Chungcheongbuk-do", "충청북도", "충북");
            addRegionData(regionData, "Chungcheongnam-do", "충청남도", "충남");
            addRegionData(regionData, "Jeonbuk State", "전북특별자치도", "전북특별자치도");
            addRegionData(regionData, "Jeollanam-do", "전라남도", "전남");
            addRegionData(regionData, "Gyeongsangbuk-do", "경상북도", "경북");
            addRegionData(regionData, "Gyeongsangnam-do", "경상남도", "경남");
            addRegionData(regionData, "Jeju-do", "제주특별자치도", "제주특별자치도");
        }

        List<Map<String, Object>> returnList = new ArrayList<>();

        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(String.valueOf(resourceLoader.getResource("classpath:/static/googleApiKey/kbio-447309-ba4bfac8ab2a.json").getFile().toPath())))
                .createScoped("https://www.googleapis.com/auth/analytics.readonly");

        BetaAnalyticsDataSettings settings = BetaAnalyticsDataSettings.newBuilder()
                .setCredentialsProvider(() -> credentials)
                .build();

        try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create(settings)) {
            ObjectMapper objectMapper = new ObjectMapper();
            String[] dimensions = objectMapper.readValue(searchDto.get("dimensions").toString(), String[].class);
            String[] metrics = objectMapper.readValue(searchDto.get("metrics").toString(), String[].class);

            RunReportRequest.Builder requestBuilder = RunReportRequest.newBuilder()
                    .setProperty("properties/478528722")
                    .addDateRanges(DateRange.newBuilder().setStartDate(searchDto.get("startDate").toString()).setEndDate(searchDto.get("endDate").toString()));

            for (String dimension : dimensions) {
                requestBuilder.addDimensions(Dimension.newBuilder().setName(dimension));
            }

            for (String metric : metrics) {
                requestBuilder.addMetrics(Metric.newBuilder().setName(metric));
            }

            RunReportRequest request = requestBuilder.build();
            RunReportResponse response = analyticsData.runReport(request);

            JPAQueryFactory q = new JPAQueryFactory(em);
            QTblUser qTblUser = QTblUser.tblUser;
            BooleanBuilder builder = new BooleanBuilder();

            Map<String, Object> otherRegionMap = new HashMap<>();
//            otherRegionMap.put("korRegionName", "기타");
//            otherRegionMap.put("region", "Others");
            otherRegionMap.put("korRegionName", "세종특별자치시");
            otherRegionMap.put("korAddrName", "세종특별자치시");
            otherRegionMap.put("region", "Sejong City");
            otherRegionMap.put("engRegionName", "Sejong City");

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

                    StatisticsUserAccessDto statisticsUserAccessDto = q.select(
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
                            Expressions.constant(searchDto.get("searchDate").toString())
                        )
                    )
                    .from(qTblUser)
                    .where(
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y%m%d')", qTblUser.joinYmd).like(searchDto.get("searchDate").toString()+ "%")
                    ).fetchFirst();

                    map.put("newUserCnt", statisticsUserAccessDto);
                }

                for(int i = 0; i < metrics.length; i++){
                    map.put(metrics[i], row.getMetricValues(i).getValue());
                    System.out.printf(metrics[i] + ": %s\n", row.getMetricValues(i).getValue());
                }

                if(searchDto.get("page").equals("regionUser")){
                    boolean regionDataInclude = false;
                    for (Map<String, Object> region : regionData) {
                        if (map.get("region").equals(region.get("engRegionName"))) {
                            regionDataInclude = true;
                            searchDto.put("regionName", region.get("korRegionName"));
                            map.put("korRegionName", region.get("korRegionName"));
                            map.put("newUserCnt", regionJoinUser(searchDto, region));
                            returnList.add(map);
                            break;
                        }
                    }

                    if(!regionDataInclude && map.get("region").equals("알 수 없음")){
                        otherRegionMap.put("totalUsers", map.get("totalUsers"));
                        otherRegionMap.put("activeUsers", map.get("activeUsers"));
                        otherRegionMap.put("newUserCnt", regionJoinUser(searchDto, otherRegionMap));
                        returnList.add(otherRegionMap);
                    }
                }else if(searchDto.get("page").equals("content")){
//                    String pagePath = (String) map.get("pagePathPlusQueryString");
//                    Map<String, Object> queryMap = new HashMap<>();
//                    if(pagePath.indexOf("?") > -1){
//                        String[] pairs = pagePath.substring(pagePath.indexOf("?") + 1).split("&");
//                        for (String pair : pairs) {
//                            String[] keyValue = pair.split("=");
//                            if (keyValue.length > 1) {
//                                queryMap.put(keyValue[0], keyValue[1]);
//                            } else {
//                                queryMap.put(keyValue[0], "");
//                            }
//                        }
//                    }

//                    if(
//                            pagePath.indexOf("/index.do") == -1 &&
//                                    (pagePath.indexOf("/content/contentView.do") > -1 || pagePath.indexOf("/compDiff/consttPoolList.do") > -1 || pagePath.indexOf("/compDiff/consttView.do") > -1 ||
//                                            pagePath.indexOf("/board") > -1 || pagePath.indexOf("/spWork/supportBusinessList.do") > -1 || pagePath.indexOf("/spWork/supportBDetailedList.do") > -1 ||
//                                            pagePath.indexOf("/spWork/supportOtherBusinessList.do") > -1 || pagePath.indexOf("/spWork/supportBusinessCalList.do") > -1 || pagePath.indexOf("/spWork/supportBusinessView.do") > -1 ||
//                                            pagePath.indexOf("/join/joinTypeSel.do") > -1 || pagePath.indexOf("/login.do") > -1) &&
//                                    !pagePath.equals("/logoutAction.do") && !pagePath.equals("/")
//                    ){
//                        Map<String, Object> menu = menuMngrRepository.getMenu(queryMap);
//                        if(menu != null){
//                            String menuPathName = (String) menu.get("MENU_NAME_PATH");
//                            if(menuPathName.indexOf("관리") == -1){
//                                map.put("pageName", menu.get("MENU_NAME"));
//                                returnList.add(map);
//                            }
//                        }
//                    }
                }else{
                    returnList.add(map);
                }
            }
        }

        resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        resultVO.putResult("rs", returnList);

        return resultVO;
    }

    private static void addRegionData(List<Map<String, Object>> regionData, String engRegionName, String korRegionName, String korAddrname) {
        Map<String, Object> regionMap = new HashMap<>();
        regionMap.put("engRegionName", engRegionName);
        regionMap.put("korRegionName", korRegionName);
        regionMap.put("korAddrName", korAddrname);
        regionData.add(regionMap);
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
                Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m')", qTblUser.joinYmd).goe(
                    Expressions.stringTemplate("{0}", dto.get("searchYear") + "-" + dto.get("searchMonth"))
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
                Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m')", qTblUserLgnHstry.lgnDt).eq(
                    Expressions.stringTemplate("{0}", dto.get("searchYear") + "-" + dto.get("searchMonth"))
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

    public ResultVO getStatisticsPstAccess(SearchDto dto) {
        ResultVO resultVO = new ResultVO();

        try {
            JPAQueryFactory q = new JPAQueryFactory(em);
            QTblPstCntnHstry qTblPstCntnHstry = QTblPstCntnHstry.tblPstCntnHstry;

            BooleanBuilder builder = new BooleanBuilder();
            if(!StringUtils.isEmpty(dto.get("trgtTblNm"))){
                builder.and(qTblPstCntnHstry.trgtTblNm.eq(dto.get("trgtTblNm").toString()));
            }

            if(!StringUtils.isEmpty(dto.get("trgtSn"))){
                builder.and(qTblPstCntnHstry.trgtSn.eq(Long.valueOf(dto.get("trgtSn").toString())));
            }

            builder.and(
                Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m')", qTblPstCntnHstry.frstCrtDt).eq(
                    Expressions.stringTemplate("{0}", dto.get("searchYear") + "-" + dto.get("searchMonth"))
                )
            );

            StringTemplate dayFormat = Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", qTblPstCntnHstry.frstCrtDt);
            List<StatisticsDto> statisticsPstAccess = q
                    .select(
                        Projections.constructor(
                            StatisticsDto.class,
                            dayFormat,
                            qTblPstCntnHstry.cntnNmtm.sum().nullif(0L)
                        )
                    )
                    .from(qTblPstCntnHstry)
                    .where(builder)
                    .groupBy(dayFormat, qTblPstCntnHstry.trgtTblNm, qTblPstCntnHstry.trgtSn)
                    .orderBy(dayFormat.asc())
                    .fetch();

            resultVO.putResult("statisticsPstAccess", statisticsPstAccess);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getStatisticsPstFile(SearchDto dto) {
        ResultVO resultVO = new ResultVO();

        try {
            JPAQueryFactory q = new JPAQueryFactory(em);
            QTblAtchFileDwnldCnt qTblAtchFileDwnldCnt = QTblAtchFileDwnldCnt.tblAtchFileDwnldCnt;

            BooleanBuilder builder = new BooleanBuilder();

            if(!StringUtils.isEmpty(dto.get("trgtTblNm"))){
                builder.and(qTblAtchFileDwnldCnt.trgtTblNm.eq(dto.get("trgtTblNm").toString()));
            }

            if(!StringUtils.isEmpty(dto.get("trgtSn"))){
                builder.and(qTblAtchFileDwnldCnt.trgtSn.eq(Long.valueOf(dto.get("trgtSn").toString())));
            }

            builder.and(
                Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m')", qTblAtchFileDwnldCnt.frstCrtDt).eq(
                    Expressions.stringTemplate("{0}", dto.get("searchYear") + "-" + dto.get("searchMonth"))
                )
            );

            StringTemplate dayFormat = Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", qTblAtchFileDwnldCnt.frstCrtDt);
            List<StatisticsDto> statisticsPstFile = q
                    .select(
                        Projections.constructor(
                            StatisticsDto.class,
                            dayFormat,
                            qTblAtchFileDwnldCnt.atchFileDwnldCnt.sum().nullif(0L)
                        )
                    )
                    .from(qTblAtchFileDwnldCnt)
                    .where(builder)
                    .groupBy(dayFormat, qTblAtchFileDwnldCnt.trgtTblNm, qTblAtchFileDwnldCnt.trgtSn)
                    .orderBy(dayFormat.asc())
                    .fetch();

            resultVO.putResult("statisticsPstFile", statisticsPstFile);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public StatisticsUserAccessDto regionJoinUser(SearchDto searchDto, Map<String, Object> region){
        JPAQueryFactory q = new JPAQueryFactory(em);
        QTblUser qTblUser = QTblUser.tblUser;

        StatisticsUserAccessDto statisticsUserAccessDto = q.select(
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
                Expressions.constant("")
            )
        )
        .from(qTblUser)
        .where(
            Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", qTblUser.joinYmd).goe(
                Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", searchDto.get("startDate").toString())
            )
            .and(
                Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", qTblUser.joinYmd).loe(
                    Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", searchDto.get("endDate").toString())
                )
            ).and(qTblUser.addr.startsWith(region.get("korAddrName").toString()))
        ).fetchFirst();

        return statisticsUserAccessDto;
    }
}
