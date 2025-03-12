package egovframework.com.devjitsu.service.consult;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.bbs.QTblBbs;
import egovframework.com.devjitsu.model.bbs.QTblPst;
import egovframework.com.devjitsu.model.bbs.TblBbs;
import egovframework.com.devjitsu.model.bbs.TblPst;
import egovframework.com.devjitsu.model.common.*;
import egovframework.com.devjitsu.model.consult.*;
import egovframework.com.devjitsu.model.menu.MenuDto;
import egovframework.com.devjitsu.model.menu.QTblAuthrtGroupMenu;
import egovframework.com.devjitsu.model.menu.QTblMenu;
import egovframework.com.devjitsu.model.menu.TblMenu;
import egovframework.com.devjitsu.model.user.*;
import egovframework.com.devjitsu.repository.code.TblComCdGroupRepository;
import egovframework.com.devjitsu.repository.code.TblComCdRepository;
import egovframework.com.devjitsu.repository.common.TblComFileRepository;
import egovframework.com.devjitsu.repository.consult.*;
import egovframework.com.devjitsu.repository.menu.TblMenuAuthrtGroupRepository;
import egovframework.com.devjitsu.repository.menu.TblMenuRepository;
import egovframework.com.devjitsu.repository.user.TblAcbgRepository;
import egovframework.com.devjitsu.repository.user.TblCrrRepository;
import egovframework.com.devjitsu.repository.user.TblQlfcLcnsRepository;
import egovframework.com.devjitsu.repository.user.TblUserRepository;
import egovframework.com.devjitsu.service.access.MngrAcsIpApiService;
import egovframework.com.devjitsu.service.common.RedisApiService;
import egovframework.com.devjitsu.service.menu.MenuAuthGroupApiService;
import egovframework.let.utl.sim.service.EgovFileScrty;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
@Transactional
public class ConsultingApiService {

    private final EntityManager em;

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    @Resource(name = "EgovFileMngUtil")
    private EgovFileMngUtil fileUtil;


    private final TblCnsltAplyRepository tblCnsltAplyRepository;
    private final TblDfclMttrRepository tblDfclMttrRepository;
    private final TblComFileRepository tblComFileRepository;
    private final TblUserRepository tblUserRepository;
    private final TblCnslttMbrRepository tblCnslttMbrRepository;
    private final TblCnsltDtlRepository tblCnsltDtlRepository;
    private final TblCnsltDsctnRepository tblCnsltDsctnRepository;
    private final TblQlfcLcnsRepository tblQlfcLcnsRepository;
    private final TblCrrRepository tblCrrRepository;
    private final TblAcbgRepository tblAcbgRepository;



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

            paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));

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
            builder.and(qTblCnslttMbr.cnsltActv.eq("Y"));

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
            }else{
                builder.and(
                        qTblUser.kornFlnm.contains((String) dto.get("searchVal"))
                                .or(qTblCnslttMbr.ogdpNm.contains((String) dto.get("searchVal")))
                                .or(qTblCnslttMbr.jbpsNm.contains((String) dto.get("searchVal")))
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

    public ResultVO getConsultantDetail(SearchDto dto){
        ResultVO resultVO = new ResultVO();

        QTblComFile qTblComFile = QTblComFile.tblComFile;
        JPAQueryFactory q = new JPAQueryFactory(em);

        try{
            TblUser tblUser = tblUserRepository.findByUserSn(Long.parseLong(dto.get("userSn").toString()));
            TblCnslttMbr tblCnslttMbr = tblCnslttMbrRepository.findByUserSn(Long.parseLong(dto.get("userSn").toString()));

            if(tblUser != null){
                tblUser.setDecodeMblTelno(EgovFileScrty.decryptAria(tblUser.getMblTelno()));
                List<TblQlfcLcns> tblQlfcLcnsList = tblQlfcLcnsRepository.findAllByUserSn(tblUser.getUserSn());
                List<TblAcbg> tblAcbgList = tblAcbgRepository.findAllByUserSn(tblUser.getUserSn());
                List<TblCrr> tblCrrList = tblCrrRepository.findAllByUserSn(tblUser.getUserSn());

                List<Long> qlfcLcnsSnList = tblQlfcLcnsList.stream()
                        .map(TblQlfcLcns::getQlfcLcnsSn)
                        .collect(Collectors.toList());

                TblComFile cnsltProfileFile = q.selectFrom(qTblComFile).where(
                        qTblComFile.psnTblSn.eq(
                                Expressions.stringTemplate("CONCAT('cnsltProfile_',{0})", tblCnslttMbr.getUserSn()) //사진
                        )
                ).fetchOne();

//

                List<TblComFile> cnsltCertificateFile = q.selectFrom(qTblComFile)
                        .where(qTblComFile.psnTblSn.in(
                                qlfcLcnsSnList.stream()
                                        .map(sn -> "cnsltCertificate_" + sn)
                                        .collect(Collectors.toList())
                        ))
                        .fetch();






                resultVO.putResult("memberDetail",tblUser);
                resultVO.putResult("consultant",tblCnslttMbr);
                resultVO.putResult("tblQlfcLcnsList",tblQlfcLcnsList);
                resultVO.putResult("tblAcbgList",tblAcbgList);
                resultVO.putResult("tblCrrList",tblCrrList);
                resultVO.putResult("cnsltProfileFile",cnsltProfileFile);
                resultVO.putResult("cnsltCertificateFile",cnsltCertificateFile);

                resultVO.setResultCode(ResponseCode.SUCCESS.getCode());

            }else{
                resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
            }


        } catch (NullPointerException e) {
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }catch (InvalidCipherTextException e) {
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setConsulting(TblCnsltAply tblCnsltAply, TblCnsltDtl tblCnsltDtl, List<MultipartFile> files) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblComFile qTblComFile = QTblComFile.tblComFile;
            JPAQueryFactory q = new JPAQueryFactory(em);
            TblCnsltAply saveTblCnsltAply =
            tblCnsltAplyRepository.save(tblCnsltAply);

            saveTblCnsltAply.getCnsltAplySn();

            tblCnsltDtl.setCnsltAplySn(tblCnsltAply.getCnsltAplySn());
            if(!StringUtils.isEmpty(tblCnsltDtl.getCnslttUserSn())){
                tblCnsltDtl.setCnslttDsgnDt(LocalDateTime.now());
            }
            tblCnsltDtlRepository.save(tblCnsltDtl);

            TblCnsltDsctn tblCnsltDsctn = new TblCnsltDsctn();
            tblCnsltDsctn.setCnsltAplySn((tblCnsltAply.getCnsltAplySn()));
            tblCnsltDsctn.setDsctnSe("0");
            tblCnsltDsctn.setCn(tblCnsltAply.getCn());
            tblCnsltDsctn.setCreatrSn(tblCnsltAply.getUserSn());
            tblCnsltDsctn.setActvtnYn("Y");
            tblCnsltDsctnRepository.save(tblCnsltDsctn);


            if(files != null){
                long fileCnt = q.selectFrom(qTblComFile).where(qTblComFile.psnTblSn.eq("consulting_" + tblCnsltAply.getCnsltAplySn())).fetchCount();
                tblComFileRepository.saveAll(
                    fileUtil.devFileInf(
                            files,
                            "/consulting/" + tblCnsltDsctn.getCnsltDsctnSn(),
                            "consulting_" + tblCnsltDsctn.getCnsltDsctnSn(),
                            fileCnt
                    )
                );
            }

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (IOException e) {
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setDfclMttr(TblDfclMttr tblDfclMttr, List<MultipartFile> files) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblComFile qTblComFile = QTblComFile.tblComFile;
            JPAQueryFactory q = new JPAQueryFactory(em);

            tblDfclMttrRepository.save(tblDfclMttr);
            if(files != null){
                long fileCnt = q.selectFrom(qTblComFile).where(qTblComFile.psnTblSn.eq("dfclMttr_" + tblDfclMttr.getDfclMttrSn())).fetchCount();
                tblComFileRepository.saveAll(
                    fileUtil.devFileInf(
                        files,
                        "/dfclMttr/" + tblDfclMttr.getDfclMttrSn(),
                        "dfclMttr_" + tblDfclMttr.getDfclMttrSn(),
                        fileCnt
                    )
                );
            }

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (IOException e) {
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }

        return resultVO;
    }
}
