package egovframework.com.devjitsu.service.consult;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.QTblComCd;
import egovframework.com.devjitsu.model.common.QTblComFile;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.common.TblComFile;
import egovframework.com.devjitsu.model.consult.TblCnsltAply;
import egovframework.com.devjitsu.model.consult.TblCnsltDtl;
import egovframework.com.devjitsu.model.user.*;
import egovframework.com.devjitsu.model.consult.*;
import egovframework.com.devjitsu.repository.common.TblComFileRepository;
import egovframework.com.devjitsu.repository.consult.*;
import egovframework.com.devjitsu.repository.user.TblMvnEntRepository;
import egovframework.com.devjitsu.repository.user.TblUserRepository;
import egovframework.let.utl.sim.service.EgovFileScrty;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ConsultingAdminApiService {

    private final EntityManager em;

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    @Resource(name = "EgovFileMngUtil")
    private EgovFileMngUtil fileUtil;

    private final TblCnsltAplyRepository tblCnsltAplyRepository;
    private final TblComFileRepository tblComFileRepository;
    private final TblCnsltDtlRepository tblCnsltDtlRepository;
    private final TblUserRepository tblUserRepository;
    private final TblCnslttMbrRepository tblCnslttMbrRepository;
    private final TblDfclMttrRepository tblDfclMttrRepository;
    private final TblCnsltDsctnRepository tblCnsltDsctnRepository;
    private final TblMvnEntRepository tblMvnEntRepository;

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
    public ResultVO getConsultantList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();
        Map<String, Object> conditions = new HashMap<>();

        try {
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
            QTblUser qTblUser = QTblUser.tblUser;
            QTblCnslttMbr qTblCnslttMbr = QTblCnslttMbr.tblCnslttMbr;
            QTblComFile qTblComFile = QTblComFile.tblComFile;
            QTblCnsltDtl qTblCnsltDtl = QTblCnsltDtl.tblCnsltDtl;
            QTblCnsltAply qTblCnsltAply = QTblCnsltAply.tblCnsltAply;
            QTblComCd qTblComCd = QTblComCd.tblComCd;


            JPAQueryFactory q = new JPAQueryFactory(em);

            /** query DSL 조건 추가하는 방법 */
            BooleanBuilder builder = new BooleanBuilder();

            builder.and(qTblUser.mbrType.eq(2L));

            if(!StringUtils.isEmpty(dto.get("cnsltFld"))){
                builder.and(qTblCnslttMbr.cnsltFld.eq(Long.valueOf((String) dto.get("cnsltFld"))));
            }

            if(!StringUtils.isEmpty(dto.get("cnsltYn"))){
                builder.and(qTblCnslttMbr.cnsltActv.eq((String) dto.get("cnsltYn")));
            }

            //사용자 페이지의 경우 비공개인 컨설턴트는 내보내지 않음
            if(!StringUtils.isEmpty(dto.get("usedByGeneral"))){
                builder.and(qTblCnslttMbr.cnsltActv.eq((String) dto.get("usedByGeneral")));
            }

            if (!StringUtils.isEmpty(dto.get("searchType"))) {
                if (dto.get("searchType").equals("kornFlnm")) { //이름
                    builder.and(qTblUser.kornFlnm.contains((String) dto.get("searchVal")));
                } else if (dto.get("searchType").equals("ogdpNm")) { //소속
                    builder.and(qTblCnslttMbr.ogdpNm.contains((String) dto.get("searchVal")));
                } else if (dto.get("searchType").equals("jbpsNm")) { //직위
                    builder.and(qTblCnslttMbr.jbpsNm.contains((String) dto.get("searchVal")));
                }
                else if (dto.get("searchType").equals("userId")) { //아이디
                    builder.and(qTblUser.userId.contains((String) dto.get("searchVal")));
                }
            }else{
                builder.and(
                        qTblUser.kornFlnm.contains((String) dto.get("searchVal"))
                                .or(qTblCnslttMbr.ogdpNm.contains((String) dto.get("searchVal")))
                                .or(qTblCnslttMbr.jbpsNm.contains((String) dto.get("searchVal")))
                                .or(qTblUser.userId.contains((String) dto.get("searchVal")))
                );
            }

            List<ConsultDto> consultantList = q
                    .select(
                            Projections.constructor(
                                    ConsultDto.class,
                                    qTblCnslttMbr,
                                    qTblUser,
                                    qTblCnsltDtl,
                                    qTblComCd.comCdNm,
                                    Expressions.numberTemplate(Long.class,
                                            "SUM(CASE WHEN {0} = 26 THEN 1 ELSE 0 END)", qTblCnsltAply.cnsltSe),
                                    Expressions.numberTemplate(Long.class,
                                            "SUM(CASE WHEN {0} = 27 THEN 1 ELSE 0 END)", qTblCnsltAply.cnsltSe),
                                    qTblComFile
                            )
                    ).from(qTblUser)
                    .join(qTblCnslttMbr)
                    .on(qTblUser.userSn.eq(qTblCnslttMbr.userSn))
                    .leftJoin(qTblComFile)  //회원가입 시 사진 발리데이션 체크하고 join으로 바꾸기
                    .on(
                            qTblComFile.psnTblSn.eq(
                                    Expressions.stringTemplate("CONCAT('cnsltProfile_',{0})", qTblCnslttMbr.userSn) // 사진 조인
                            )
                    )
                    .leftJoin(qTblCnsltDtl)
                    .on(qTblCnsltDtl.cnslttUserSn.eq(qTblCnslttMbr.userSn))
                    .leftJoin(qTblComCd)
                    .on(qTblComCd.comCd.eq(Expressions.stringTemplate("{0}", qTblCnslttMbr.cnsltFld))
                            .and(qTblComCd.cdGroupSn.eq(10L)))
                    .leftJoin(qTblCnsltAply)
                    .on(qTblCnsltAply.cnsltAplySn.eq(qTblCnsltDtl.cnsltAplySn))
                    .where(builder)
                    .groupBy(qTblCnslttMbr.userSn)
                    .orderBy(qTblUser.userSn.desc())
                    .offset(paginationInfo.getFirstRecordIndex())
                    .limit(paginationInfo.getRecordCountPerPage())
                    .fetch();



            /*Long totCnt = q.select(qTblUser.count())
                    .join(qTblCnslttMbr).on(qTblUser.userSn.eq(qTblCnslttMbr.userSn)) //컨설턴트
//                    .join().on() 컨설턴트 사진
                    .from(qTblUser)
                    .where(builder)
                    .fetchOne();*/

            Long totCnt = q.select(qTblUser.count())
                    .from(qTblUser)
                    .join(qTblCnslttMbr).on(qTblUser.userSn.eq(qTblCnslttMbr.userSn))
                    .where(builder)
                    .fetchOne();



            if(totCnt == null) totCnt = 0L;
            paginationInfo.setTotalRecordCount(totCnt.intValue());

            resultVO.putResult("consultantList", consultantList);
            resultVO.putPaginationInfo(paginationInfo);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (NullPointerException e) {
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }
        return resultVO;
    }

    public ResultVO getConsultingList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();

        try {
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

            QTblUser qTblUser = QTblUser.tblUser; //회원테이블
            QTblCnslttMbr qTblCnslttMbr = QTblCnslttMbr.tblCnslttMbr; //컨설턴트회원
            QTblCnsltAply qTblCnsltAply = QTblCnsltAply.tblCnsltAply; //컨설팅신청
            QTblCnsltDtl qTblCnsltDtl = QTblCnsltDtl.tblCnsltDtl; //컨설팅상세
            QTblCnsltDsctn qTblCnsltDsctn = QTblCnsltDsctn.tblCnsltDsctn; //컨설팅내역
            QTblCnsltDgstfn qTblCnsltDgstfn = QTblCnsltDgstfn.tblCnsltDgstfn; //만족도
            QTblComFile qTblComFile = QTblComFile.tblComFile;
            QTblComCd qTblComCd = QTblComCd.tblComCd; //공통코드

            QTblUser qCnslttUser = new QTblUser("qCnslttUser");
            QTblComCd qCnsltSttsCd = new QTblComCd("qCnsltSttsCd");

            JPAQueryFactory q = new JPAQueryFactory(em);

            /** query DSL 조건 추가하는 방법 */
            BooleanBuilder builder = new BooleanBuilder();
            builder.and(qTblCnsltAply.cnsltSe.eq(Long.valueOf(dto.get("cnsltSe").toString())));
            if (!StringUtils.isEmpty(dto.get("startDt"))) {
                builder.and(
                    Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", qTblCnsltAply.frstCrtDt).goe(
                            Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", dto.get("startDt"))
                    )
                );
            }
            if (!StringUtils.isEmpty(dto.get("endDt"))) {
                builder.and(
                    Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", qTblCnsltAply.frstCrtDt).loe(
                            Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", dto.get("endDt"))
                    )
                );
            }

            if (!StringUtils.isEmpty(dto.get("cnsltFld"))) {
                builder.and(qTblCnsltAply.cnsltFld.eq(Long.valueOf((String) dto.get("cnsltFld"))));
            }

            if (!StringUtils.isEmpty(dto.get("cnsltSttsCd"))) {
                //builder.and(qTblCnsltDtl.cnsltSttsCd.eq((String) dto.get("cnsltSttsCd")));
                builder.and(
                        qTblCnsltDtl.cnsltSttsCd.in(
                                JPAExpressions
                                        .select(qTblComCd.comCd)
                                        .from(qTblComCd)
                                        .where(qTblComCd.comCdSn.eq(Long.valueOf(dto.get("cnsltSttsCd").toString())))
                        )
                );

            }

            if (!StringUtils.isEmpty(dto.get("searchType"))) {
                if(dto.get("searchType").equals("kornFlnm")){ //신청자
                    builder.and(qTblUser.kornFlnm.contains((String) dto.get("searchVal")));
                }else if(dto.get("searchType").equals("ogdpNm")){ //소속
                    builder.and(qTblCnslttMbr.ogdpNm.contains((String) dto.get("searchVal")));
                }else if(dto.get("searchType").equals("ttl")){ //제목
                    builder.and(qTblCnsltAply.ttl.contains((String) dto.get("searchVal")));
                }else if(dto.get("searchType").equals("cnslttKornFlnm")){
                    builder.and(qCnslttUser.kornFlnm.contains((String) dto.get("searchVal")));
                }else if(dto.get("searchType").equals("cnslttSn")){
                    builder.and(qTblCnsltDtl.cnslttUserSn.eq(Long.valueOf(dto.get("userSn").toString())));
                }
            }else{
                builder.and(
                    qTblUser.kornFlnm.contains((String) dto.get("searchVal"))
                    .or(qTblCnslttMbr.ogdpNm.contains((String) dto.get("searchVal")))
                    .or(qTblCnslttMbr.jbpsNm.contains((String) dto.get("searchVal")))
                );
            }

            JPQLQuery<Long> fileCnt = JPAExpressions
                    .select(qTblComFile.count())
                    .from(qTblComFile)
                    .where(
                            qTblComFile.psnTblSn.eq(
                                    Expressions.stringTemplate(
                                            "CONCAT('consulting_', {0})", qTblCnsltAply.cnsltAplySn
                                    )
                            )
                    );

          List<ConsultingDTO> consultantList = q.
                  select(
                         Projections.constructor(
                         ConsultingDTO.class,
                         qTblCnsltAply.cnsltAplySn,
                         qTblCnsltAply.userSn,
                         //컨설턴트유저sn
                         qTblCnsltDtl.cnslttUserSn,

                         qTblUser.kornFlnm,

                         //컨설턴트유저
                         qCnslttUser.kornFlnm,

                         qTblCnsltAply.frstCrtDt,
                         qTblCnsltAply.cnsltFld,
                         qTblComCd.comCdNm,
                         qTblCnslttMbr.ogdpNm,
                          //qTblComCd.comCdSn,
                          JPAExpressions
                                  .select(qTblComCd.comCdSn)
                                  .from(qTblComCd)
                                  .where(qTblComCd.comCd.eq(qTblCnsltDtl.cnsltSttsCd)
                                          .and(qTblComCd.cdGroupSn.eq(14L))),
                         qTblCnsltDtl.cnsltSttsCd,
                         qCnsltSttsCd.comCdNm,
//                        JPAExpressions
//                               .select(
//                                        qTblCnsltDgstfn.dgstfnArtcl.count().coalesce(0L)
//                               )
//                               .from(qTblCnsltDgstfn)
//                               .where(qTblCnsltDtl.cnsltAplySn.eq(qTblCnsltDgstfn.cnsltAplySn)),
                         qTblCnsltDgstfn.dgstfnArtcl.countDistinct().coalesce(0L),
                         qTblCnsltAply.ttl,
                         fileCnt
                          )
                  ).from(qTblCnsltAply)

                    //조인
                  .join(qTblCnsltDtl)
                  .on(
                          qTblCnsltAply.cnsltAplySn.eq(qTblCnsltDtl.cnsltAplySn)
                  )
                  .join(qTblUser)
                  .on(
                          qTblCnsltAply.userSn.eq(qTblUser.userSn)
                  )

                  //컨설턴트 유저
                  .leftJoin(qCnslttUser).on(qTblCnsltDtl.cnslttUserSn.eq(qCnslttUser.userSn))

                  .leftJoin(qTblCnslttMbr)
                  .on(
                          qTblCnsltDtl.cnslttUserSn.eq(qTblCnslttMbr.userSn)
                  )

                  .join(qTblComCd)
                  .on(qTblComCd.comCdSn.eq(qTblCnsltAply.cnsltFld))
                  //만족도 조인

                  .join(qCnsltSttsCd)
                  .on(qCnsltSttsCd.comCd.eq(qTblCnsltDtl.cnsltSttsCd)
                          .and(qCnsltSttsCd.cdGroupSn.eq(14L)))

                  .leftJoin(qTblCnsltDgstfn)
                  .on(
                          qTblCnsltAply.cnsltAplySn.eq(qTblCnsltDgstfn.cnsltAplySn)
                  )

                  .where(builder)
                  .orderBy(qTblCnsltAply.frstCrtDt.desc())
                  .groupBy(qTblCnsltAply.cnsltAplySn)
                  .offset(paginationInfo.getFirstRecordIndex())
                  .limit(paginationInfo.getRecordCountPerPage())
                  .fetch();

            /**
             private long cnsltAplySn;
             private long userSn;
             private long cnslttUserSn;
             private String kornFlnm;
             private String cnslttKornFlnm;
             private LocalDateTime frstCrtDt;
             private String cnsltArtcl;
             private String ogdpNm;
             private String cnsltSttsCd;
             private String dgstfnArtcl;
             **/



