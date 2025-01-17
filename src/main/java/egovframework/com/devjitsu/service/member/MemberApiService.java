package egovframework.com.devjitsu.service.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.login.LettnemplyrinfoVO;
import egovframework.com.devjitsu.model.login.QLettnemplyrinfoVO;
import egovframework.com.devjitsu.model.user.QTblMvnEnt;
import egovframework.com.devjitsu.model.user.TblMvnEnt;
import egovframework.com.devjitsu.repository.login.LettnemplyrinfoRepository;
import egovframework.com.devjitsu.service.common.RedisApiService;
import egovframework.com.jwt.EgovJwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

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

    public ResultVO checkBusiness(SearchDto dto) {

        ResultVO resultVO = new ResultVO();

        QTblMvnEnt tblMvnEnt = QTblMvnEnt.tblMvnEnt;

        JPAQueryFactory q = new JPAQueryFactory(em);
        TblMvnEnt List = q.selectFrom(tblMvnEnt)
                .where(tblMvnEnt.brno.eq(dto.get("businessNumber").toString()))
                .fetchOne();

        if (List != null) {
            resultVO.setResultCode(200);
            resultVO.putResult("businessCnt", 1);
            resultVO.putResult("businessData", List);
        } else {
            resultVO.setResultCode(400);
            resultVO.putResult("businessCnt", 0);
            resultVO.putResult("businessData", null);
        }

        return resultVO;
    }

    public ResultVO insertMember(SearchDto dto) {
        ResultVO resultVO = new ResultVO();

        // SearchDto에서 값 추출
        String userNm = (String) dto.get("mberNm");
        String emplyrId = (String) dto.get("mberId");
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