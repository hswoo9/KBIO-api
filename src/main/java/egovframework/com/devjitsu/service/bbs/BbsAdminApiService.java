package egovframework.com.devjitsu.service.bbs;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.bbs.*;
import egovframework.com.devjitsu.model.common.*;
import egovframework.com.devjitsu.repository.bbs.TblBbsRepository;
import egovframework.com.devjitsu.repository.bbs.TblPstRepository;
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
public class BbsAdminApiService {

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    @Resource(name = "EgovFileMngUtil")
    private EgovFileMngUtil fileUtil;

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
    private final TblComFileRepository tblComFileRepository;

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
            resultVO.putResult("bbs", getBbs(tblBbs.getBbsSn()));
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
            QTblPst qTblPst = QTblPst.tblPst;
            JPAQueryFactory q = new JPAQueryFactory(em);
            List<TblPst> tblPsts = q.selectFrom(qTblPst).where(qTblPst.bbsSn.eq(tblBbs.getBbsSn())).fetch();
            for(TblPst tblPst : tblPsts){
                deletePstRecursively(tblPst);
            }

            tblBbsRepository.delete(tblBbs);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }

        return resultVO;
    }

    public TblBbs getBbs(long bbsSn) {
        return tblBbsRepository.findByBbsSn(bbsSn);
    }

    private void deletePstRecursively(TblPst tblPst) throws Exception {
        QTblPst qTblPst = QTblPst.tblPst;
        QTblComFile qTblComFile = QTblComFile.tblComFile;

        JPAQueryFactory q = new JPAQueryFactory(em);
        /** 답글 삭제 */
        List<TblPst> replyPsts = q.selectFrom(qTblPst).where(qTblPst.orgnlPstSn.eq(tblPst.getPstSn())).fetch();
        for (TblPst replyPst : replyPsts) {
            deletePstRecursively(replyPst);
        }

        List<TblComFile> pstFiles = q.selectFrom(qTblComFile).where(qTblComFile.psnTblPk.eq("pst_" + tblPst.getPstSn())).fetch();
        for (TblComFile pstFile : pstFiles) {
            boolean isDelete = fileUtil.deleteFile(new String[]{pstFile.getStrgFileNm()}, pstFile.getAtchFilePathNm());
            if(isDelete){
                tblComFileRepository.delete(pstFile);
            }else{
                throw new Exception();
            }
        }

        tblPstRepository.delete(tblPst);
    }
}
