package egovframework.com.devjitsu.service.user;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.ResultVO;

import egovframework.com.devjitsu.model.common.QTblComFile;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.common.TblComFile;
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

    public ResultVO setMvnEnt(TblMvnEnt tblMvnEnt, List<MultipartFile> files){
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
            JPAQueryFactory q = new JPAQueryFactory(em);

            BooleanBuilder builder = new BooleanBuilder();

            if(!StringUtils.isEmpty(dto.get("mvnEntNm"))){
                builder.and(qTblMvnEnt.mvnEntNm.contains((String) dto.get("mvnEntNm")));
            }
            if(!StringUtils.isEmpty(dto.get("brno"))){
                builder.and(qTblMvnEnt.brno.contains((String) dto.get("brno")));
            }
            if(!StringUtils.isEmpty(dto.get("rpsvNm"))){
                builder.and(qTblMvnEnt.rpsvNm.contains((String) dto.get("rpsvNm")));
            }

            List<TblMvnEnt> tblMvnEntList = q.selectFrom(qTblMvnEnt)
                    .where(builder)
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

            resultVO.putResult("rc",tblMvnEntRepository.findByMvnEntSn(tblMvnEnt.getMvnEntSn()));
            resultVO.putResult("logoFile",residentCompanyLogo);
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
        List<TblMvnEntMbr> entMbrList;
        QTblMvnEntMbr qTblMvnEntMbr = QTblMvnEntMbr.tblMvnEntMbr;
        QTblComFile qTblComFile = QTblComFile.tblComFile;
        BooleanBuilder builder = new BooleanBuilder();
        JPAQueryFactory q = new JPAQueryFactory(em);

        long mvnEntSn = ((Number)dto.get("mvnEntSn")).longValue();

            TblComFile residentCompanyLogo = q.selectFrom(qTblComFile)
                    .where(
                            qTblComFile.psnTblSn.eq(
                                    Expressions.stringTemplate("CONCAT('mvnEnt_',{0})", mvnEntSn)
                            )
                    ).fetchOne();

        if (!StringUtils.isEmpty(dto.get("sysMngrYn"))){
            //관리자설정에서 실행됨
            builder.and(qTblMvnEntMbr.sysMngrYn.contains((String) dto.get("sysMngrYn")));
            builder.and(qTblMvnEntMbr.mvnEntSn.eq(mvnEntSn));

            entMbrList = q.selectFrom(qTblMvnEntMbr)
                    .where(builder)
                    .orderBy(qTblMvnEntMbr.userSn.desc()).fetch();

        } else {
            //직원목록일 경우 실행됨
            entMbrList = tblMvnEntMbrRepository.findUserSnByMvnEntSn(mvnEntSn);
        }

        //System.out.println("****entMbrList :*****"+entMbrList);

        List<Long> userSnList = entMbrList.stream()
                .map(TblMvnEntMbr::getUserSn)
                .collect(Collectors.toList());



        if (!StringUtils.isEmpty(dto.get("actvtnYn"))) {
            conditions.put("actvtnYn", dto.get("actvtnYn"));
        }
        if (!StringUtils.isEmpty(dto.get("kornFlnm"))){
            conditions.put("kornFlnm", dto.get("kornFlnm"));
        }
        if (!StringUtils.isEmpty(dto.get("userId"))){
            conditions.put("userId", dto.get("userId"));
        }

        List<TblUser> userList = getFilteredUsers(userSnList, conditions);
        Long totCnt = Long.valueOf(userList.size());



        if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
                paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
        }
        paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
        paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));

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


    public List<TblUser> getFilteredUsers(List<Long> userSnList, Map<String, Object> conditions) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TblUser> query = cb.createQuery(TblUser.class);
        Root<TblUser> root = query.from(TblUser.class);

        List<Predicate> predicates = new ArrayList<>();

        // userSn IN 조건
        predicates.add(root.get("userSn").in(userSnList));

        // DTO에서 넘어온 조건 추가
        if (conditions.containsKey("actvtnYn")) {
            predicates.add(cb.equal(root.get("actvtnYn"), conditions.get("actvtnYn")));
        }
        if (conditions.containsKey("kornFlnm")) {
            predicates.add(cb.equal(root.get("kornFlnm"), conditions.get("kornFlnm")));
        }
        if (conditions.containsKey("userId")) {
            predicates.add(cb.equal(root.get("userId"), conditions.get("userId")));
        }

        query.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(query).getResultList();
    }







}
