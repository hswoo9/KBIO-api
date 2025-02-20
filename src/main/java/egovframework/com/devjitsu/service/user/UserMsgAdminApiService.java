package egovframework.com.devjitsu.service.user;

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

        try {
            QTblUser qTblUser = QTblUser.tblUser;
            QTblUserMsg qTblUserMsg = QTblUserMsg.tblUserMsg;
            JPAQueryFactory q = new JPAQueryFactory(em);

            List<UserMsgDto> userMsgList = q
                .select(
                        Projections.constructor(
                                UserMsgDto.class,
                                qTblUserMsg,
                                qTblUser
                        )
                )
                .from(qTblUserMsg)
                .join(qTblUser).on(qTblUserMsg.dsptchUserSn.eq(qTblUser.userSn))

                .where(
                        qTblUserMsg.actvtnYn.eq("Y")
                        .and(qTblUserMsg.expsrYn.eq("Y"))
                )
                .orderBy(qTblUserMsg.frstCrtDt.desc())
                .fetch();

            resultVO.putResult("userMsgList", userMsgList);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }
}
