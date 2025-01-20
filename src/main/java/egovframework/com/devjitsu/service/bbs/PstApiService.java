package egovframework.com.devjitsu.service.bbs;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.bbs.*;
import egovframework.com.devjitsu.model.common.QTblComFile;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.common.TblComFile;
import egovframework.com.devjitsu.repository.bbs.TblBbsRepository;
import egovframework.com.devjitsu.repository.bbs.TblPstRepository;
import egovframework.com.devjitsu.repository.common.TblComFileRepository;
import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PstApiService {

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
            QTblComFile qTblComFile = QTblComFile.tblComFile;

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

            JPQLQuery<Long> fileCnt = JPAExpressions.select(qTblComFile.count())
                    .from(qTblComFile)
                    .where(
                        qTblComFile.psnTblPk.eq(
                            Expressions.stringTemplate(
                                "CONCAT('pst_', {0})", qTblPst.pstSn
                            )
                        )
                    );

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
                            qTblPst.pstTtl,
                            qTblPst.pstInqCnt,
                            qTblPst.rlsYn,
                            qTblPst.actvtnYn,
                            qTblPst.creatrSn,
                            qTblPst.frstCrtDt,
                            fileCnt
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
                        qTblPst.cmntLevel.asc(),
                        qTblPst.frstCrtDt.desc()
                    )
                    .offset(paginationInfo.getFirstRecordIndex())
                    .limit(paginationInfo.getRecordCountPerPage())
                    .fetch();

            Long totCnt = q.select(qTblPst.count())
                    .from(qTblPst)
                    .where(builder)
                    .fetchOne();
            if(totCnt == null) totCnt = 0L;
            paginationInfo.setTotalRecordCount(totCnt.intValue());

            resultVO.putResult("pstList", pstList);
            resultVO.putPaginationInfo(paginationInfo);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getPst(TblPst tblPst) {
        ResultVO resultVO = new ResultVO();
        try {
            tblPst = tblPstRepository.findByPstSn(tblPst.getPstSn());
            tblPst.setPstFiles(tblComFileRepository.findByPsnTblPk("pst_" + tblPst.getPstSn()));
            resultVO.putResult("pst", tblPst);
            resultVO.putResult("bbs", getBbs(tblPst.getBbsSn()));
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setPst(TblPst tblPst, List<MultipartFile> files) {
        ResultVO resultVO = new ResultVO();

        try {

            QTblPst qTblPst = QTblPst.tblPst;
            QTblComFile qTblComFile = QTblComFile.tblComFile;

            JPAQueryFactory q = new JPAQueryFactory(em);

            if(!StringUtils.isEmpty(tblPst.getOrgnlPstSn())) {
                NumberPath<Integer> maxCmntLevel = qTblPst.cmntLevel;
                JPAQuery<Integer> query = q
                        .select(Expressions.numberTemplate(Integer.class, "COALESCE(MAX({0}), 0) + 1", maxCmntLevel))
                        .from(qTblPst)
                        .where(qTblPst.bbsSn.eq(tblPst.getBbsSn())
                                .and(qTblPst.pstGroup.eq(tblPst.getPstGroup())));
                Integer nextReplyLevel = query.fetchOne();
                tblPst.setCmntLevel(nextReplyLevel);
            }

            if(StringUtils.isEmpty(tblPst.getPstSn())){
                /** 등록 */
                NumberPath<Long> maxArticleGroup = qTblPst.pstGroup;
                JPAQuery<Integer> group  = q
                        .select(Expressions.numberTemplate(Integer.class, "COALESCE(MAX({0}), 0) + 1", maxArticleGroup))
                        .from(qTblPst)
                        .where(qTblPst.bbsSn.eq(tblPst.getBbsSn()));
                tblPst.setPstGroup(group.fetchOne());
            }

            tblPstRepository.save(tblPst);
            if(files != null){
                long fileCnt = q.selectFrom(qTblComFile).where(qTblComFile.psnTblPk.eq("pst_" + tblPst.getPstSn())).fetchCount();
                tblComFileRepository.saveAll(
                    fileUtil.devFileInf(
                        files,
                        "/bbs/" + tblPst.getBbsSn() + "/pst/" + tblPst.getPstSn(),
                        "pst_" + tblPst.getPstSn(),
                        fileCnt
                    )
                );
            }

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }

        return resultVO;

    }

    public ResultVO setPstDel(TblPst tblPst) {
        ResultVO resultVO = new ResultVO();

        try {
            deletePstRecursively(tblPst);
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
