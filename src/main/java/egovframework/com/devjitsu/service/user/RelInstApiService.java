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
import egovframework.com.devjitsu.model.user.*;

import egovframework.com.devjitsu.repository.common.TblComFileRepository;
import egovframework.com.devjitsu.repository.user.TblMvnEntMbrRepository;
import egovframework.com.devjitsu.repository.user.TblRelInstMbrRepository;
import egovframework.com.devjitsu.repository.user.TblRelInstRepository;

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
public class RelInstApiService {

    private final EntityManager em;
    private final TblUserRepository tblUserRepository;
    private final TblRelInstRepository tblRelInstRepository;
    private final TblRelInstMbrRepository tblRelInstMbrRepository;
    private final TblComFileRepository tblComFileRepository;

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    @Resource(name = "EgovFileMngUtil")
    private EgovFileMngUtil fileUtil;

    public ResultVO setRelInstList(SearchDto dto){
        ResultVO resultVO = new ResultVO();
        try{
            Gson gson = new Gson();
            List<TblRelInst> tblRelInstList = gson.fromJson(dto.get("tblRelInstList").toString(), new TypeToken<List<TblRelInst>>() {}.getType());
            if(tblRelInstList.size() > 0){
                tblRelInstRepository.saveAll(tblRelInstList);
            }
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
        }
        return resultVO;
    }