/*            Long totCnt = q.select(qTblUser.count())
//                    .join().on() 컨설턴트
//                    .join().on() 컨설턴트 사진
                    .from(qTblUser)
                    .where(builder)
                    .fetchOne();*/
            Long totCnt = q
                    .select(qTblCnsltAply.count())
                    .from(qTblCnsltAply)
                    .join(qTblCnsltDtl).on(qTblCnsltAply.cnsltAplySn.eq(qTblCnsltDtl.cnsltAplySn))
                    .join(qTblUser).on(qTblCnsltAply.userSn.eq(qTblUser.userSn))
                    .leftJoin(qCnslttUser).on(qTblCnsltDtl.cnslttUserSn.eq(qCnslttUser.userSn))
                    .leftJoin(qTblCnslttMbr).on(qTblCnsltDtl.cnslttUserSn.eq(qTblCnslttMbr.userSn))
                    .where(builder)
                    .fetchOne();


            if(totCnt == null) totCnt = 0L;
            paginationInfo.setTotalRecordCount(totCnt.intValue());

            resultVO.putResult("consultantList", consultantList);
            resultVO.putPaginationInfo(paginationInfo);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (NullPointerException e) {
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }
        return resultVO;
    }

    /**
     SearchDto
         userSn : 신청자sn
         cnsltAplySn : 컨설팅신청sn
         cnslttUserSn : 컨설턴트회원sn
     **/
    public ResultVO getConsultingDetail(SearchDto dto) {
        ResultVO resultVO = new ResultVO();

        try{
            QTblMvnEntMbr qTblMvnEntMbr = QTblMvnEntMbr.tblMvnEntMbr;
            QTblUser qTblUser = QTblUser.tblUser;
            QTblCnsltAply qTblCnsltAply = QTblCnsltAply.tblCnsltAply;
            QTblCnsltDsctn qTblCnsltDsctn = QTblCnsltDsctn.tblCnsltDsctn;
            QTblCnsltDgstfn qTblCnsltDgstfn = QTblCnsltDgstfn.tblCnsltDgstfn;
            QTblComFile qTblComFile = QTblComFile.tblComFile;
            JPAQueryFactory q = new JPAQueryFactory(em);

            //컨설턴트 정보
            //이 부분 if처리하기
            Object cnslttUserSnObj = dto.get("cnslttUserSn");
            String cnslttUserSnStr = cnslttUserSnObj != null ? cnslttUserSnObj.toString() : "";
            if (!StringUtils.isEmpty(cnslttUserSnStr) && !"0".equals(cnslttUserSnStr)) {
                TblUser consulttUser = tblUserRepository.findByUserSn(Long.parseLong(dto.get("cnslttUserSn").toString()));
                consulttUser.setDecodeMblTelno(EgovFileScrty.decryptAria(consulttUser.getMblTelno()));
                TblCnslttMbr consulttDtl = tblCnslttMbrRepository.findByUserSn(Long.parseLong(dto.get("cnslttUserSn").toString()));

                TblComFile cnsltProfileFile = q.selectFrom(qTblComFile).where(
                        qTblComFile.psnTblSn.eq(
                                Expressions.stringTemplate("CONCAT('cnsltProfile_',{0})", consulttUser.getUserSn()) //사진
                        )
                ).fetchOne();
                List<TblComFile> cnsltCertificateFile = q.selectFrom(qTblComFile).where(
                        qTblComFile.psnTblSn.eq(
                                Expressions.stringTemplate("CONCAT('cnsltCertificate_',{0})", consulttUser.getUserSn()) //자격증
                        )
                ).fetch();

                resultVO.putResult("consulttUser", consulttUser);
                resultVO.putResult("consulttDtl", consulttDtl);
                resultVO.putResult("cnsltProfileFile", cnsltProfileFile);
                resultVO.putResult("cnsltCertificateFile", cnsltCertificateFile);
            }

            //신청자 정보
            TblUser tblUser = tblUserRepository.findByUserSn(Long.parseLong(dto.get("userSn").toString()));
            Long mvnEntSn = q
                    .select(qTblMvnEntMbr.mvnEntSn)
                    .from(qTblMvnEntMbr)
                    .join(qTblUser).on(qTblMvnEntMbr.userSn.eq(qTblUser.userSn))
                    .where(qTblUser.userSn.eq(Long.parseLong(dto.get("userSn").toString())))
                    .fetchOne();

                if (mvnEntSn != null) {
                    TblMvnEnt tblMvnEnt = tblMvnEntRepository.findByMvnEntSn(mvnEntSn);
                    resultVO.putResult("userCompDetail",tblMvnEnt);
                }

            resultVO.putResult("userDetail",tblUser);


            //상담신청 정보
            TblCnsltAply tblCnsltAply = q
                    .selectFrom(qTblCnsltAply)
                            .where(qTblCnsltAply.cnsltAplySn.eq(Long.parseLong(dto.get("cnsltAplySn").toString())))
                                    .fetchOne();

            resultVO.putResult("cnslt", tblCnsltAply);
            //상담내역 정보
            List<TblCnsltDsctn> tblCnsltDsctnList = q
                    .selectFrom(qTblCnsltDsctn)
                            .where(qTblCnsltDsctn.cnsltAplySn.eq(Long.parseLong(dto.get("cnsltAplySn").toString())))
                                    .orderBy(qTblCnsltDsctn.frstCrtDt.asc()).fetch();

            List<TblComFile> allFiles = q
                    .selectFrom(qTblComFile)
                    .where(qTblComFile.psnTblSn.in(
                            tblCnsltDsctnList.stream()
                                    .map(tblCnsltDsctn -> "consulting_" + tblCnsltDsctn.getCnsltDsctnSn()) // cnsltDsctnSn을 기반으로 psnTblSn을 생성
                                    .collect(Collectors.toList())
                    ))
                    .orderBy(qTblComFile.atchFileSn.desc())
                    .fetch();

            // cnsltDsctnSn 별로 파일을 매칭
            Map<Long, List<TblComFile>> filesByDsctnSn = allFiles.stream()
                    .collect(Collectors.groupingBy(file -> {
                        String psnTblSn = file.getPsnTblSn();
                        return Long.parseLong(psnTblSn.replace("consulting_", ""));
                    }));


            resultVO.putResult("cnsltDsctnList", tblCnsltDsctnList);
            resultVO.putResult("filesByDsctnSn",filesByDsctnSn);



            //만족도 정보
            List<TblCnsltDgstfn> tblCnsltDgstfn = q
                    .selectFrom(qTblCnsltDgstfn)
                    .where(qTblCnsltDgstfn.cnsltAplySn.eq(Long.parseLong(dto.get("cnsltAplySn").toString())))
                    .orderBy(qTblCnsltDgstfn.frstCrtDt.desc()).fetch();

            if(!tblCnsltDgstfn.isEmpty()) {
                resultVO.putResult("cnsltDgstfnList", tblCnsltDgstfn);
            }
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());

        } catch (NullPointerException e) {
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }catch (InvalidCipherTextException e) {
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setCnslttMbrActv (TblCnslttMbr tblCnslttMbr, TblUser tblUser) throws Exception{
        ResultVO resultVO = new ResultVO();
        long userSn = tblCnslttMbr.getUserSn();

        try{
            /*Optional<TblCnslttMbr> tblCnslttMbrOptional = Optional.ofNullable(tblCnslttMbrRepository.findByUserSn(userSn));
            if(tblCnslttMbrOptional.isPresent()) {
                TblCnslttMbr cnslttMbr = tblCnslttMbrOptional.get();
                cnslttMbr.setCnsltActv(tblCnslttMbr.getCnsltActv());

                resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
            }else{
                resultVO.setResultCode(ResponseCode.NOT_USER.getCode());
            }*/
            //String encryptedMblTelno = EgovFileScrty.encode(tblUser.getMblTelno());
            String encryptedMblTelno = EgovFileScrty.encryptAria(tblUser.getMblTelno().toString().getBytes(StandardCharsets.UTF_8));
            tblUser.setMblTelno(encryptedMblTelno);
            tblUserRepository.save(tblUser);
            tblCnslttMbrRepository.save(tblCnslttMbr);

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (NullPointerException e) {
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setCnsltDtlSttsCd (TblCnsltDtl tblCnsltDtl){
        ResultVO resultVO = new ResultVO();
        long cnsltAplySn = tblCnsltDtl.getCnsltAplySn();

        try{
            Optional<TblCnsltDtl> cnsltDtl = Optional.ofNullable(tblCnsltDtlRepository.findByCnsltAplySn(cnsltAplySn));

            if (cnsltDtl.isPresent()) {
                TblCnsltDtl detail = cnsltDtl.get();
                if (detail.getCnslttUserSn() != null) {
                    detail.setCnsltSttsCd("13");
                    resultVO.putResult("resultType","matchComplete");
                    resultVO.putResult("resultUserSn",detail.getCnslttUserSn());
                    resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
                } else {
                    detail.setCnsltSttsCd("12");
                    resultVO.putResult("resultType","matchNotComplete");
                    resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
                }
            }else{
                resultVO.setResultCode(ResponseCode.NOT_USER.getCode());
            }


        }catch(NullPointerException e){
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }
        return resultVO;
    }

    public ResultVO cancleCnsltDtlSttsCd (TblCnsltDtl tblCnsltDtl){
        ResultVO resultVO = new ResultVO();
        long cnsltAplySn = tblCnsltDtl.getCnsltAplySn();

        try{
            Optional<TblCnsltDtl> cnsltDtl = Optional.ofNullable(tblCnsltDtlRepository.findByCnsltAplySn(cnsltAplySn));

            if (cnsltDtl.isPresent()) {
                TblCnsltDtl detail = cnsltDtl.get();
                detail.setCnsltSttsCd("999");
                resultVO.setResultCode(ResponseCode.SUCCESS.getCode());

            }else{
                resultVO.setResultCode(ResponseCode.NOT_USER.getCode());
            }


        }catch(NullPointerException e){
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }
        return resultVO;
    }

    public ResultVO updateCnsltt(TblCnsltDtl tblCnsltDtl){
        ResultVO resultVO = new ResultVO();
        long cnsltAplySn = tblCnsltDtl.getCnsltAplySn();

        try {
            Optional<TblCnsltDtl> cnsltDtl = Optional.ofNullable(tblCnsltDtlRepository.findByCnsltAplySn(cnsltAplySn));

            if (cnsltDtl.isPresent()) {
                TblCnsltDtl detail = cnsltDtl.get();
                detail.setCnsltSttsCd("13");
                detail.setCnslttUserSn(tblCnsltDtl.getCnslttUserSn());
                detail.setCnslttDsgnDt(LocalDateTime.now());

                resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
            }


        }catch(NullPointerException e){
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }



}
