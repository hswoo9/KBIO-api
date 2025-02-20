package egovframework.com.devjitsu.service.user;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.user.QTblUser;
import egovframework.com.devjitsu.model.user.QTblUserMsg;
import egovframework.com.devjitsu.model.user.TblUserMsg;
import egovframework.com.devjitsu.model.user.UserMsgDto;
import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserMsgAdminApiService {

    private final EntityManager em;

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

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

            /** 발신사용자 */
            QTblUser qDsptchUser = QTblUser.tblUser;
            /** 수신사용자 */
            QTblUser qRcptnUser = new QTblUser("qRcptnUser");
            QTblUserMsg qTblUserMsg = QTblUserMsg.tblUserMsg;
            JPAQueryFactory q = new JPAQueryFactory(em);
            BooleanBuilder builder = new BooleanBuilder();
            builder.and(qTblUserMsg.actvtnYn.eq("Y"));

            if (!StringUtils.isEmpty(dto.get("searchType"))) {
                if(dto.get("searchType").equals("msgTtl")){
                    builder.and(qTblUserMsg.msgTtl.contains((String) dto.get("searchVal")));
                }else if(dto.get("searchType").equals("msgCn")){
                    builder.and(qTblUserMsg.msgCn.contains((String) dto.get("searchVal")));
                }else if(dto.get("searchType").equals("dsptchUser")){
                    builder.and(qDsptchUser.kornFlnm.contains((String) dto.get("searchVal")));
                }else if(dto.get("searchType").equals("rcptnUser")){
                    builder.and(qRcptnUser.kornFlnm.contains((String) dto.get("searchVal")));
                }
            }else{
                builder.and(
                    qTblUserMsg.msgTtl.contains((String) dto.get("searchVal"))
                        .or(qTblUserMsg.msgCn.contains((String) dto.get("searchVal")))
                        .or(qDsptchUser.kornFlnm.contains((String) dto.get("searchVal")))
                        .or(qRcptnUser.kornFlnm.contains((String) dto.get("searchVal")))
                );
            }

            List<UserMsgDto> userMsgList = q
                .select(
                    Projections.constructor(
                        UserMsgDto.class,
                        qTblUserMsg,
                        qDsptchUser,
                        qRcptnUser
                    )
                )
                .from(qTblUserMsg)
                .join(qDsptchUser).on(qTblUserMsg.dsptchUserSn.eq(qDsptchUser.userSn))
                .join(qRcptnUser).on(qTblUserMsg.rcptnUserSn.eq(qRcptnUser.userSn))
                .where(builder)
                .orderBy(qTblUserMsg.frstCrtDt.desc())
                .offset(paginationInfo.getFirstRecordIndex())
                .limit(paginationInfo.getRecordCountPerPage())
                .fetch();

            Long totCnt = q.select(qTblUserMsg.count())
                    .from(qTblUserMsg)
                    .join(qDsptchUser).on(qTblUserMsg.dsptchUserSn.eq(qDsptchUser.userSn))
                    .join(qRcptnUser).on(qTblUserMsg.rcptnUserSn.eq(qRcptnUser.userSn))
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
}
