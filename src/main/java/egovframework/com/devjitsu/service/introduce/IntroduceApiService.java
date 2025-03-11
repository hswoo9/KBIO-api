package egovframework.com.devjitsu.service.introduce;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;

import egovframework.com.devjitsu.model.common.QTblComCd;
import egovframework.com.devjitsu.model.common.QTblComFile;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.common.TblComFile;
import egovframework.com.devjitsu.model.user.*;

import egovframework.com.devjitsu.model.user.QTblMvnEnt;
import egovframework.com.devjitsu.repository.code.TblComCdRepository;
import egovframework.com.devjitsu.repository.common.TblComFileRepository;
import egovframework.com.devjitsu.repository.user.TblMvnEntMbrRepository;
import egovframework.com.devjitsu.repository.user.TblMvnEntRepository;

import egovframework.com.devjitsu.repository.user.TblRelInstRepository;
import egovframework.com.devjitsu.repository.user.TblUserRepository;
import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class IntroduceApiService {

    private final EntityManager em;
    private final TblMvnEntRepository tblMvnEntRepository;
    private final TblMvnEntMbrRepository tblMvnEntMbrRepository;
    private final TblUserRepository tblUserRepository;
    private final TblRelInstRepository tblRelInstRepository;
    private final TblComFileRepository tblComFileRepository;
    private final TblComCdRepository tblComCdRepository;

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    public ResultVO getOperationalList(SearchDto dto){
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();

        try{
            if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
                paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
            }
            paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));

            QTblMvnEnt qTblMvnEnt = QTblMvnEnt.tblMvnEnt;
            QTblComCd qTblComCd = QTblComCd.tblComCd;
            QTblComFile qTblComFile = QTblComFile.tblComFile;
            JPAQueryFactory q = new JPAQueryFactory(em);

            JPQLQuery<String> entClsfNm = JPAExpressions
                    .select(qTblComCd.comCdNm)
                    .from(qTblComCd)
                    .where(
                            qTblComCd.cdGroupSn.eq(17L).and(qTblComCd.comCd.eq(qTblMvnEnt.entClsf))
                    );

            BooleanBuilder builder = new BooleanBuilder();
            builder.and(qTblMvnEnt.actvtnYn.eq("Y")
                    .and(Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", qTblMvnEnt.rlsBgngYmd).loe(
                            Expressions.stringTemplate("DATE_FORMAT(NOW(), '%Y-%m-%d')")))
                    .and(Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", qTblMvnEnt.rlsEndYmd).goe(
                            Expressions.stringTemplate("DATE_FORMAT(NOW(), '%Y-%m-%d')"))));

            builder.and(qTblMvnEnt.rlsYn.eq("Y"));

            if (!StringUtils.isEmpty(dto.get("searchType"))) {
                if(dto.get("searchType").equals("mvnEntNm")){
                    builder.and(qTblMvnEnt.mvnEntNm.contains((String) dto.get("searchVal")));
                }else if(dto.get("searchType").equals("rpsvNm")){
                    builder.and(qTblMvnEnt.rpsvNm.contains((String) dto.get("searchVal")));
                }
            }else{
                builder.and(
                        qTblMvnEnt.mvnEntNm.contains((String) dto.get("searchVal"))
                                .or(qTblMvnEnt.rpsvNm.contains((String) dto.get("searchVal")))
                );
            }

            List<TblMvnEntDto> tblMvnEntList = q
                    .select(
                            Projections.constructor(
                                    TblMvnEntDto.class,
                                    qTblMvnEnt,
                                    qTblComFile,
                                    entClsfNm
                            )
                    ).from(qTblMvnEnt)
                    .leftJoin(qTblComFile).on(
                        qTblComFile.psnTblSn.eq(
                            Expressions.stringTemplate("CONCAT('mvnEnt_', {0})", qTblMvnEnt.mvnEntSn)
                        )
                    )
                    .where(builder)
                    .orderBy(qTblMvnEnt.frstCrtDt.desc())
                    .offset(paginationInfo.getFirstRecordIndex())
                    .limit(paginationInfo.getRecordCountPerPage()).fetch();

            Long totCnt = q.select(qTblMvnEnt.count()).from(qTblMvnEnt).where(builder).fetchOne();

            if(totCnt == null) totCnt = 0L;
            paginationInfo.setTotalRecordCount(totCnt.intValue());

            resultVO.putResult("getOperationalList",tblMvnEntList);
            resultVO.putPaginationInfo(paginationInfo);

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch(NullPointerException e){
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }


        return resultVO;
    }

    public ResultVO getOperationalDetail(TblMvnEnt tblMvnEnt) {
        ResultVO resultVO = new ResultVO();

        try{
            tblMvnEnt = tblMvnEntRepository.findByMvnEntSn(tblMvnEnt.getMvnEntSn());
            tblMvnEnt.setTblComFile(tblComFileRepository.findByPsnTblSn("mvnEnt_" + tblMvnEnt.getMvnEntSn()));
            tblMvnEnt.setEntTpbizNm(tblComCdRepository.findComCdNm(18L, tblMvnEnt.getEntTpbiz()));
            resultVO.putResult("operational", tblMvnEnt);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (NullPointerException e){
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getRelatedList(SearchDto dto){
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();

        try{
            if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
                paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
            }
            paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));


            QTblRelInst qTblRelInst = QTblRelInst.tblRelInst;
            QTblComCd qTblComCd = QTblComCd.tblComCd;
            QTblComFile qTblComFile = QTblComFile.tblComFile;
            JPAQueryFactory q = new JPAQueryFactory(em);

            JPQLQuery<String> clsfNm = JPAExpressions
                    .select(qTblComCd.comCdNm)
                    .from(qTblComCd)
                    .where(
                            qTblComCd.cdGroupSn.eq(17L).and(qTblComCd.comCd.eq(qTblRelInst.clsf))
                    );

            BooleanBuilder builder = new BooleanBuilder();
            qTblRelInst.actvtnYn.eq("Y")
                .and(Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", qTblRelInst.rlsBgngYmd).loe(
                    Expressions.stringTemplate("DATE_FORMAT(NOW(), '%Y-%m-%d')")))
                .and(Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", qTblRelInst.rlsEndYmd).goe(
                    Expressions.stringTemplate("DATE_FORMAT(NOW(), '%Y-%m-%d')")));


            if (!StringUtils.isEmpty(dto.get("searchType"))) {
                if(dto.get("searchType").equals("relInstNm")){
                    builder.and(qTblRelInst.relInstNm.contains((String) dto.get("searchVal")));
                }else if(dto.get("searchType").equals("rpsvNm")){
                    builder.and(qTblRelInst.rpsvNm.contains((String) dto.get("searchVal")));
                }
            }else{
                builder.and(
                        qTblRelInst.relInstNm.contains((String) dto.get("searchVal"))
                                .or(qTblRelInst.rpsvNm.contains((String) dto.get("searchVal")))
                );
            }

            List<TblRelInstDto> tblRelInstList = q
                    .select(
                        Projections.constructor(
                            TblRelInstDto.class,
                            qTblRelInst,
                            qTblComFile,
                            clsfNm
                        )
                    ).from(qTblRelInst)
                    .leftJoin(qTblComFile).on(
                        qTblComFile.psnTblSn.eq(
                            Expressions.stringTemplate("CONCAT('relInst_', {0})", qTblRelInst.relInstSn)
                        )
                    )
                    .where(builder)
                    .orderBy(qTblRelInst.frstCrtDt.desc())
                    .offset(paginationInfo.getFirstRecordIndex())
                    .limit(paginationInfo.getRecordCountPerPage()).fetch();

            Long totCnt = q.select(qTblRelInst.count()).from(qTblRelInst).where(builder).fetchOne();

            if(totCnt == null) totCnt = 0L;
            paginationInfo.setTotalRecordCount(totCnt.intValue());

            resultVO.putResult("getRelatedList",tblRelInstList);
            resultVO.putPaginationInfo(paginationInfo);

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch(NullPointerException e){
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }


        return resultVO;
    }


    public ResultVO getRelInstDetail(TblRelInst tblRelInst) {
        ResultVO resultVO = new ResultVO();

        try{
            tblRelInst = tblRelInstRepository.findByRelInstSn(tblRelInst.getRelInstSn());
            tblRelInst.setLogoFile(tblComFileRepository.findByPsnTblSn("relInst_" + tblRelInst.getRelInstSn()));
            tblRelInst.setTpbizNm(tblComCdRepository.findComCdNm(18L, tblRelInst.getTpbiz()));

            resultVO.putResult("related", tblRelInst);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (NullPointerException e){
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }
}
