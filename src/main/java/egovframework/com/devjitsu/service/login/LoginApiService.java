package egovframework.com.devjitsu.service.login;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.QTblComCdGroup;
import egovframework.com.devjitsu.model.login.LettnemplyrinfoVO;
import egovframework.com.devjitsu.model.login.QLettnemplyrinfoVO;
import egovframework.com.devjitsu.repository.common.TblComCdGroupRepository;
import egovframework.com.devjitsu.repository.common.TblComCdRepository;
import egovframework.com.devjitsu.repository.common.TblComFileRepository;
import egovframework.com.devjitsu.repository.login.LettnemplyrinfoRepository;
import egovframework.let.utl.sim.service.EgovFileScrty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginApiService {

    private final EntityManager em;
    private final LettnemplyrinfoRepository lettnemplyrinfoRepository;
    /**
     * jpa 부등호
     * gt : >
     * lt : <
     * goe : >=
     * loe : <=
     */
    /**
     *  query DSL 조건 추가하는 방법
     *  BooleanBuilder builder = new BooleanBuilder();
     *  builder.and(qTblComCdGroup.actvtnYn.eq("Y"));
     * */

    public LettnemplyrinfoVO actionLogin(LoginVO vo) throws Exception {
        String enpassword = EgovFileScrty.encryptPassword(vo.getPassword(), vo.getId());

        QLettnemplyrinfoVO qLettnemplyrinfoVO = QLettnemplyrinfoVO.lettnemplyrinfoVO;
        JPAQueryFactory q = new JPAQueryFactory(em);

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qLettnemplyrinfoVO.emplyrId.eq(vo.getId()));
        builder.and(qLettnemplyrinfoVO.password.eq(enpassword));

        LettnemplyrinfoVO loginVO = q.selectFrom(qLettnemplyrinfoVO).where(builder).fetchOne();

        if (loginVO != null && !loginVO.getEmplyrId().equals("") && !loginVO.getPassword().equals("")) {
            return loginVO;
        } else {
            loginVO = new LettnemplyrinfoVO();
        }

        return loginVO;
    }
}
