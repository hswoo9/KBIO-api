package egovframework.com.devjitsu.service.bbs;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.bbs.*;
import egovframework.com.devjitsu.model.bbs.QTblPst;
import egovframework.com.devjitsu.model.bbs.QTblPstCmnt;
import egovframework.com.devjitsu.model.bbs.QTblPstEvl;
import egovframework.com.devjitsu.model.common.QTblComCd;
import egovframework.com.devjitsu.model.common.QTblComFile;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.common.TblComFile;
import egovframework.com.devjitsu.model.menu.AuthrtDto;
import egovframework.com.devjitsu.model.user.QTblUser;
import egovframework.com.devjitsu.repository.bbs.TblBbsRepository;
import egovframework.com.devjitsu.repository.bbs.TblPstCmntRepository;
import egovframework.com.devjitsu.repository.bbs.TblPstEvlRepository;
import egovframework.com.devjitsu.repository.bbs.TblPstRepository;
import egovframework.com.devjitsu.repository.common.TblComFileRepository;
import egovframework.com.devjitsu.service.access.MngrAcsIpApiService;
import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PstApiService {

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    @Resource(name = "EgovFileMngUtil")
    private EgovFileMngUtil fileUtil;

    private final EntityManager em;

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
     *  query DSL 조건 추가하는 방법
     *  BooleanBuilder builder = new BooleanBuilder();
     *  builder.and(qTblComCdGroup.actvtnYn.eq("Y"));
     * */
    private final TblBbsRepository tblBbsRepository;
    private final TblPstRepository tblPstRepository;
    private final TblPstCmntRepository tblPstCmntRepository;
    private final TblPstEvlRepository tblPstEvlRepository;
    private final TblComFileRepository tblComFileRepository;

    public ResultVO getPstList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();

        try {
            TblBbs tblBbs = tblBbsRepository.findByBbsSn(Long.parseLong(dto.get("bbsSn").toString()));

            if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
                paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
            }
            paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));

            QTblUser qTblUser = QTblUser.tblUser;
            QTblPst qTblPst = QTblPst.tblPst;
            QTblPst qTblPst2 = new QTblPst("qTblPst2");

            QTblComCd qTblComCd = QTblComCd.tblComCd;
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
                }
            }else{
                builder.and(
                        qTblPst.pstTtl.contains((String) dto.get("searchVal"))
                            .or(qTblPst.pstCn.contains((String) dto.get("searchVal")))
                );
            }

            if (!StringUtils.isEmpty(dto.get("actvtnYn"))) {
                builder.and(qTblPst.actvtnYn.eq((String) dto.get("actvtnYn")));
            }else{
                builder.and(qTblPst.actvtnYn.eq("Y"));
            }

            if(tblBbs.getBbsTypeNm().equals("2")){
                builder.and(qTblPst.upPstSn.isNull());
            }

            JPQLQuery<Long> fileCnt = JPAExpressions
                .select(qTblComFile.count())
                .from(qTblComFile)
                .where(
                    qTblComFile.psnTblSn.eq(
                        Expressions.stringTemplate(
                            "CONCAT('pst_', {0})", qTblPst.pstSn
                        )
                    )
                );

            JPQLQuery<String> answer = JPAExpressions
                .select(new CaseBuilder().when(qTblPst2.count().gt(0)).then("Y").otherwise("N"))
                .from(qTblPst2)
                .where(
                    qTblPst2.upPstSn.eq(qTblPst.pstSn)
                );

            Expression<String> pstClsfNm;
            if (tblBbs.getPstCtgryYn().equals("Y")) {
                pstClsfNm = JPAExpressions.select(qTblComCd.comCdNm)
                        .from(qTblComCd)
                        .where(qTblComCd.comCdSn.eq(qTblPst.pstClsf));
            } else {
                pstClsfNm = Expressions.constant("");
            }

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
                            pstClsfNm,
                            qTblPst.pstTtl,
                            qTblPst.pstInqCnt,
                            qTblPst.rlsYn,
                            qTblPst.actvtnYn,
                            qTblPst.upPstSn,
                            qTblPst.creatrSn,
                            qTblUser.kornFlnm,
                            qTblPst.prvtPswd,
                            qTblPst.frstCrtDt,
                            fileCnt,
                            answer,
                            Expressions.constant("")
                        )
                    )
                    .from(qTblPst)
                    .join(qTblUser)
                    .on(qTblPst.creatrSn.eq(qTblUser.userSn))
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

            resultVO.putResult("bbs", tblBbs);
            resultVO.putResult("pstList", pstList);

            if (!StringUtils.isEmpty(dto.get("userSn"))) {
                /** 사용자 권한 불러오기 */
                resultVO.putResult("authrt", bbsAdminApiService.getUserBbsAuthrt(tblBbs, Long.parseLong(dto.get("userSn").toString())));
            }else{
                resultVO.putResult("authrt", bbsAdminApiService.getUserBbsAuthrt(tblBbs, null));
            }

            resultVO.putPaginationInfo(paginationInfo);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getPst(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        try {
            QTblPst qTblPst = QTblPst.tblPst;
            QTblComCd qTblComCd = QTblComCd.tblComCd;
            JPAQueryFactory q = new JPAQueryFactory(em);

            TblPst tblPst = tblPstRepository.findByPstSn(Long.parseLong(dto.get("pstSn").toString()));
            tblPst.setPstFiles(tblComFileRepository.findAllByPsnTblSn("pst_" + tblPst.getPstSn()));
            tblPst.setPstCmnt(getPstCmnt(tblPst));

            TblBbs tblBbs = getBbs(tblPst.getBbsSn());

            if(tblBbs.getBbsTypeNm().equals("2")){
                tblPst.setAnswer(q.selectFrom(qTblPst).where(qTblPst.upPstSn.eq(tblPst.getPstSn())).fetchFirst());
            }

            if(!StringUtils.isEmpty(tblPst.getPstClsf())){
                tblPst.setPstClsfNm(q.select(qTblComCd.comCdNm)
                        .from(qTblComCd)
                        .where(qTblComCd.comCdSn.eq(tblPst.getPstClsf())).fetchOne());
            }

            q.update(qTblPst).set(qTblPst.pstInqCnt, qTblPst.pstInqCnt.add(1)).where(qTblPst.pstSn.eq(tblPst.getPstSn())).execute();

            resultVO.putResult("pst", tblPst);
            resultVO.putResult("pstPrevNext", getPstPrevNext(tblPst));
            resultVO.putResult("bbs", getBbs(tblPst.getBbsSn()));

            if (!StringUtils.isEmpty(dto.get("userSn"))) {
                /** 사용자 권한 불러오기 */
                resultVO.putResult("authrt", bbsAdminApiService.getUserBbsAuthrt(tblBbs, Long.parseLong(dto.get("userSn").toString())));
            }else{
                resultVO.putResult("authrt", bbsAdminApiService.getUserBbsAuthrt(tblBbs, null));
            }

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

            if(!StringUtils.isEmpty(tblPst.getUpPstSn())) {
                TblPst orgnlPst = q.selectFrom(qTblPst).where(qTblPst.pstSn.eq(tblPst.getUpPstSn())).fetchOne();
                if(orgnlPst.getRlsYn().equals("Y")) {
                    tblPst.setRlsYn(orgnlPst.getRlsYn());
                    tblPst.setPrvtPswd(orgnlPst.getPrvtPswd());
                }

                if(StringUtils.isEmpty(tblPst.getAnsStp())){
                    NumberPath<Integer> maxAnsStp = qTblPst.ansStp;
                    JPAQuery<Integer> query = q
                            .select(Expressions.numberTemplate(Integer.class, "COALESCE(MAX({0}), 0) + 1", maxAnsStp))
                            .from(qTblPst)
                            .where(qTblPst.bbsSn.eq(tblPst.getBbsSn())
                                    .and(qTblPst.pstGroup.eq(tblPst.getPstGroup())));
                    Integer nextAnsStp = query.fetchOne();
                    tblPst.setAnsStp(nextAnsStp);
                }
            }

            if(StringUtils.isEmpty(tblPst.getPstGroup())){
                /** 등록 */
                NumberPath<Long> maxArticleGroup = qTblPst.pstGroup;
                JPAQuery<Integer> group  = q
                        .select(Expressions.numberTemplate(Integer.class, "COALESCE(MAX({0}), 0) + 1", maxArticleGroup))
                        .from(qTblPst)
                        .where(qTblPst.bbsSn.eq(tblPst.getBbsSn()));
                tblPst.setPstGroup(Long.valueOf(group.fetchOne()));
            }

            tblPstRepository.save(tblPst);
            if(files != null){
                long fileCnt = q.selectFrom(qTblComFile).where(qTblComFile.psnTblSn.eq("pst_" + tblPst.getPstSn())).fetchCount();
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

    public ResultVO getPstCmntList(TblPst tblPst) {
        ResultVO resultVO = new ResultVO();

        try {
            resultVO.putResult("pstCmntList", getPstCmnt(tblPst));
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }

        return resultVO;
    }

    public List<TblPstCmnt> getPstCmnt(TblPst tblPst){
        QTblPstCmnt qTblPstCmnt = QTblPstCmnt.tblPstCmnt;
        JPAQueryFactory q = new JPAQueryFactory(em);

        List<TblPstCmnt> pstCmntList = q
                .selectFrom(qTblPstCmnt)
                .where(qTblPstCmnt.pstSn.eq(tblPst.getPstSn()).and(qTblPstCmnt.actvtnYn.eq("Y")))
                .orderBy(qTblPstCmnt.cmntGrp.asc(), qTblPstCmnt.cmntSeq.asc()).fetch();
        return pstCmntList;
    }

    public ResultVO setPstCmnt(TblPstCmnt tblPstCmnt) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblPstCmnt qTblPstCmnt = QTblPstCmnt.tblPstCmnt;
            JPAQueryFactory q = new JPAQueryFactory(em);

            if(!StringUtils.isEmpty(tblPstCmnt.getUpPstCmntSn())){
                /** 댓글 순서 */
                NumberPath<Long> maxCmntSeq = qTblPstCmnt.cmntSeq;
                JPAQuery<Long> upPstCmntSeq  = q
                        .select(Expressions.numberTemplate(Long.class, "COALESCE(MAX({0}), 0) + 1", maxCmntSeq))
                        .from(qTblPstCmnt)
                        .where(qTblPstCmnt.cmntGrp.eq(tblPstCmnt.getCmntGrp()).and(qTblPstCmnt.pstCmntSn.eq(tblPstCmnt.getUpPstCmntSn())));

                JPAQuery<Long> upSubPstCmntSeq  = q
                        .select(Expressions.numberTemplate(Long.class, "COALESCE(MAX({0}), 0) + 1", maxCmntSeq))
                        .from(qTblPstCmnt)
                        .where(qTblPstCmnt.cmntGrp.eq(tblPstCmnt.getCmntGrp()).and(qTblPstCmnt.upPstCmntSn.eq(tblPstCmnt.getUpPstCmntSn())));

                Long upPstCmntSeqVal = upPstCmntSeq.fetchOne();
                Long upSubPstCmntSeqVal = upSubPstCmntSeq.fetchOne();

                if(upPstCmntSeqVal > upSubPstCmntSeqVal){
                    tblPstCmnt.setCmntSeq(upPstCmntSeqVal);
                }else{
                    tblPstCmnt.setCmntSeq(upSubPstCmntSeqVal);
                }

                NumberPath<Long> maxCmntStp = qTblPstCmnt.cmntStp;
                JPAQuery<Long> seq = q
                        .select(Expressions.numberTemplate(Long.class, "COALESCE(MAX({0}), 0) + 1", maxCmntStp))
                        .from(qTblPstCmnt)
                        .where(qTblPstCmnt.cmntGrp.eq(tblPstCmnt.getCmntGrp())
                                .and(qTblPstCmnt.pstCmntSn.eq(tblPstCmnt.getUpPstCmntSn())));
                tblPstCmnt.setCmntStp(seq.fetchOne());


                q.update(qTblPstCmnt)
                        .set(qTblPstCmnt.cmntSeq, qTblPstCmnt.cmntSeq.add(1))
                        .where(
                                qTblPstCmnt.cmntSeq.goe(tblPstCmnt.getCmntSeq())
                                        .and(qTblPstCmnt.pstSn.eq(tblPstCmnt.getPstSn()))
                        ).execute();
            }

            if(StringUtils.isEmpty(tblPstCmnt.getCmntGrp())) {
                NumberPath<Long> cmntGroup = qTblPstCmnt.cmntGrp;
                JPAQuery<Long> maxCmntGroup  = q
                        .select(Expressions.numberTemplate(Long.class, "COALESCE(MAX({0}), 0) + 1", cmntGroup))
                        .from(qTblPstCmnt)
                        .where(qTblPstCmnt.pstSn.eq(tblPstCmnt.getPstSn()));
                tblPstCmnt.setCmntGrp(maxCmntGroup.fetchOne());
            }

            tblPstCmntRepository.save(tblPstCmnt);

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setPstCmntDel(TblPstCmnt tblPstCmnt) {
        ResultVO resultVO = new ResultVO();

        try {
            deletePstCmntRecursively(tblPstCmnt);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getPstEvlList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        try {
            QTblPstEvl qTblPstEvl = QTblPstEvl.tblPstEvl;
            JPAQueryFactory q = new JPAQueryFactory(em);
            BooleanBuilder builder = new BooleanBuilder();
            if(!StringUtils.isEmpty(dto.get("pstSn"))){
                builder.and(qTblPstEvl.pstSn.eq(Long.valueOf(Integer.parseInt(dto.get("pstSn").toString()))));
            }
            resultVO.putResult("pstEvlList", q.selectFrom(qTblPstEvl).where(builder).orderBy(qTblPstEvl.pstSn.desc()).fetch());
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getPstEvl(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        try {
            QTblPstEvl qTblPstEvl = QTblPstEvl.tblPstEvl;
            JPAQueryFactory q = new JPAQueryFactory(em);
            BooleanBuilder builder = new BooleanBuilder();
            if(!StringUtils.isEmpty(dto.get("userSn"))){
                builder.and(qTblPstEvl.evlUserSn.eq(Long.valueOf(Integer.parseInt(dto.get("userSn").toString()))));
            }
            if(!StringUtils.isEmpty(dto.get("pstSn"))){
                builder.and(qTblPstEvl.pstSn.eq(Long.valueOf(Integer.parseInt(dto.get("pstSn").toString()))));
            }
            resultVO.putResult("pstEvl", q.selectFrom(qTblPstEvl).where(builder).fetchFirst());
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setPstEvl(TblPstEvl tblPstEvl) {
        ResultVO resultVO = new ResultVO();

        try {
            tblPstEvlRepository.save(tblPstEvl);
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

    private List<PstDto> getPstPrevNext(TblPst tblPst){
        List<Object[]> results = tblPstRepository.getPrevNextPst(tblPst.getBbsSn(), tblPst.getPstSn());
        List<PstDto> pstDtos = new ArrayList<>();
        for (Object[] result : results) {
            PstDto pstDto = new PstDto(
                    Long.valueOf((Integer) result[0]),
                    (String) result[1],        // pstTtl
                    (String) result[2]        // position
            );
            pstDtos.add(pstDto);
        }

        return pstDtos;
    }

    private void deletePstRecursively(TblPst tblPst) throws Exception {
        QTblPst qTblPst = QTblPst.tblPst;
        QTblComFile qTblComFile = QTblComFile.tblComFile;

        JPAQueryFactory q = new JPAQueryFactory(em);
        /** 답글 삭제 */
        List<TblPst> replyPsts = q.selectFrom(qTblPst).where(qTblPst.upPstSn.eq(tblPst.getPstSn())).fetch();
        for (TblPst replyPst : replyPsts) {
            deletePstRecursively(replyPst);
        }

        List<TblComFile> pstFiles = q.selectFrom(qTblComFile).where(qTblComFile.psnTblSn.eq("pst_" + tblPst.getPstSn())).fetch();
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

    private void deletePstCmntRecursively(TblPstCmnt tblPstCmnt) {
        QTblPstCmnt qTblPstCmnt = QTblPstCmnt.tblPstCmnt;

        JPAQueryFactory q = new JPAQueryFactory(em);
        /** 답글 삭제 */
        List<TblPstCmnt> childrenCmnt = q.selectFrom(qTblPstCmnt).where(qTblPstCmnt.upPstCmntSn.eq(tblPstCmnt.getPstCmntSn())).fetch();
        for (TblPstCmnt cmnt : childrenCmnt) {
            deletePstCmntRecursively(cmnt);
        }

        tblPstCmntRepository.delete(tblPstCmnt);
    }
}
