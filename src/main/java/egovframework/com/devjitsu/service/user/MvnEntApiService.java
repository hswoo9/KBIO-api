package egovframework.com.devjitsu.service.user;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;

import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.user.TblMvnEnt;
import egovframework.com.devjitsu.model.user.QTblMvnEnt;

import egovframework.com.devjitsu.repository.user.TblMvnEntRepository;

import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MvnEntApiService {

    private final EntityManager em;
    private final TblMvnEntRepository tblMvnEntRepository;

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    public ResultVO setMvnEnt(TblMvnEnt tblMvnEnt){
        ResultVO resultVO = new ResultVO();

        try{
            QTblMvnEnt qTblMvnEnt = QTblMvnEnt.tblMvnEnt;
            JPAQueryFactory q = new JPAQueryFactory(em);

            tblMvnEntRepository.save(tblMvnEnt);

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getMvnEntList(SearchDto dto){
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
            if(!StringUtils.isEmpty(dto.get("mvnEntNm"))){
                builder.and(qTblMvnEnt.mvnEntNm.contains((String) dto.get("mvnEntNm")));
            }
            if(!StringUtils.isEmpty(dto.get("brno"))){
                builder.and(qTblMvnEnt.brno.contains((String) dto.get("brno")));
            }
            if(!StringUtils.isEmpty(dto.get("rpsvNm"))){
                builder.and(qTblMvnEnt.rpsvNm.contains((String) dto.get("rpsvNm")));
            }

            List<TblMvnEnt> tblMvnEntList = q.selectFrom(qTblMvnEnt)
                    .where(builder)
                    .orderBy(qTblMvnEnt.frstCrtDt.desc())
                    .offset(paginationInfo.getFirstRecordIndex())
                    .limit(paginationInfo.getRecordCountPerPage()).fetch();

            Long totCnt = q.select(qTblMvnEnt.count()).from(qTblMvnEnt).where(builder).fetchOne();

            if(totCnt == null) totCnt = 0L;
            paginationInfo.setTotalRecordCount(totCnt.intValue());

            resultVO.putResult("rcList",tblMvnEntList);
            resultVO.putPaginationInfo(paginationInfo);

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch(Exception e){
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }


        return resultVO;
    }

    public ResultVO getRc(TblMvnEnt tblMvnEnt) {
        ResultVO resultVO = new ResultVO();

        try{
            resultVO.putResult("rc",tblMvnEntRepository.findByMvnEntSn(tblMvnEnt.getMvnEntSn()));
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }


}
