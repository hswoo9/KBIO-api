package egovframework.com.devjitsu.service.user;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;

import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.user.*;

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
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    public ResultVO setRelInst(TblRelInst tblRelInst){
        ResultVO resultVO = new ResultVO();

        try{
            QTblRelInst qTblRelInst = QTblRelInst.tblRelInst;
            JPAQueryFactory q = new JPAQueryFactory(em);

            tblRelInstRepository.save(tblRelInst);

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
            JPAQueryFactory q = new JPAQueryFactory(em);

            BooleanBuilder builder = new BooleanBuilder();

            if(!StringUtils.isEmpty(dto.get("relInstNm"))){
                builder.and(qTblRelInst.relInstNm.contains((String) dto.get("relInstNm")));
            }
            if(!StringUtils.isEmpty(dto.get("brno"))){
                builder.and(qTblRelInst.brno.contains((String) dto.get("brno")));
            }
            if(!StringUtils.isEmpty(dto.get("rpsvNm"))){
                builder.and(qTblRelInst.rpsvNm.contains((String) dto.get("rpsvNm")));
            }

            List<TblRelInst> tblRelInstList = q.selectFrom(qTblRelInst)
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
            resultVO.putResult("rc",tblRelInstRepository.findByRelInstSn(tblRelInst.getRelInstSn()));
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
            BooleanBuilder builder = new BooleanBuilder();
            JPAQueryFactory q = new JPAQueryFactory(em);

            long relInstSn = ((Number)dto.get("relInstSn")).longValue();


            System.out.println("****relInstSn :*****"+relInstSn);

            if (!StringUtils.isEmpty(dto.get("sysMngrYn"))){
                //관리자설정에서 실행됨
                builder.and(qTblRelInstMbr.sysMngrYn.contains((String) dto.get("sysMngrYn")));
                builder.and(qTblRelInstMbr.relInstSn.eq(relInstSn));

                relInstList = q.selectFrom(qTblRelInstMbr)
                        .where(builder)
                        .orderBy(qTblRelInstMbr.userSn.desc()).fetch();

            } else {
                //직원목록일 경우 실행됨
                relInstList = tblRelInstMbrRepository.findUserSnByRelInstSn(relInstSn);
            }

            System.out.println("****entMbrList :*****"+relInstList);

            List<Long> userSnList = relInstList.stream()
                    .map(TblRelInstMbr::getUserSn)
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


            resultVO.putResult("getRelatedMemberList",userList);
            resultVO.putPaginationInfo(paginationInfo);

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());

        }catch (Exception e){
            e.printStackTrace();
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

    public ResultVO getRelatedMemberOne(SearchDto dto){
        ResultVO resultVO = new ResultVO();
        long userSn = ((Number)dto.get("userSn")).longValue();
        long relInstSn = ((Number)dto.get("relInstSn")).longValue();
        try {
            resultVO.putResult("member", tblUserRepository.findByUserSn(userSn));
            resultVO.putResult("rc",tblRelInstRepository.findByRelInstSn(relInstSn));
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());

        }

        return resultVO;
    }
}