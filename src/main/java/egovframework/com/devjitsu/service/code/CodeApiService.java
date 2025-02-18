package egovframework.com.devjitsu.service.code;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.*;
import egovframework.com.devjitsu.model.common.QTblComCd;
import egovframework.com.devjitsu.model.common.QTblComCdGroup;
import egovframework.com.devjitsu.repository.code.TblComCdGroupRepository;
import egovframework.com.devjitsu.repository.code.TblComCdRepository;
import egovframework.com.devjitsu.repository.common.TblComFileRepository;
import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CodeApiService {

    private final EntityManager em;

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

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
    private final TblComCdRepository tblComCdRepository;
    private final TblComCdGroupRepository tblComCdGroupRepository;
    private final TblComFileRepository tblComFileRepository;

    public ResultVO getComCdGroupList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();

        QTblComCdGroup qTblComCdGroup = QTblComCdGroup.tblComCdGroup;
        QTblComCd qTblComCd = QTblComCd.tblComCd;
        JPAQueryFactory q = new JPAQueryFactory(em);

        /** query DSL 조건 추가하는 방법 */
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qTblComCdGroup.actvtnYn.eq("Y"));
        if (!StringUtils.isEmpty(dto.get("cdGroupSn"))) {
            builder.and(qTblComCdGroup.cdGroupSn.eq(Long.valueOf(Integer.parseInt(dto.get("cdGroupSn").toString()))));
        }
        List<TblComCdGroup> cdGroupList = q.selectFrom(qTblComCdGroup).where(builder).orderBy(qTblComCdGroup.frstCrtDt.desc()).fetch();
        if(cdGroupList.size() > 0){

            for( TblComCdGroup tccg : cdGroupList){
                BooleanBuilder builder2 = new BooleanBuilder();
                if(!StringUtils.isEmpty(tccg.getCdGroupSn())){
                    builder2.and(qTblComCd.cdGroupSn.eq(tccg.getCdGroupSn()));
                    List<TblComCd> comCdList = q.selectFrom(qTblComCd).join(qTblComCdGroup).on(qTblComCd.cdGroupSn.eq(qTblComCdGroup.cdGroupSn)).where(builder2).orderBy(qTblComCd.sortSeq.asc()).fetch();
                    tccg.setComCdList(comCdList);
                }
            }
        }
        resultVO.putResult("cdGroupList",  cdGroupList);
        resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        return resultVO;
    }

    public ResultVO getComCdGroupListOnPageing(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();
        try{

            if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
                paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
            }
            paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));

            QTblComCdGroup qTblComCdGroup = QTblComCdGroup.tblComCdGroup;
            JPAQueryFactory q = new JPAQueryFactory(em);

            /** query DSL 조건 추가하는 방법 */
            BooleanBuilder builder = new BooleanBuilder();

            if(!StringUtils.isEmpty(dto.get("actvtnYn"))){
                builder.and(qTblComCdGroup.actvtnYn.eq(dto.get("actvtnYn").toString()));
                //builder.and(qTblComCdGroup.actvtnYn.eq(Long.valueOf(dto.get("upperMenuSn").toString())));
            }
            if(!StringUtils.isEmpty(dto.get("searchType")) && !StringUtils.isEmpty(dto.get("searchVal"))){
                if(dto.get("searchType").toString().equals("cdGroup")){
                    builder.and(qTblComCdGroup.cdGroup.contains(dto.get("searchVal").toString()));
                }else if(dto.get("searchType").toString().equals("cdGroupNm")){
                    builder.and(qTblComCdGroup.cdGroupNm.contains(dto.get("searchVal").toString()));
                }else{
                    builder.and(
                            qTblComCdGroup.cdGroup.contains(dto.get("searchVal").toString())
                            .or(qTblComCdGroup.cdGroupNm.contains(dto.get("searchVal").toString()))
                    );
                }
            }

            List<TblComCdGroup> cdGroupList = q.selectFrom(qTblComCdGroup).where(builder).orderBy(qTblComCdGroup.frstCrtDt.desc()).offset(paginationInfo.getFirstRecordIndex()).limit(paginationInfo.getRecordCountPerPage()).fetch();

            Long totCnt = q.select(qTblComCdGroup.count())
                    .from(qTblComCdGroup)
                    .where(builder).orderBy(qTblComCdGroup.frstCrtDt.desc()).fetchOne();

            if(totCnt == null) totCnt = 0L;
            paginationInfo.setTotalRecordCount(totCnt.intValue());
            resultVO.putResult("authGroup", cdGroupList);
            resultVO.putPaginationInfo(paginationInfo);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }
        return resultVO;
    }

    public ResultVO getComCdListOnPageing(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();
        try{

            if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
                paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
            }
            paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));

            QTblComCd qTblComCd = QTblComCd.tblComCd;
            JPAQueryFactory q = new JPAQueryFactory(em);

            /** query DSL 조건 추가하는 방법 */
            BooleanBuilder builder = new BooleanBuilder();
            if (!StringUtils.isEmpty(dto.get("cdGroupSn"))) {
                builder.and(qTblComCd.cdGroupSn.eq(Long.valueOf(Integer.parseInt(dto.get("cdGroupSn").toString()))));
            }

            if(!StringUtils.isEmpty(dto.get("actvtnYn"))){
                builder.and(qTblComCd.actvtnYn.eq(dto.get("actvtnYn").toString()));
            }
            if(!StringUtils.isEmpty(dto.get("searchType")) && !StringUtils.isEmpty(dto.get("searchVal"))){
                if(dto.get("searchType").toString().equals("comCd")){
                    builder.and(qTblComCd.comCd.contains(dto.get("searchVal").toString()));
                }else if(dto.get("searchType").toString().equals("comCdNm")){
                    builder.and(qTblComCd.comCdNm.contains(dto.get("searchVal").toString()));
                }else{
                    builder.and(
                            qTblComCd.comCd.contains(dto.get("searchVal").toString())
                            .or(qTblComCd.comCdNm.contains(dto.get("searchVal").toString()))
                    );
                }
            }


            List<TblComCd> cdList = q.selectFrom(qTblComCd).where(builder).orderBy(qTblComCd.frstCrtDt.desc()).offset(paginationInfo.getFirstRecordIndex()).limit(paginationInfo.getRecordCountPerPage()).fetch();

            Long totCnt = q.select(qTblComCd.count())
                    .from(qTblComCd)
                    .where(builder).orderBy(qTblComCd.frstCrtDt.desc()).fetchOne();

            if(totCnt == null) totCnt = 0L;
            paginationInfo.setTotalRecordCount(totCnt.intValue());
            resultVO.putResult("cdList", cdList);
            resultVO.putPaginationInfo(paginationInfo);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }
        return resultVO;
    }

    public ResultVO setComCdGroup(TblComCdGroup tblComCdGroup){
        ResultVO resultVO = new ResultVO();
        try {
            JPAQueryFactory q = new JPAQueryFactory(em);
            tblComCdGroupRepository.save(tblComCdGroup);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
        }
        return resultVO;
    }

    public ResultVO setComCd(TblComCd tblComCd){
        ResultVO resultVO = new ResultVO();
        try {
            JPAQueryFactory q = new JPAQueryFactory(em);
            tblComCdRepository.save(tblComCd);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
        }
        return resultVO;
    }

    public ResultVO getComCdGroup(TblComCdGroup tblComCdGroup){
        ResultVO resultVO = new ResultVO();
        try {

            QTblComCdGroup qTblComCdGroup = QTblComCdGroup.tblComCdGroup;
            QTblComCd qTblComCd = QTblComCd.tblComCd;
            JPAQueryFactory q = new JPAQueryFactory(em);
            BooleanBuilder builder = new BooleanBuilder();

            TblComCdGroup comCdGroup = tblComCdGroupRepository.findByCdGroupSn(tblComCdGroup.getCdGroupSn());
            if(!StringUtils.isEmpty(comCdGroup.getCdGroupSn())){
                builder.and(qTblComCd.cdGroupSn.eq(comCdGroup.getCdGroupSn()));
                List<TblComCd> comCdList = q.selectFrom(qTblComCd).join(qTblComCdGroup).on(qTblComCd.cdGroupSn.eq(qTblComCdGroup.cdGroupSn)).where(builder).fetch();
                comCdGroup.setComCdList(comCdList);
            }

            resultVO.putResult("comCdGroup", comCdGroup);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
        }
        return resultVO;
    }

    public ResultVO getComCd(TblComCd tblComCd){
        ResultVO resultVO = new ResultVO();
        try {
            QTblComCd qTblComCd = QTblComCd.tblComCd;
            JPAQueryFactory q = new JPAQueryFactory(em);
            BooleanBuilder builder = new BooleanBuilder();
            resultVO.putResult("comCd", tblComCdRepository.findByComCdSn(tblComCd.getComCdSn()));
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
        }
        return resultVO;
    }

    public ResultVO getComCdList(SearchDto dto){
        ResultVO resultVO = new ResultVO();
        try {
            QTblComCd qTblComCd = QTblComCd.tblComCd;
            JPAQueryFactory q = new JPAQueryFactory(em);
            BooleanBuilder builder = new BooleanBuilder();
            if (!StringUtils.isEmpty(dto.get("cdGroupSn"))) {
                builder.and(qTblComCd.cdGroupSn.eq(Long.valueOf(Integer.parseInt(dto.get("cdGroupSn").toString()))));
            }

            List<TblComCd> tblComCdList = q.selectFrom(qTblComCd).where(builder).orderBy(qTblComCd.sortSeq.asc()).fetch();

            resultVO.putResult("comCdList", tblComCdList);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
        }
        return resultVO;
    }

    public ResultVO setComCdGroupDel(TblComCdGroup tblComCdGroup) {
        ResultVO resultVO = new ResultVO();
        try {
            JPAQueryFactory q = new JPAQueryFactory(em);
            QTblComCd qTblComCd = QTblComCd.tblComCd;
            q.delete(qTblComCd).where(qTblComCd.cdGroupSn.eq(tblComCdGroup.getCdGroupSn())).execute();
            tblComCdGroupRepository.delete(tblComCdGroup);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }
        return resultVO;
    }

    public ResultVO setComCdDel(TblComCd tblComCd) {
        ResultVO resultVO = new ResultVO();
        try {
            JPAQueryFactory q = new JPAQueryFactory(em);
            tblComCdRepository.delete(tblComCd);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }
        return resultVO;
    }
}
