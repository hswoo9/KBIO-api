package egovframework.com.devjitsu.service.user;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;

import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.user.*;

import egovframework.com.devjitsu.repository.user.TblMvnEntMbrRepository;
import egovframework.com.devjitsu.repository.user.TblRelInstRepository;

import egovframework.com.devjitsu.repository.user.TblUserRepository;
import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RelInstApiService {

    private final EntityManager em;
    private final TblUserRepository tblUserRepository;
    private final TblRelInstRepository tblRelInstRepository;
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    public ResultVO setRelInst(TblRelInst tblRelInst){
        ResultVO resultVO = new ResultVO();

        try{
            QTblRelInst qTblRelInst = QTblRelInst.tblRelInst;
            JPAQueryFactory q = new JPAQueryFactory(em);

            tblRelInstRepository.save(tblRelInst);

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getRelInstList(SearchDto dto){
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();

        try{
            if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
                paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
            }
            paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));

            QTblRelInst qTblRelInst = QTblRelInst.tblRelInst;
            JPAQueryFactory q = new JPAQueryFactory(em);

            BooleanBuilder builder = new BooleanBuilder();

            if(!StringUtils.isEmpty(dto.get("relInstNm"))){
                builder.and(qTblRelInst.relInstNm.contains((String) dto.get("relInstNm")));
            }
            if(!StringUtils.isEmpty(dto.get("brno"))){
                builder.and(qTblRelInst.brno.contains((String) dto.get("brno")));
            }
            if(!StringUtils.isEmpty(dto.get("rpsvNm"))){
                builder.and(qTblRelInst.rpsvNm.contains((String) dto.get("rpsvNm")));
            }

            List<TblRelInst> tblRelInstList = q.selectFrom(qTblRelInst)
                    .where(builder)
                    .orderBy(qTblRelInst.frstCrtDt.desc())
                    .offset(paginationInfo.getFirstRecordIndex())
                    .limit(paginationInfo.getRecordCountPerPage()).fetch();

            Long totCnt = q.select(qTblRelInst.count()).from(qTblRelInst).where(builder).fetchOne();

            if(totCnt == null) totCnt = 0L;
            paginationInfo.setTotalRecordCount(totCnt.intValue());

            resultVO.putResult("rcList",tblRelInstList);
            resultVO.putPaginationInfo(paginationInfo);

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch(Exception e){
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }


        return resultVO;
    }

    public ResultVO getRc(TblRelInst tblRelInst) {
        ResultVO resultVO = new ResultVO();

        try{
            resultVO.putResult("rc",tblRelInstRepository.findByRelInstSn(tblRelInst.getRelInstSn()));
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }
}