package egovframework.com.devjitsu.service.bbs;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.bbs.QTblBbs;
import egovframework.com.devjitsu.model.bbs.QTblPst;
import egovframework.com.devjitsu.model.bbs.TblBbs;
import egovframework.com.devjitsu.model.bbs.TblPst;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.repository.bbs.TblBbsRepository;
import egovframework.com.devjitsu.repository.bbs.TblPstRepository;
import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class BbsAdminApiService {

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

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
    private final TblBbsRepository tblBbsRepository;
    private final TblPstRepository tblPstRepository;

    public ResultVO getBbsList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();

        try {
            if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
                paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
            }
            paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));

            QTblBbs qTblBbs = QTblBbs.tblBbs;
            JPAQueryFactory q = new JPAQueryFactory(em);

            /** query DSL 조건 추가하는 방법 */
            BooleanBuilder builder = new BooleanBuilder();
            if (!StringUtils.isEmpty(dto.get("bbsNm"))) {
                builder.and(qTblBbs.bbsNm.contains((String) dto.get("bbsNm")));
            }
            if (!StringUtils.isEmpty(dto.get("bbsType"))) {
                builder.and(qTblBbs.bbsType.eq((String) dto.get("bbsType")));
            }

            List<TblBbs> bbsList = q.selectFrom(qTblBbs)
                    .where(builder)
                    .orderBy(qTblBbs.frstCrtDt.desc())
                    .offset(paginationInfo.getFirstRecordIndex())
                    .limit(paginationInfo.getRecordCountPerPage())
                    .fetch();

            Long totCnt = q.select(qTblBbs.count())
                    .from(qTblBbs)
                    .where(builder)
                    .fetchOne();
            if(totCnt == null) totCnt = 0L;
            paginationInfo.setTotalRecordCount(totCnt.intValue());

            resultVO.putResult("bbsList", bbsList);
            resultVO.putPaginationInfo(paginationInfo);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setBbs(TblBbs tblBbs) {
        ResultVO resultVO = new ResultVO();

        try {
            tblBbsRepository.save(tblBbs);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getBbs(TblBbs tblBbs) {
        ResultVO resultVO = new ResultVO();

        try {
            resultVO.putResult("bbs", tblBbsRepository.findByBbsSn(tblBbs.getBbsSn()));
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setBbsDel(TblBbs tblBbs) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblPst tblPst = QTblPst.tblPst;
            JPAQueryFactory q = new JPAQueryFactory(em);
            q.delete(tblPst).where(tblPst.bbsSn.eq(tblBbs.getBbsSn())).execute();
            tblBbsRepository.delete(tblBbs);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getPstList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();

        try {
            if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
                paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
            }
            paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));

            QTblPst qTblPst = QTblPst.tblPst;
            JPAQueryFactory q = new JPAQueryFactory(em);

            /** query DSL 조건 추가하는 방법 */
            BooleanBuilder builder = new BooleanBuilder();
            builder.and(qTblPst.bbsSn.eq(Long.parseLong(dto.get("bbsSn").toString())));
            if (!StringUtils.isEmpty(dto.get("searchType"))) {
                if(dto.get("searchType").equals("pstTtl")){
                    builder.and(qTblPst.pstTtl.contains((String) dto.get("searchVal")));
                }else if(dto.get("searchType").equals("pstCn")){
                    builder.and(qTblPst.pstCn.contains((String) dto.get("searchVal")));
                }else{
                    builder.and(
                        qTblPst.pstTtl.contains((String) dto.get("searchVal"))
                        .or(qTblPst.pstCn.contains((String) dto.get("searchVal")))
                    );
                }
            }

            List<TblPst> bbsList = q.selectFrom(qTblPst)
                    .where(builder)
                    .orderBy(qTblPst.frstCrtDt.desc())
                    .offset(paginationInfo.getFirstRecordIndex())
                    .limit(paginationInfo.getRecordCountPerPage())
                    .fetch();

            Long totCnt = q.select(qTblPst.count())
                    .from(qTblPst)
                    .where(builder)
                    .fetchOne();
            if(totCnt == null) totCnt = 0L;
            paginationInfo.setTotalRecordCount(totCnt.intValue());

            resultVO.putResult("pstList", bbsList);
            resultVO.putPaginationInfo(paginationInfo);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }
}
