package egovframework.com.devjitsu.service.common;

import com.querydsl.core.BooleanBuilder;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.QTblComCdGroup;
import egovframework.com.devjitsu.repository.common.TblComCdGroupRepository;
import egovframework.com.devjitsu.repository.common.TblComCdRepository;
import egovframework.com.devjitsu.repository.common.TblComFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class CommonApiService {

    private final EntityManager em;
    /**
     * jpa 부등호
     * gt : >
     * lt : <
     * goe : >=
     * loe : <=
     */
    private final TblComCdRepository tblComCdRepository;
    private final TblComCdGroupRepository tblComCdGroupRepository;
    private final TblComFileRepository tblComFileRepository;

    public ResultVO getComCdGroupList(Map<String, Object> params) {
        ResultVO resultVO = new ResultVO();

        QTblComCdGroup qTblComCdGroup = QTblComCdGroup.tblComCdGroup;
        JPAQueryFactory q = new JPAQueryFactory(em);

        /** query DSL 조건 추가하는 방법 */
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qTblComCdGroup.actvtnYn.eq("Y"));

        resultVO.putResult("rs",  q.selectFrom(qTblComCdGroup).where(builder).orderBy(qTblComCdGroup.frstCrtDt.desc()).fetch());
        resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        return resultVO;
    }
}
