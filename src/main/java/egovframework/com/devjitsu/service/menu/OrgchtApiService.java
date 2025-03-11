package egovframework.com.devjitsu.service.menu;

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
import egovframework.com.devjitsu.model.bbs.PstDto;
import egovframework.com.devjitsu.model.bbs.QTblPst;
import egovframework.com.devjitsu.model.bbs.TblBbs;
import egovframework.com.devjitsu.model.common.QTblComCd;
import egovframework.com.devjitsu.model.common.QTblComFile;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.menu.QTblMenu;
import egovframework.com.devjitsu.model.menu.TblContent;
import egovframework.com.devjitsu.model.menu.TblMenu;
import egovframework.com.devjitsu.model.user.QTblOrgcht;
import egovframework.com.devjitsu.model.user.QTblUser;
import egovframework.com.devjitsu.model.user.TblOrgcht;
import egovframework.com.devjitsu.model.user.TblOrgchtDTO;
import egovframework.com.devjitsu.repository.menu.TblContentRepository;
import egovframework.com.devjitsu.repository.menu.TblMenuRepository;
import egovframework.com.devjitsu.repository.user.TblOrgchtRepository;
import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrgchtApiService {

    private final EntityManager em;
    private final TblOrgchtRepository tblOrgchtRepository;

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    public ResultVO setOrgcht(TblOrgcht tblOrgcht){
        ResultVO resultVO = new ResultVO();
        try {
            tblOrgchtRepository.save(tblOrgcht);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (NullPointerException e){
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }
        return resultVO;
    }

    public ResultVO delOrgcht(TblOrgcht tblOrgcht){
        ResultVO resultVO = new ResultVO();
        try {
            tblOrgchtRepository.delete(tblOrgcht);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (NullPointerException e){
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }
        return resultVO;
    }

    public ResultVO getOrgcht(TblOrgcht tblOrgcht){
        ResultVO resultVO = new ResultVO();
        try {
            resultVO.putResult("orgcht", tblOrgchtRepository.findByOrgchtSn(tblOrgcht.getOrgchtSn()));
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (NullPointerException e){
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }
        return resultVO;
    }

    public ResultVO getOrgchtList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();
        try {

            JPAQueryFactory q = new JPAQueryFactory(em);
            BooleanBuilder builder = new BooleanBuilder();
            QTblOrgcht qTblOrgcht = QTblOrgcht.tblOrgcht;
            QTblComCd qTblComCd = QTblComCd.tblComCd;

            if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
                paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
            }

            if (!StringUtils.isEmpty(dto.get("pageSize"))) {
                paginationInfo.setPageSize(Integer.parseInt(dto.get("pageIndex").toString()));
            }else{
                paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));
            }

            if (!StringUtils.isEmpty(dto.get("pageUnit"))) {
                paginationInfo.setRecordCountPerPage(Integer.parseInt(dto.get("pageUnit").toString()));
            }else{
                paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            }

            if (!StringUtils.isEmpty(dto.get("deptSn"))) {
                builder.and(qTblOrgcht.deptSn.eq(Long.parseLong(dto.get("deptSn").toString())));
            }

            if (!StringUtils.isEmpty(dto.get("jbttlSn"))) {
                builder.and(qTblOrgcht.jbttlSn.eq(Long.parseLong(dto.get("jbttlSn").toString())));
            }

            if (!StringUtils.isEmpty(dto.get("searchType"))) {
                if(dto.get("searchType").toString().equals("kornFlnm")){
                    builder.and(qTblOrgcht.kornFlnm.contains(dto.get("searchVal").toString()));
                }else if(dto.get("searchType").toString().equals("tkcgTask")){
                    builder.and(qTblOrgcht.tkcgTask.contains(dto.get("searchVal").toString()));
                }else{
                    builder.and(qTblOrgcht.kornFlnm.contains(dto.get("searchVal").toString()).or(qTblOrgcht.tkcgTask.contains(dto.get("searchVal").toString())));
                }
            }

            List<TblOrgchtDTO> orgchtList = q.select(
                    Projections.constructor(
                            TblOrgchtDTO.class,
                            qTblOrgcht.orgchtSn,
                            qTblOrgcht.kornFlnm,
                            qTblOrgcht.email,
                            qTblOrgcht.telno,
                            qTblOrgcht.deptSn,
                            JPAExpressions.select(qTblComCd.comCdNm).from(qTblComCd).where(qTblComCd.comCdSn.eq(qTblOrgcht.deptSn)),
                            qTblOrgcht.jbttlSn,
                            JPAExpressions.select(qTblComCd.comCdNm).from(qTblComCd).where(qTblComCd.comCdSn.eq(qTblOrgcht.jbttlSn)),
                            qTblOrgcht.tkcgTask,
                            qTblOrgcht.sortSeq,
                            qTblOrgcht.actvtnYn,
                            qTblOrgcht.frstCrtDt
                    )
                ).from(qTblOrgcht)
                .where(builder)
                .orderBy(qTblOrgcht.sortSeq.asc())
                .offset(paginationInfo.getFirstRecordIndex())
                .limit(paginationInfo.getRecordCountPerPage())
                .fetch();

            Long totCnt = q.select(qTblOrgcht.count())
                    .from(qTblOrgcht)
                    .where(builder)
                    .fetchOne();
            if(totCnt == null) totCnt = 0L;

            resultVO.putResult("orgchtList", orgchtList);
            paginationInfo.setTotalRecordCount(totCnt.intValue());
            resultVO.putPaginationInfo(paginationInfo);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (NullPointerException e) {
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }
}
