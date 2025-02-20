package egovframework.com.devjitsu.service.user;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.user.*;
import egovframework.com.devjitsu.repository.user.TblMvnEntMbrRepository;
import egovframework.com.devjitsu.repository.user.TblMvnEntRepository;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserMsgApiService {

    private final EntityManager em;

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    public ResultVO getUserMsgTopList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblUser qTblUser = QTblUser.tblUser;
            QTblUserMsg qTblUserMsg = QTblUserMsg.tblUserMsg;
            JPAQueryFactory q = new JPAQueryFactory(em);

            if(!StringUtils.isEmpty(dto.get("userSn"))){
                List<UserMsgDto> userMsgList = q
                        .select(
                                Projections.constructor(
                                        UserMsgDto.class,
                                        qTblUserMsg,
                                        qTblUser,
                                        Expressions.constant("")
                                )
                        )
                        .from(qTblUserMsg)
                        .join(qTblUser).on(qTblUserMsg.dsptchUserSn.eq(qTblUser.userSn))

                        .where(
                                qTblUserMsg.actvtnYn.eq("Y")
                                        .and(qTblUserMsg.rcptnUserSn.eq(Long.parseLong(dto.get("userSn").toString())))
                                        .and(qTblUserMsg.expsrYn.eq("Y"))
                        )
                        .orderBy(qTblUserMsg.frstCrtDt.desc())
                        .fetch();

                resultVO.putResult("userMsgTopList", userMsgList);
            }

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getUserMsgList(SearchDto dto) {
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

            QTblUser qTblUser = QTblUser.tblUser;
            QTblUserMsg qTblUserMsg = QTblUserMsg.tblUserMsg;
            JPAQueryFactory q = new JPAQueryFactory(em);
            BooleanBuilder builder = new BooleanBuilder();
            builder.and(qTblUserMsg.actvtnYn.eq("Y"))
                    .and(qTblUserMsg.rcptnUserSn.eq(Long.parseLong(dto.get("userSn").toString())));

            if (!StringUtils.isEmpty(dto.get("searchType"))) {
                if(dto.get("searchType").equals("msgTtl")){
                    builder.and(qTblUserMsg.msgTtl.contains((String) dto.get("searchVal")));
                }else if(dto.get("searchType").equals("msgCn")){
                    builder.and(qTblUserMsg.msgCn.contains((String) dto.get("searchVal")));
                }else if(dto.get("searchType").equals("kornFlnm")){
                    builder.and(qTblUser.kornFlnm.contains((String) dto.get("searchVal")));
                }
            }else{
                builder.and(
                        qTblUserMsg.msgTtl.contains((String) dto.get("searchVal"))
                                .or(qTblUserMsg.msgCn.contains((String) dto.get("searchVal")))
                                .or(qTblUser.kornFlnm.contains((String) dto.get("searchVal")))
                );
            }

            List<UserMsgDto> userMsgList = q
                    .select(
                            Projections.constructor(
                                    UserMsgDto.class,
                                    qTblUserMsg,
                                    qTblUser,
                                    Expressions.constant("")
                            )
                    )
                    .from(qTblUserMsg)
                    .join(qTblUser).on(qTblUserMsg.dsptchUserSn.eq(qTblUser.userSn))
                    .where(builder)
                    .orderBy(qTblUserMsg.frstCrtDt.desc())
                    .offset(paginationInfo.getFirstRecordIndex())
                    .limit(paginationInfo.getRecordCountPerPage())
                    .fetch();

            Long totCnt = q.select(qTblUserMsg.count())
                    .from(qTblUserMsg)
                    .join(qTblUser).on(qTblUserMsg.dsptchUserSn.eq(qTblUser.userSn))
                    .where(builder)
                    .fetchOne();
            if(totCnt == null) totCnt = 0L;
            paginationInfo.setTotalRecordCount(totCnt.intValue());

            resultVO.putResult("userMsgList", userMsgList);
            resultVO.putPaginationInfo(paginationInfo);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setUserMsgExpsrYn(TblUserMsg tblUserMsg) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblUserMsg qTblUserMsg = QTblUserMsg.tblUserMsg;
            JPAQueryFactory q = new JPAQueryFactory(em);

            q.update(qTblUserMsg)
                    .set(qTblUserMsg.expsrYn, "N")
                    .set(qTblUserMsg.mdfrSn, tblUserMsg.getMdfrSn())
                    .where(qTblUserMsg.msgSn.eq(tblUserMsg.getMsgSn()))
                    .execute();

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setMsgConfirm(TblUserMsg tblUserMsg) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblUserMsg qTblUserMsg = QTblUserMsg.tblUserMsg;
            JPAQueryFactory q = new JPAQueryFactory(em);

            q.update(qTblUserMsg)
                    .set(qTblUserMsg.rcptnIdntyYn, "Y")
                    .set(qTblUserMsg.mdfrSn, tblUserMsg.getMdfrSn())
                    .set(qTblUserMsg.mdfcnDt, LocalDateTime.now())
                    .where(qTblUserMsg.msgSn.eq(tblUserMsg.getMsgSn()))
                    .execute();

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
        }

        return resultVO;
    }
}
