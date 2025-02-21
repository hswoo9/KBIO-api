package egovframework.com.devjitsu.service.main;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.bannerPopup.BannerPopupDto;
import egovframework.com.devjitsu.model.bannerPopup.QTblBnrPopup;
import egovframework.com.devjitsu.model.bannerPopup.TblBnrPopup;
import egovframework.com.devjitsu.model.bbs.PstDto;
import egovframework.com.devjitsu.model.bbs.QTblPst;
import egovframework.com.devjitsu.model.bbs.TblBbs;
import egovframework.com.devjitsu.model.bbs.TblPst;
import egovframework.com.devjitsu.model.common.QTblComCd;
import egovframework.com.devjitsu.model.common.QTblComFile;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.user.QTblMvnEnt;
import egovframework.com.devjitsu.model.user.QTblUser;
import egovframework.com.devjitsu.model.user.TblMvnEnt;
import egovframework.com.devjitsu.repository.bbs.TblBbsRepository;
import egovframework.com.devjitsu.service.bbs.BbsAdminApiService;
import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MainApiService {

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

    public ResultVO getBnrPopupList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();

        try {
            JPAQueryFactory q = new JPAQueryFactory(em);
            QTblBnrPopup qTblBnrPopup = QTblBnrPopup.tblBnrPopup;
            QTblComFile qTblComFile = QTblComFile.tblComFile;

            BooleanBuilder builder = new BooleanBuilder();
            builder.and(qTblBnrPopup.actvtnYn.eq("Y"));
            if(!StringUtils.isEmpty(dto.get("bnrPopupKnd"))){
                builder.and(qTblBnrPopup.bnrPopupKnd.eq((String) dto.get("bnrPopupKnd")));

                if(dto.get("bnrPopupKnd").equals("popup")){
                    builder.and(qTblBnrPopup.popupBgngDt.loe(LocalDateTime.now()).and(qTblBnrPopup.popupEndDt.goe(LocalDateTime.now())));
                }
            }

            List<BannerPopupDto> bnrPopupList = q
                    .select(
                        Projections.constructor(
                            BannerPopupDto.class,
                            qTblBnrPopup,
                            qTblComFile
                        )
                    ).from(qTblBnrPopup)
                    .join(qTblComFile)
                    .on(
                        qTblComFile.psnTblSn.eq(
                            Expressions.stringTemplate("CONCAT('bnrPopup_', {0})", qTblBnrPopup.bnrPopupSn)
                        )
                    )
                    .where(builder)
                    .fetch();

            resultVO.putResult("bnrPopupList", bnrPopupList);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getMvnEntList() {
        ResultVO resultVO = new ResultVO();
        try{
            QTblMvnEnt qTblMvnEnt = QTblMvnEnt.tblMvnEnt;
            JPAQueryFactory q = new JPAQueryFactory(em);
            List<TblMvnEnt> tblMvnEntList = q.selectFrom(qTblMvnEnt)
                    .where(qTblMvnEnt.actvtnYn.eq("Y"))
                    .orderBy(qTblMvnEnt.frstCrtDt.desc()).fetch();
            resultVO.putResult("mvnEntList",tblMvnEntList);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch(Exception e){
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getPstList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();

        try {
            TblBbs tblBbs = tblBbsRepository.findByBbsSn(Long.parseLong(dto.get("bbsSn").toString()));

            QTblPst qTblPst = QTblPst.tblPst;
            JPAQueryFactory q = new JPAQueryFactory(em);

            BooleanBuilder builder = new BooleanBuilder();
            builder.and(qTblPst.actvtnYn.eq("Y"));
            builder.and(qTblPst.bbsSn.eq(Long.parseLong(dto.get("bbsSn").toString())));
            builder.and(qTblPst.upPstSn.isNull());

            List<PstDto> pstList = q
                    .select(
                        Projections.constructor(
                            PstDto.class,
                            qTblPst.pstSn,
                            new CaseBuilder()
                                    .when(qTblPst.upendNtcYn.eq("Y")
                                            .and(
                                                    Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", qTblPst.ntcBgngDt).loe(
                                                            Expressions.stringTemplate("DATE_FORMAT(NOW(), '%Y-%m-%d')")
                                                    )
                                            )
                                            .and(
                                                    Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", qTblPst.ntcEndDate).goe(
                                                            Expressions.stringTemplate("DATE_FORMAT(NOW(), '%Y-%m-%d')")
                                                    )
                                            )
                                    )
                                    .then("Y")
                                    .otherwise("N").as("upendNtcYn"),
                            qTblPst.bbsSn,
                            Expressions.constant(""),
                            qTblPst.pstTtl,
                            qTblPst.pstCn,
                            qTblPst.pstInqCnt,
                            qTblPst.rlsYn,
                            qTblPst.actvtnYn,
                            qTblPst.upPstSn,
                            qTblPst.creatrSn,
                            Expressions.constant(""),
                            qTblPst.prvtPswd,
                            qTblPst.frstCrtDt,
                            Expressions.constant(0L),
                            Expressions.constant(""),
                            Expressions.constant("")
                        )
                    )
                    .from(qTblPst)
                    .where(builder)
                    .orderBy(
                        new CaseBuilder()
                            .when(qTblPst.upendNtcYn.eq("Y")
                                .and(
                                    Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", qTblPst.ntcBgngDt).loe(
                                        Expressions.stringTemplate("DATE_FORMAT(NOW(), '%Y-%m-%d')")
                                    )
                                )
                                .and(
                                    Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", qTblPst.ntcEndDate).goe(
                                        Expressions.stringTemplate("DATE_FORMAT(NOW(), '%Y-%m-%d')")
                                    )
                                )
                            )
                            .then(0)
                            .otherwise(1)
                            .asc(),  // ASC로 정렬
                        qTblPst.pstGroup.desc(),
                        qTblPst.ansStp.asc(),
                        qTblPst.frstCrtDt.desc())
                    .offset(0)
                    .limit(2)
                    .fetch();

            resultVO.putResult("bbs", tblBbs);
            resultVO.putResult("pstList", pstList);

            if (!StringUtils.isEmpty(dto.get("userSn"))) {
                /** 사용자 권한 불러오기 */
                resultVO.putResult("authrt", bbsAdminApiService.getUserBbsAuthrt(tblBbs, Long.parseLong(dto.get("userSn").toString())));
            }else{
                resultVO.putResult("authrt", bbsAdminApiService.getUserBbsAuthrt(tblBbs, null));
            }
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }
}
