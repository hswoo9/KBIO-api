package egovframework.com.devjitsu.service.main;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.bannerPopup.BannerPopupDto;
import egovframework.com.devjitsu.model.bannerPopup.QTblBnrPopup;
import egovframework.com.devjitsu.model.bbs.QTblPst;
import egovframework.com.devjitsu.model.bbs.TblBbs;
import egovframework.com.devjitsu.model.bbs.TblPst;
import egovframework.com.devjitsu.model.common.QTblComFile;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.consult.QTblCnsltAply;
import egovframework.com.devjitsu.model.consult.QTblDfclMttr;
import egovframework.com.devjitsu.model.main.MainStatusDto;
import egovframework.com.devjitsu.model.statistics.StatisticsUserAccessDto;
import egovframework.com.devjitsu.model.user.QTblMvnEnt;
import egovframework.com.devjitsu.model.user.QTblUser;
import egovframework.com.devjitsu.model.user.TblMvnEnt;
import egovframework.com.devjitsu.repository.bbs.TblBbsRepository;
import egovframework.com.devjitsu.service.bbs.BbsAdminApiService;
import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ManagerMainApiService {

    private final EntityManager em;
    private final TblBbsRepository tblBbsRepository;

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    @Autowired
    private BbsAdminApiService bbsAdminApiService;

    /**
     * jpa 부등호
     * gt : >
     * lt : <
     * goe : >=
     * loe : <=
     */
    /**
     * query DSL 조건 추가하는 방법
     * BooleanBuilder builder = new BooleanBuilder();
     * builder.and(qTblComCdGroup.actvtnYn.eq("Y"));
     */

    public ResultVO getStatus() {
        ResultVO resultVO = new ResultVO();

        try {
            MainStatusDto mainStatusDto = new MainStatusDto();
            getOperationalStatus(mainStatusDto);
            getConsultingStatus(mainStatusDto);
            getJoinStatus(mainStatusDto);

            resultVO.putResult("mainStatus", mainStatusDto);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public void getOperationalStatus(MainStatusDto mainStatusDto) {
        JPAQueryFactory q = new JPAQueryFactory(em);
        QTblUser qTblUser = QTblUser.tblUser;
        QTblDfclMttr qTblDfclMttr = QTblDfclMttr.tblDfclMttr;

        Tuple result = q.select(
            Expressions.numberTemplate(Long.class,
            "SUM(CASE WHEN {0} = 1 THEN 1 ELSE 0 END)", qTblUser.mbrType).as("mbrType1Count"),
            Expressions.numberTemplate(Long.class,
            "SUM(CASE WHEN {0} = 2 THEN 1 ELSE 0 END)", qTblUser.mbrType).as("mbrType2Count"),
            Expressions.numberTemplate(Long.class,
            "SUM(CASE WHEN {0} = 3 THEN 1 ELSE 0 END)", qTblUser.mbrType).as("mbrType3Count"),
            Expressions.numberTemplate(Long.class,
            "SUM(CASE WHEN {0} = 4 THEN 1 ELSE 0 END)", qTblUser.mbrType).as("mbrType4Count")
        ).from(qTblUser)
        .where(qTblUser.actvtnYn.eq("Y")).fetchFirst();

        if (result != null) {
            mainStatusDto.setMbrType1Cnt(result.get(0, Long.class));
            mainStatusDto.setMbrType2Cnt(result.get(1, Long.class));
            mainStatusDto.setMbrType3Cnt(result.get(2, Long.class));
            mainStatusDto.setMbrType4Cnt(result.get(3, Long.class));
        }

        mainStatusDto.setDfclCnt(q.selectFrom(qTblDfclMttr).where(qTblDfclMttr.actvtnYn.eq("Y")).fetchCount());
    }

    public void getConsultingStatus(MainStatusDto mainStatusDto) {
        JPAQueryFactory q = new JPAQueryFactory(em);
        QTblCnsltAply qTblCnsltAply = QTblCnsltAply.tblCnsltAply;

        Tuple result = q.select(
                    Expressions.numberTemplate(Long.class,
                            "SUM(CASE WHEN {0} = 26 THEN 1 ELSE 0 END)", qTblCnsltAply.cnsltSe),
                    Expressions.numberTemplate(Long.class,
                            "SUM(CASE WHEN {0} = 27 THEN 1 ELSE 0 END)", qTblCnsltAply.cnsltSe)
                )
                .from(qTblCnsltAply)
                .where(qTblCnsltAply.actvtnYn.eq("Y"))
                .fetchFirst();

        if (result != null) {
            mainStatusDto.setCnsltAply26Cnt(result.get(0, Long.class));
            mainStatusDto.setCnsltAply27Cnt(result.get(1, Long.class));
        }
    }

    public void getJoinStatus(MainStatusDto mainStatusDto) {
        JPAQueryFactory q = new JPAQueryFactory(em);
        QTblUser qTblUser = QTblUser.tblUser;

        Tuple result = q.select(
                        Expressions.numberTemplate(Long.class,
                                "SUM(CASE WHEN {0} = 'Y' THEN 1 ELSE 0 END)", qTblUser.actvtnYn).as("activeYCnt"),
                        Expressions.numberTemplate(Long.class,
                                "SUM(CASE WHEN {0} = 'W' THEN 1 ELSE 0 END)", qTblUser.actvtnYn).as("activeWCnt"),
                        Expressions.numberTemplate(Long.class,
                                "SUM(CASE WHEN {0} = 'R' THEN 1 ELSE 0 END)", qTblUser.actvtnYn).as("activeRCnt"),
                        Expressions.numberTemplate(Long.class,
                                "SUM(CASE WHEN {0} = 'S' THEN 1 ELSE 0 END)", qTblUser.actvtnYn).as("activeSCnt")
                ).from(qTblUser).fetchFirst();

        if (result != null) {
            mainStatusDto.setActiveYCnt(result.get(0, Long.class));
            mainStatusDto.setActiveWCnt(result.get(1, Long.class));
            mainStatusDto.setActiveRCnt(result.get(2, Long.class));
            mainStatusDto.setActiveSCnt(result.get(3, Long.class));
        }
    }
}
