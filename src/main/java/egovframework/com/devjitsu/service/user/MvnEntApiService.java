package egovframework.com.devjitsu.service.user;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import egovframework.com.devjitsu.model.menu.TblMenuAuthrtGroupUser;
import egovframework.com.devjitsu.model.user.*;

import egovframework.com.devjitsu.repository.common.TblComFileRepository;
import egovframework.com.devjitsu.repository.user.TblMvnEntMbrRepository;
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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
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

    public ResultVO setMvnEntList(SearchDto dto){
        ResultVO resultVO = new ResultVO();

        try{
            Gson gson = new Gson();
            List<TblMvnEnt> tblMvnEntList = gson.fromJson(dto.get("tblMvnEntList").toString(), new TypeToken<List<TblMvnEnt>>() {}.getType());
            if(tblMvnEntList.size() > 0){
                tblMvnEntRepository.saveAll(tblMvnEntList);
            }
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (NullPointerException e){
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
        }
        return resultVO;
    }

    public ResultVO setMvnEnt(TblMvnEnt tblMvnEnt, List<MultipartFile> files, List<MultipartFile> biFile, List<MultipartFile> mvnEntAtchFiles){
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

            if(biFile != null){
                long fileCnt = q.selectFrom(qTblComFile).where(qTblComFile.psnTblSn.eq("mvnEntBi_" + tblMvnEnt.getMvnEntSn())).fetchCount();
                tblComFileRepository.saveAll(
                        fileUtil.devFileInf(
                                biFile,
                                "/mvnEnt/" + tblMvnEnt.getMvnEntSn(),
                                "mvnEntBi_" + tblMvnEnt.getMvnEntSn(),
                                fileCnt
                        )
                );
            }

            if(mvnEntAtchFiles != null){
                long fileCnt = q.selectFrom(qTblComFile).where(qTblComFile.psnTblSn.eq("mvnEntAtch_" + tblMvnEnt.getMvnEntSn())).fetchCount();
                tblComFileRepository.saveAll(
                        fileUtil.devFileInf(
                                mvnEntAtchFiles,
                                "/mvnEnt/" + tblMvnEnt.getMvnEntSn(),
                                "mvnEntAtch_" + tblMvnEnt.getMvnEntSn(),
                                fileCnt
                        )
                );
            }

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (IOException e){
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
            QTblMvnEnt qTblMvnEnt = QTblMvnEnt.tblMvnEnt;
            QTblComCd qTblComCd = QTblComCd.tblComCd;
            QTblComCd qTpbiz = new QTblComCd("qTpbiz");
            JPAQueryFactory q = new JPAQueryFactory(em);

            BooleanBuilder builder = new BooleanBuilder();

            //활성여부
            builder.and(qTblMvnEnt.actvtnYn.eq("Y"));

            if(!StringUtils.isEmpty(dto.get("entClsf"))){
                builder.and(qTblMvnEnt.entClsf.eq((String) dto.get("entClsf")));
            }

            if(!StringUtils.isEmpty(dto.get("entTpbiz"))){
                builder.and(qTblMvnEnt.entTpbiz.eq((String) dto.get("entTpbiz")));
            }

            if(!StringUtils.isEmpty(dto.get("rlsYn"))){
                builder.and(qTblMvnEnt.rlsYn.eq((String) dto.get("rlsYn")));
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
        }catch(NullPointerException e){
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

            TblComFile biLogoFile = q.selectFrom(qTblComFile)
                    .where(
                            qTblComFile.psnTblSn.eq(
                                    Expressions.stringTemplate("CONCAT('mvnEntBi_',{0})", tblMvnEnt.getMvnEntSn())
                            )
                    ).fetchOne();

            List<TblComFile> mvnEntAtchFile = q.selectFrom(qTblComFile)
                    .where(qTblComFile.psnTblSn.eq( Expressions.stringTemplate("CONCAT('mvnEntAtch_',{0})", tblMvnEnt.getMvnEntSn())))
                    .fetch();

            resultVO.putResult("rc",tblMvnEntRepository.findByMvnEntSn(tblMvnEnt.getMvnEntSn()));
            resultVO.putResult("logoFile",residentCompanyLogo);
            resultVO.putResult("biLogoFile",biLogoFile);
            resultVO.putResult("mvnEntAtchFile",mvnEntAtchFile);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (NullPointerException e){
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

            for (MvnEntMbrDto mvnEntMbrDto : userList) {
                TblUser user = mvnEntMbrDto.getTblUser();
                if (user != null && user.getMblTelno() != null) {
                        user.setDecodeMblTelno(EgovFileScrty.decryptAria(user.getMblTelno()));

                }
            }



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

        }catch (NullPointerException e){
        resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }catch (InvalidCipherTextException e) {
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }


        return resultVO;
    }

    public ResultVO getResidentMemberOne(SearchDto dto){
        ResultVO resultVO = new ResultVO();
        long userSn = ((Number)dto.get("userSn")).longValue();
        long mvnEntSn = ((Number)dto.get("mvnEntSn")).longValue();

        try {
            resultVO.putResult("mvnEntMbr", tblMvnEntMbrRepository.findByUserSn(userSn));
            resultVO.putResult("member", tblUserRepository.findByUserSn(userSn));
            resultVO.putResult("rc",tblMvnEntRepository.findByMvnEntSn(mvnEntSn));
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (NullPointerException e) {
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());

        }

        return resultVO;
    }

    public ResultVO setAprvYn(TblMvnEntMbr tblMvnEntMbr){
        ResultVO resultVO = new ResultVO();
        long userSn = tblMvnEntMbr.getUserSn();
        String aprvYn = tblMvnEntMbr.getAprvYn();

        try {
            Optional<TblMvnEntMbr> optionalTblMvnEntMbr = Optional.ofNullable(tblMvnEntMbrRepository.findByUserSn(userSn));
            if (optionalTblMvnEntMbr.isPresent()) {
                TblMvnEntMbr member = optionalTblMvnEntMbr.get();
                member.setAprvYn(aprvYn); // 상태 변경
                resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
            }else{
                resultVO.setResultCode(ResponseCode.NOT_USER.getCode());
            }
        }catch (NullPointerException e){
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO updateMvnEntMbrToMng(List<TblMvnEntMbr> tblMvnEntMbrList){
        ResultVO resultVO = new ResultVO();

        try{

            for (TblMvnEntMbr member : tblMvnEntMbrList) {
                member.setMvnEntSn(member.getMvnEntSn());
                member.setSysMngrYn("Y");
            }
            tblMvnEntMbrRepository.saveAll(tblMvnEntMbrList);


            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (NullPointerException e){

            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO cancleMng(TblMvnEntMbr tblMvnEntMbr){
        ResultVO resultVO = new ResultVO();

        try {

            tblMvnEntMbr.setSysMngrYn("N");
            tblMvnEntMbr.setMvnEntSn(tblMvnEntMbr.getMvnEntSn());
            tblMvnEntMbrRepository.save(tblMvnEntMbr);

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (NullPointerException e){
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setRcActvtnYn(TblMvnEnt tblMvnEnt){
        ResultVO resultVO = new ResultVO();

        try {
            tblMvnEnt.setActvtnYn("N");
            tblMvnEntRepository.save(tblMvnEnt);

            long mvnEntSn = tblMvnEnt.getMvnEntSn();
            List <TblMvnEntMbr> tblMvnEntMbrList =tblMvnEntMbrRepository.findUserSnByMvnEntSn(mvnEntSn);

            List<Long> userSnList = new ArrayList<>();

            if (tblMvnEntMbrList != null && !tblMvnEntMbrList.isEmpty()) {
                userSnList = tblMvnEntMbrList.stream()
                        .map(TblMvnEntMbr::getUserSn)
                        .collect(Collectors.toList());

                userSnList.forEach(userSn -> {
                    TblUser user = tblUserRepository.findByUserSn(userSn);
                    if (user != null) {
                        user.setActvtnYn("N");
                        tblUserRepository.save(user);
                    }
                });

            }

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (NullPointerException e) {
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }







}
