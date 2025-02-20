package egovframework.com.devjitsu.service.introduce;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;

import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.user.*;

import egovframework.com.devjitsu.model.user.QTblMvnEnt;
import egovframework.com.devjitsu.repository.user.TblMvnEntMbrRepository;
import egovframework.com.devjitsu.repository.user.TblMvnEntRepository;

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
            JPAQueryFactory q = new JPAQueryFactory(em);

            BooleanBuilder builder = new BooleanBuilder();

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

            List<TblMvnEnt> tblMvnEntList = q.selectFrom(qTblMvnEnt)
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
        }catch(Exception e){
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }


        return resultVO;
    }

    public ResultVO getOperationalAllList(SearchDto dto){
        ResultVO resultVO = new ResultVO();
        try{
            QTblMvnEnt qTblMvnEnt = QTblMvnEnt.tblMvnEnt;
            JPAQueryFactory q = new JPAQueryFactory(em);

            BooleanBuilder builder = new BooleanBuilder();

            if(!StringUtils.isEmpty(dto.get("mvnEntNm"))){
                builder.and(qTblMvnEnt.mvnEntNm.contains((String) dto.get("mvnEntNm")));
            }
            if(!StringUtils.isEmpty(dto.get("actvtnYn"))){
                builder.and(qTblMvnEnt.actvtnYn.eq((String) dto.get("actvtnYn")));
            }
            List<TblMvnEnt> tblMvnEntList = q.selectFrom(qTblMvnEnt)
                    .where(builder)
                    .orderBy(qTblMvnEnt.frstCrtDt.desc())
                    .fetch();
            resultVO.putResult("operationalList",tblMvnEntList);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch(Exception e){
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }


        return resultVO;
    }

    public ResultVO getOperationalDetail(TblMvnEnt tblMvnEnt) {
        ResultVO resultVO = new ResultVO();

        try{
            resultVO.putResult("operational",tblMvnEntRepository.findByMvnEntSn(tblMvnEnt.getMvnEntSn()));
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            e.printStackTrace();
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
            JPAQueryFactory q = new JPAQueryFactory(em);

            BooleanBuilder builder = new BooleanBuilder();

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

            List<TblRelInst> tblRelInstList = q.selectFrom(qTblRelInst)
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
        }catch(Exception e){
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }


        return resultVO;
    }


    public ResultVO getRelInstDetail(TblRelInst tblRelInst) {
        ResultVO resultVO = new ResultVO();

        try{
            resultVO.putResult("related",tblRelInstRepository.findByRelInstSn(tblRelInst.getRelInstSn()));
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }
}
