package egovframework.com.devjitsu.service.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.QTblComCdGroup;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.login.LettnemplyrinfoVO;
import egovframework.com.devjitsu.model.login.QLettnemplyrinfoVO;
import egovframework.com.devjitsu.model.user.QTblUserSnsCertInfo;
import egovframework.com.devjitsu.model.user.TblUserSnsCertInfo;
import egovframework.com.devjitsu.repository.common.TblComCdGroupRepository;
import egovframework.com.devjitsu.repository.common.TblComCdRepository;
import egovframework.com.devjitsu.repository.common.TblComFileRepository;
import egovframework.com.devjitsu.repository.login.LettnemplyrinfoRepository;
import egovframework.com.devjitsu.service.common.RedisApiService;
import egovframework.com.jwt.EgovJwtTokenUtil;
import egovframework.let.utl.sim.service.EgovFileScrty;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberApiService {

    @Autowired
    private EgovJwtTokenUtil jwtTokenUtil;

    @Autowired
    private RedisApiService redisApiService;

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

    public ResultVO checkMemberId(SearchDto dto) {

        ResultVO resultVO = new ResultVO();

        QLettnemplyrinfoVO qLettnemplyrinfoVO = QLettnemplyrinfoVO.lettnemplyrinfoVO;
        LettnemplyrinfoVO lettnemplyrinfoVO = new JPAQueryFactory(em)
                .selectFrom(qLettnemplyrinfoVO)
                .where(qLettnemplyrinfoVO.emplyrId.eq(dto.get("memberId").toString()))
                .fetchOne();

        if (lettnemplyrinfoVO != null) {
            resultVO.setResultCode(400);  // 중복된 아이디 코드
            resultVO.putResult("usedCnt", 1);  // 1개 중복된 아이디 존재
        } else {
            // 사용 가능한 ID일 경우
            resultVO.setResultCode(200);  // 사용 가능한 아이디 코드
            resultVO.putResult("usedCnt", 0);  // 중복되지 않음
        }

        return resultVO;
    }

    public ResultVO insertMember(SearchDto dto) {
        ResultVO resultVO = new ResultVO();

        // SearchDto에서 값 추출
        String userNm = (String) dto.get("mberId");
        String emplyrId = (String) dto.get("mberNm");
        String password = (String) dto.get("password");
        String houseAdres = (String) dto.get("searchAddress");
        String emailAdres = (String) dto.get("emailPrefix") +"@"+  dto.get("emailDomain");
        String mbtlnum = (String) dto.get("phonenum");

        // LettnemplyrinfoVO에 데이터 설정
        LettnemplyrinfoVO member = new LettnemplyrinfoVO();
        member.setUserNm(userNm);
        member.setEmplyrId(emplyrId);
        member.setPassword(password);
        member.setHouseAdres(houseAdres);
        member.setEmailAdres(emailAdres);
        member.setMbtlnum(mbtlnum);

        // 데이터 저장
        lettnemplyrinfoRepository.save(member);

        // 성공 메시지 설정
        resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        resultVO.setResultMessage("회원 등록이 성공적으로 완료되었습니다.");
        return resultVO;
    }
}