    public ResultVO setRelInst(TblRelInst tblRelInst, List<MultipartFile> files, List<MultipartFile> biFile, List<MultipartFile> relInstAtchFiles){
        ResultVO resultVO = new ResultVO();

        try{
            QTblComFile qTblComFile = QTblComFile.tblComFile;
            JPAQueryFactory q = new JPAQueryFactory(em);

            tblRelInstRepository.save(tblRelInst);

            if(files != null){
                long fileCnt = q.selectFrom(qTblComFile).where(qTblComFile.psnTblSn.eq("relInst_" + tblRelInst.getRelInstSn())).fetchCount();
                tblComFileRepository.saveAll(
                    fileUtil.devFileInf(
                        files,
                        "/relInst/" + tblRelInst.getRelInstSn(),
                        "relInst_" + tblRelInst.getRelInstSn(),
                        fileCnt
                    )
                );
            }

            if(biFile != null){
                long fileCnt = q.selectFrom(qTblComFile).where(qTblComFile.psnTblSn.eq("relInstBi_" + tblRelInst.getRelInstSn())).fetchCount();
                tblComFileRepository.saveAll(
                        fileUtil.devFileInf(
                                biFile,
                                "/relInst/" + tblRelInst.getRelInstSn(),
                                "relInstBi_" + tblRelInst.getRelInstSn(),
                                fileCnt
                        )
                );
            }

            if(relInstAtchFiles != null){
                long fileCnt = q.selectFrom(qTblComFile).where(qTblComFile.psnTblSn.eq("relInstAtch_" + tblRelInst.getRelInstSn())).fetchCount();
                tblComFileRepository.saveAll(
                    fileUtil.devFileInf(
                        relInstAtchFiles,
                        "/relInstAtch/" + tblRelInst.getRelInstSn(),
                        "relInstAtch_" + tblRelInst.getRelInstSn(),
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

    public ResultVO getRelInstList(SearchDto dto){
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();

        try{
            if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
                paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
            }
            paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));

            QTblRelInst qTblRelInst = QTblRelInst.tblRelInst;
            QTblComCd qTblComCd = QTblComCd.tblComCd;
            QTblComCd qTpbiz = new QTblComCd("qTpbiz");

            JPAQueryFactory q = new JPAQueryFactory(em);

            BooleanBuilder builder = new BooleanBuilder();
            
            //활성여부
            builder.and(qTblRelInst.actvtnYn.eq("Y"));

            if(!StringUtils.isEmpty(dto.get("clsf"))){
                builder.and(qTblRelInst.clsf.eq((String) dto.get("clsf")));
            }

            if(!StringUtils.isEmpty(dto.get("tpbiz"))){
                builder.and(qTblRelInst.tpbiz.eq((String) dto.get("tpbiz")));
            }

            if(!StringUtils.isEmpty(dto.get("rlsYn"))){
                builder.and(qTblRelInst.rlsYn.eq((String) dto.get("rlsYn")));
            }

            if(!StringUtils.isEmpty(dto.get("searchType"))) {
                if (dto.get("searchType").equals("relInstNm")) {
                    builder.and(qTblRelInst.relInstNm.contains((String) dto.get("searchVal")));
                } else if (dto.get("searchType").equals("rpsvNm")) {
                    builder.and(qTblRelInst.rpsvNm.contains((String) dto.get("searchVal")));
                }
            }else{
                builder.and(
                    qTblRelInst.relInstNm.contains((String)dto.get("searchVal"))
                        .or(qTblRelInst.rpsvNm.contains((String)dto.get("searchVal")))
                );
            }


            List<RelInstDto> tblRelInstList = q
                    .select(
                        Projections.constructor(
                            RelInstDto.class,
                            qTblRelInst,
                            qTblComCd.comCdNm,
                            qTpbiz.comCdNm
                        )
                    ).from(qTblRelInst)
                    .leftJoin(qTblComCd)
                    .on(qTblComCd.comCd.eq(qTblRelInst.clsf).and(qTblComCd.cdGroupSn.eq(17L)))
                    .leftJoin(qTpbiz)
                    .on(qTpbiz.comCd.eq(qTblRelInst.tpbiz).and(qTpbiz.cdGroupSn.eq(18L)))
                    .where(builder)
                    .orderBy(qTblRelInst.frstCrtDt.desc())
                    .offset(paginationInfo.getFirstRecordIndex())
                    .limit(paginationInfo.getRecordCountPerPage()).fetch();

            Long totCnt = q.select(qTblRelInst.count()).from(qTblRelInst).where(builder).fetchOne();

            if(totCnt == null) totCnt = 0L;
            paginationInfo.setTotalRecordCount(totCnt.intValue());

            resultVO.putResult("rcList",tblRelInstList);
            resultVO.putPaginationInfo(paginationInfo);

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch(Exception e){
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }


        return resultVO;
    }

    public ResultVO getRc(TblRelInst tblRelInst) {
        ResultVO resultVO = new ResultVO();

        try{
            tblRelInst = tblRelInstRepository.findByRelInstSn(tblRelInst.getRelInstSn());
            tblRelInst.setLogoFile(tblComFileRepository.findByPsnTblSn("relInst_" + tblRelInst.getRelInstSn()));
            tblRelInst.setBiLogoFile(tblComFileRepository.findByPsnTblSn("relInstBi_" + tblRelInst.getRelInstSn()));
            tblRelInst.setRelInstAtchFiles(tblComFileRepository.findAllByPsnTblSn("relInstAtch_" + tblRelInst.getRelInstSn()));

            resultVO.putResult("rc", tblRelInst);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getRelatedtMemberList(SearchDto dto){
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();
        Map<String, Object> conditions = new HashMap<>();

        try {
            List<TblRelInstMbr> relInstList;
            QTblRelInstMbr qTblRelInstMbr = QTblRelInstMbr.tblRelInstMbr;
            QTblUser qTblUser = QTblUser.tblUser;
            QTblComFile qTblComFile = QTblComFile.tblComFile;
            BooleanBuilder builder = new BooleanBuilder();
            JPAQueryFactory q = new JPAQueryFactory(em);

            if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
                paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
            }
            paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));


            long relInstSn = ((Number)dto.get("relInstSn")).longValue();

            //로고파일
            TblComFile relInstLogo = q.selectFrom(qTblComFile)
                    .where(
                            qTblComFile.psnTblSn.eq(
                                    Expressions.stringTemplate("CONCAT('relInst_',{0})",relInstSn)
                            )
                    ).fetchOne();

            builder.and(qTblRelInstMbr.relInstSn.eq(((Number)dto.get("relInstSn")).longValue()));

            if (!StringUtils.isEmpty(dto.get("sysMngrYn"))){
                //관리자설정에서 실행됨
                builder.and(qTblRelInstMbr.sysMngrYn.contains((String) dto.get("sysMngrYn")));

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

            List<TblRelInstMbrDto> userList = q.
                    select(
                            Projections.constructor(
                                    TblRelInstMbrDto.class,
                                    qTblUser,
                                    qTblRelInstMbr
                            )
                    ).from(qTblRelInstMbr)
                    .join(qTblUser)
                    .on(
                            qTblUser.userSn.eq(qTblRelInstMbr.userSn)
                    )
                    .where(builder)
                    .orderBy(qTblUser.frstCrtDt.desc())
                    .offset(paginationInfo.getFirstRecordIndex())
                    .limit(paginationInfo.getRecordCountPerPage())
                    .fetch();

            Long totCnt = q
                    .select(qTblRelInstMbr.count())
                    .from(qTblRelInstMbr)
                    .join(qTblUser)
                    .on(qTblUser.userSn.eq(qTblRelInstMbr.userSn))
                    .where(builder)
                    .fetchOne();





            if(totCnt == null) totCnt = 0L;
            paginationInfo.setTotalRecordCount(totCnt.intValue());

            resultVO.putResult("logoFile",relInstLogo);
            resultVO.putResult("getRelatedMemberList",userList);
            resultVO.putPaginationInfo(paginationInfo);

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());

        }catch (Exception e){
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }


        return resultVO;
    }



    public ResultVO setAprvYn(TblRelInstMbr tblRelInstMbr){
        ResultVO resultVO = new ResultVO();
        long userSn = tblRelInstMbr.getUserSn();
        String aprvYn = tblRelInstMbr.getAprvYn();

        try {
            Optional<TblRelInstMbr> optionalTblRelInstMbr = Optional.ofNullable(tblRelInstMbrRepository.findByUserSn(userSn));
            if (optionalTblRelInstMbr.isPresent()) {
                TblRelInstMbr member = optionalTblRelInstMbr.get();
                member.setAprvYn(aprvYn); // 상태 변경
                resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
            }else{
                resultVO.setResultCode(ResponseCode.NOT_USER.getCode());
            }
        }catch (Exception e){
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getRelatedMemberOne(SearchDto dto){
        ResultVO resultVO = new ResultVO();
        long userSn = ((Number)dto.get("userSn")).longValue();
        long relInstSn = ((Number)dto.get("relInstSn")).longValue();
        try {
            resultVO.putResult("relInstMbr", tblRelInstMbrRepository.findByUserSn(userSn));
            resultVO.putResult("member", tblUserRepository.findByUserSn(userSn));
            resultVO.putResult("rc",tblRelInstRepository.findByRelInstSn(relInstSn));
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());

        }

        return resultVO;
    }

    public ResultVO updateRelInstMbrToMng(List<TblRelInstMbr> tblRelInstMbrList){
        ResultVO resultVO = new ResultVO();

        try{

            for(TblRelInstMbr member : tblRelInstMbrList){
                member.setRelInstSn(member.getRelInstSn());
                member.setSysMngrYn("Y");
            }
            tblRelInstMbrRepository.saveAll(tblRelInstMbrList);

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO cancleMng(TblRelInstMbr tblRelInstMbr){
        ResultVO resultVO = new ResultVO();

        try{
            tblRelInstMbr.setSysMngrYn("N");
            tblRelInstMbr.setRelInstSn(tblRelInstMbr.getRelInstSn());
            tblRelInstMbrRepository.save(tblRelInstMbr);

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

}