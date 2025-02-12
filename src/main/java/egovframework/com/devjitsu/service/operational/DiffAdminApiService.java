package egovframework.com.devjitsu.service.operational;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.bbs.PstDto;
import egovframework.com.devjitsu.model.common.QTblComCd;
import egovframework.com.devjitsu.model.common.QTblComFile;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.common.TblComFile;
import egovframework.com.devjitsu.model.consult.*;
import egovframework.com.devjitsu.model.user.QTblCnslttMbr;
import egovframework.com.devjitsu.model.user.QTblUser;
import egovframework.com.devjitsu.model.user.TblUser;
import egovframework.com.devjitsu.repository.common.TblComFileRepository;
import egovframework.com.devjitsu.repository.consult.TblCnsltAplyRepository;
import egovframework.com.devjitsu.repository.consult.TblCnsltDtlRepository;
import egovframework.com.devjitsu.repository.consult.TblDfclMttrRepository;
import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DiffAdminApiService {

    private final EntityManager em;

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    @Resource(name = "EgovFileMngUtil")
    private EgovFileMngUtil fileUtil;

    private final TblComFileRepository tblComFileRepository;
    private final TblDfclMttrRepository tblDfclMttrRepository;

    /**
     * jpa 부등호
     * gt : >
     * lt : <
     * goe : >=
     * loe : <=
     */
    /**
     * query DSL 조건 추가하는 방법
     * BooleanBuilder builder = new BooleanBuilder();
     * builder.and(qTblComCdGroup.actvtnYn.eq("Y"));
     */

    public ResultVO getDfclMttrList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();

        try {
            if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
                paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
            }

            paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));

            QTblUser qTblUser = QTblUser.tblUser;
            QTblDfclMttr qTblDfclMttr = QTblDfclMttr.tblDfclMttr;
            QTblComCd qTblComCd = QTblComCd.tblComCd;

            JPAQueryFactory q = new JPAQueryFactory(em);

            /** query DSL 조건 추가하는 방법 */
            BooleanBuilder builder = new BooleanBuilder();
            if (!StringUtils.isEmpty(dto.get("startDt"))) {
                builder.and(
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", qTblDfclMttr.frstCrtDt).goe(
                                Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", dto.get("startDt"))
                        )
                );
            }
            if (!StringUtils.isEmpty(dto.get("endDt"))) {
                builder.and(
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", qTblDfclMttr.frstCrtDt).loe(
                                Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", dto.get("endDt"))
                        )
                );
            }

            if (!StringUtils.isEmpty(dto.get("answerYn"))) {
                if(dto.get("answerYn").equals("Y")){
                    builder.and(qTblDfclMttr.ansCn.isNotNull());
                }else if(dto.get("answerYn").equals("N")){
                    builder.and(qTblDfclMttr.ansCn.isNull());
                }
            }

            if (!StringUtils.isEmpty(dto.get("dfclMttrFld"))) {
                builder.and(qTblDfclMttr.dfclMttrFld.eq(Long.parseLong(dto.get("dfclMttrFld").toString())));
            }

            if (!StringUtils.isEmpty(dto.get("searchType"))) {
                if(dto.get("searchType").equals("ttl")){
                    builder.and(qTblDfclMttr.ttl.contains((String) dto.get("searchVal")));
                }else if(dto.get("searchType").equals("kornFlnm")){
                    builder.and(qTblUser.kornFlnm.contains((String) dto.get("searchVal")));
                }else if(dto.get("searchType").equals("dfclMttrCn")){
                    builder.and(qTblDfclMttr.dfclMttrCn.contains((String) dto.get("searchVal")));
                }
            }else{
                builder.and(
                    qTblDfclMttr.ttl.contains((String) dto.get("searchVal"))
                    .or(qTblUser.kornFlnm.contains((String) dto.get("searchVal")))
                    .or(qTblDfclMttr.dfclMttrCn.contains((String) dto.get("searchVal")))
                );
            }

            List<DfclMttrDto> diffList = q
                    .select(
                        Projections.constructor(
                        DfclMttrDto.class,
                        qTblDfclMttr.dfclMttrSn,
                        qTblDfclMttr.userSn,
                        qTblDfclMttr.dfclMttrFld,
                        qTblComCd.comCdNm,
                        qTblDfclMttr.ttl,
                        qTblUser.kornFlnm,
                        qTblDfclMttr.frstCrtDt,
                        new CaseBuilder()
                                .when(qTblDfclMttr.ansCn.isNotNull().and(qTblDfclMttr.ansCn.ne("")))
                                .then("Y")
                                .otherwise("N")
                        )
                    ).from(qTblDfclMttr)
                    .join(qTblUser).on(qTblDfclMttr.userSn.eq(qTblUser.userSn))
                    .join(qTblComCd).on(qTblDfclMttr.dfclMttrFld.eq(qTblComCd.comCdSn))
                    .where(builder)
                    .orderBy(qTblUser.frstCrtDt.desc())
                    .offset(paginationInfo.getFirstRecordIndex())
                    .limit(paginationInfo.getRecordCountPerPage())
                    .fetch();

            Long totCnt = q.select(qTblDfclMttr.count())
                    .from(qTblDfclMttr)
                    .join(qTblUser).on(qTblDfclMttr.userSn.eq(qTblUser.userSn))
                    .join(qTblComCd).on(qTblDfclMttr.dfclMttrFld.eq(qTblComCd.comCdSn))
                    .where(builder)
                    .fetchOne();
            if(totCnt == null) totCnt = 0L;
            paginationInfo.setTotalRecordCount(totCnt.intValue());

            resultVO.putResult("diffList", diffList);
            resultVO.putPaginationInfo(paginationInfo);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }
        return resultVO;
    }

    public ResultVO getDfclMttr(TblDfclMttr tblDfclMttr) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblUser qTblUser = QTblUser.tblUser;
            QTblComCd qTblComCd = QTblComCd.tblComCd;
            QTblComFile qTblComFile = QTblComFile.tblComFile;

            JPAQueryFactory q = new JPAQueryFactory(em);

            tblDfclMttr = tblDfclMttrRepository.findByDfclMttrSn(tblDfclMttr.getDfclMttrSn());
            tblDfclMttr.setKornFlnm(q.select(qTblUser.kornFlnm).from(qTblUser).where(qTblUser.userSn.eq(tblDfclMttr.getUserSn())).fetchOne());
            tblDfclMttr.setDfclMttrFldNm(q.select(qTblComCd.comCdNm).from(qTblComCd).where(qTblComCd.comCdSn.eq(tblDfclMttr.getDfclMttrFld())).fetchOne());
            tblDfclMttr.setDiffFiles(q.selectFrom(qTblComFile).where(qTblComFile.psnTblSn.eq("dfclMttr_" + tblDfclMttr.getDfclMttrSn())).fetch());
            tblDfclMttr.setAnswerFiles(q.selectFrom(qTblComFile).where(qTblComFile.psnTblSn.eq("dfclMttrAnswer_" + tblDfclMttr.getDfclMttrSn())).fetch());

            resultVO.putResult("dfclMttr", tblDfclMttr);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setDfclMttrAnswer(TblDfclMttr tblDfclMttr, List<MultipartFile> files) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblComFile qTblComFile = QTblComFile.tblComFile;
            JPAQueryFactory q = new JPAQueryFactory(em);

            if(tblDfclMttr.getAnsRegDt() == null) {
                tblDfclMttr.setAnsRegDt(LocalDateTime.now());
            }
            tblDfclMttrRepository.save(tblDfclMttr);

            if(files != null){
                long fileCnt = q.selectFrom(qTblComFile).where(qTblComFile.psnTblSn.eq("dfclMttrAnswer_" + tblDfclMttr.getDfclMttrSn())).fetchCount();
                tblComFileRepository.saveAll(
                    fileUtil.devFileInf(
                        files,
                        "/dfclMttr/" + tblDfclMttr.getDfclMttrSn() + "/answer",
                        "dfclMttrAnswer_" + tblDfclMttr.getDfclMttrSn(),
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
}
