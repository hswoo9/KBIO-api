package egovframework.com.devjitsu.service.user;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.ResultVO;

import egovframework.com.devjitsu.model.common.QTblComCd;
import egovframework.com.devjitsu.model.common.QTblComFile;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.common.TblComFile;
import egovframework.com.devjitsu.model.consult.ConsultingDTO;
import egovframework.com.devjitsu.model.user.*;

import egovframework.com.devjitsu.repository.common.TblComFileRepository;
import egovframework.com.devjitsu.repository.user.TblMvnEntMbrRepository;
import egovframework.com.devjitsu.repository.user.TblMvnEntRepository;

import egovframework.com.devjitsu.repository.user.TblUserRepository;
import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MvnEntApiService {

    private final EntityManager em;
    private final TblMvnEntRepository tblMvnEntRepository;
    private final TblMvnEntMbrRepository tblMvnEntMbrRepository;
    private final TblUserRepository tblUserRepository;
    private final TblComFileRepository tblComFileRepository;

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    @Resource(name = "EgovFileMngUtil")
    private EgovFileMngUtil fileUtil;

    public ResultVO setMvnEnt(TblMvnEnt tblMvnEnt, List<MultipartFile> files, List<MultipartFile> mvnEntAtchFiles){
        ResultVO resultVO = new ResultVO();

        try{
            QTblMvnEnt qTblMvnEnt = QTblMvnEnt.tblMvnEnt;
            QTblComFile qTblComFile = QTblComFile.tblComFile;

            JPAQueryFactory q = new JPAQueryFactory(em);

            tblMvnEntRepository.save(tblMvnEnt);



            if(files != null){
                long fileCnt = q.selectFrom(qTblComFile).where(qTblComFile.psnTblSn.eq("mvnEnt_" + tblMvnEnt.getMvnEntSn())).fetchCount();
                tblComFileRepository.saveAll(
                        fileUtil.devFileInf(
                                files,
                                "/mvnEnt/" + tblMvnEnt.getMvnEntSn(),
                                "mvnEnt_" + tblMvnEnt.getMvnEntSn(),
                                fileCnt
                        )
                );
            }

            if(mvnEntAtchFiles != null){
                long fileCnt = q.selectFrom(qTblComFile).where(qTblComFile.psnTblSn.eq("mvnEntAtch_" + tblMvnEnt.getMvnEntSn())).fetchCount();
                tblComFileRepository.saveAll(
                        fileUtil.devFileInf(
                                mvnEntAtchFiles,
                                "/mvnEntAtch/" + tblMvnEnt.getMvnEntSn(),
                                "mvnEntAtch_" + tblMvnEnt.getMvnEntSn(),
                                fileCnt
                        )
                );
            }

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
            QTblComCd qTblComCd = QTblComCd.tblComCd;

            QTblComCd qTpbiz = new QTblComCd("qTpbiz");
            JPAQueryFactory q = new JPAQueryFactory(em);

            BooleanBuilder builder = new BooleanBuilder();

            if(!StringUtils.isEmpty(dto.get("entClsf"))){
                builder.and(qTblMvnEnt.entClsf.contains((String) dto.get("entClsf")));
            }
            if(!StringUtils.isEmpty(dto.get("actvtnYn"))){
                builder.and(qTblMvnEnt.actvtnYn.contains((String) dto.get("actvtnYn")));
            }

            if(!StringUtils.isEmpty(dto.get("searchType"))) {
                if (dto.get("searchType").equals("mvnEntNm")) { //기업명
                    builder.and(qTblMvnEnt.mvnEntNm.contains((String) dto.get("searchVal")));
                } else if (dto.get("searchType").equals("rpsvNm")) { //대표자명
                    builder.and(qTblMvnEnt.rpsvNm.contains((String) dto.get("searchVal")));
                }
            }else{
                builder.and(
                        qTblMvnEnt.mvnEntNm.contains((String)dto.get("searchVal"))
                                .or(qTblMvnEnt.rpsvNm.contains((String)dto.get("searchVal")))
                );
            }

            /*List<TblMvnEnt> tblMvnEntList = q.selectFrom(qTblMvnEnt)
                    .where(builder)
                    .orderBy(qTblMvnEnt.frstCrtDt.desc())
                    .offset(paginationInfo.getFirstRecordIndex())
                    .limit(paginationInfo.getRecordCountPerPage()).fetch();*/

            List<MvnEntDto> tblMvnEntList = q
                    .select(
                            Projections.constructor(
                                    MvnEntDto.class,
                                    qTblMvnEnt,
                                    qTblComCd.comCdNm,
                                    //업종
                                    qTpbiz.comCdNm
                            )
                    ).from(qTblMvnEnt)
                    .leftJoin(qTblComCd)
                    .on(qTblComCd.comCd.eq(qTblMvnEnt.entClsf)
                            .and(qTblComCd.cdGroupSn.eq(17L)))
                    //업종
                    .leftJoin(qTpbiz)
                    .on(qTpbiz.comCd.eq(qTblMvnEnt.entTpbiz)
                            .and(qTpbiz.cdGroupSn.eq(18L)))
            .where(builder)
            .groupBy(qTblMvnEnt.mvnEntSn)
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

        QTblComFile qTblComFile = QTblComFile.tblComFile;
        JPAQueryFactory q = new JPAQueryFactory(em);

        try{
            TblComFile residentCompanyLogo = q.selectFrom(qTblComFile)
                            .where(
                                    qTblComFile.psnTblSn.eq(
                                            Expressions.stringTemplate("CONCAT('mvnEnt_',{0})", tblMvnEnt.getMvnEntSn())
                                    )
                            ).fetchOne();

            List<TblComFile> mvnEntAtchFile = q.selectFrom(qTblComFile)
                    .where(qTblComFile.psnTblSn.eq( Expressions.stringTemplate("CONCAT('mvnEntAtch_',{0})", tblMvnEnt.getMvnEntSn())))
                    .fetch();

            resultVO.putResult("rc",tblMvnEntRepository.findByMvnEntSn(tblMvnEnt.getMvnEntSn()));
            resultVO.putResult("logoFile",residentCompanyLogo);
            resultVO.putResult("mvnEntAtchFile",mvnEntAtchFile);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getResidentMemberList(SearchDto dto){
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();
        Map<String, Object> conditions = new HashMap<>();

        try {
        QTblMvnEntMbr qTblMvnEntMbr = QTblMvnEntMbr.tblMvnEntMbr;
        QTblUser qTblUser = QTblUser.tblUser;
        QTblComFile qTblComFile = QTblComFile.tblComFile;
        BooleanBuilder builder = new BooleanBuilder();
        JPAQueryFactory q = new JPAQueryFactory(em);

        if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
            paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
        }
            paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));

        long mvnEntSn = ((Number)dto.get("mvnEntSn")).longValue();

        //로고파일
        TblComFile residentCompanyLogo = q.selectFrom(qTblComFile)
                .where(
                        qTblComFile.psnTblSn.eq(
                                Expressions.stringTemplate("CONCAT('mvnEnt_',{0})", mvnEntSn)
                        )
               ).fetchOne();

        builder.and(qTblMvnEntMbr.mvnEntSn.eq(((Number) dto.get("mvnEntSn")).longValue()));

        if (!StringUtils.isEmpty(dto.get("sysMngrYn"))){
            //관리자설정에서 실행됨
            builder.and(qTblMvnEntMbr.sysMngrYn.eq((String) dto.get("sysMngrYn")));
        }

        //회원상태
        if (!StringUtils.isEmpty(dto.get("mbrStts"))) {
            builder.and(qTblUser.mbrStts.eq((String) dto.get("mbrStts")));
        }
        //검색어
        if (!StringUtils.isEmpty(dto.get("searchType"))) {
            if(dto.get("searchType").equals("kornFlnm")){ //성명
                builder.and(qTblUser.kornFlnm.contains((String) dto.get("searchVal")));
            }else if(dto.get("searchType").equals("userId")){ //아이디
                builder.and(qTblUser.userId.contains((String) dto.get("searchVal")));
            }
        }else {
            builder.and(
                    qTblUser.kornFlnm.contains((String) dto.get("searchVal"))
                            .or(qTblUser.userId.contains((String) dto.get("searchVal")))
            );
        }


            List<MvnEntMbrDto> userList = q.
                    select(
                            Projections.constructor(
                                    MvnEntMbrDto.class,
                                    qTblUser,
                                    qTblMvnEntMbr
                            )
                    ).from(qTblMvnEntMbr)
                    .join(qTblUser)
                    .on(
                            qTblUser.userSn.eq(qTblMvnEntMbr.userSn)
                    )
                    .where(builder)
                    .orderBy(qTblUser.frstCrtDt.desc())
                    .offset(paginationInfo.getFirstRecordIndex())
                    .limit(paginationInfo.getRecordCountPerPage())
                    .fetch();

            Long totCnt = q
                    .select(qTblMvnEntMbr.count())
                            .from(qTblMvnEntMbr)
                                    .join(qTblUser)
                                            .on(qTblUser.userSn.eq(qTblMvnEntMbr.userSn))
                                                    .where(builder)
                                                            .fetchOne();





        if(totCnt == null) totCnt = 0L;
        paginationInfo.setTotalRecordCount(totCnt.intValue());

        resultVO.putResult("logoFile",residentCompanyLogo);
        resultVO.putResult("getResidentMemberList",userList);
        resultVO.putPaginationInfo(paginationInfo);

        resultVO.setResultCode(ResponseCode.SUCCESS.getCode());

        }catch (Exception e){
        e.printStackTrace();
        resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }


        return resultVO;
    }

    public ResultVO getResidentMemberOne(SearchDto dto){
        ResultVO resultVO = new ResultVO();
        long userSn = ((Number)dto.get("userSn")).longValue();
        long mvnEntSn = ((Number)dto.get("mvnEntSn")).longValue();

        try {
            resultVO.putResult("member", tblUserRepository.findByUserSn(userSn));
            resultVO.putResult("rc",tblMvnEntRepository.findByMvnEntSn(mvnEntSn));
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());

        }

        return resultVO;
    }

    public ResultVO setMemberMbrStts(TblUser tblUser){
        ResultVO resultVO = new ResultVO();
        long userSn = tblUser.getUserSn();
        String mbrStts = tblUser.getMbrStts();

        try {
            Optional<TblUser> optionalTblUser = Optional.ofNullable(tblUserRepository.findByUserSn(userSn));
            if (optionalTblUser.isPresent()) {
                TblUser user = optionalTblUser.get();
                user.setActvtnYn(mbrStts); // 상태 변경
                resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
            }else{
                resultVO.setResultCode(ResponseCode.NOT_USER.getCode());
            }
        }catch (Exception e){
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }







}
