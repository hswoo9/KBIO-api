package egovframework.com.devjitsu.service.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.login.LettnemplyrinfoVO;
import egovframework.com.devjitsu.model.login.QLettnemplyrinfoVO;
import egovframework.com.devjitsu.model.user.QTblMvnEnt;
import egovframework.com.devjitsu.model.user.QTblUser;
import egovframework.com.devjitsu.model.user.TblMvnEnt;
import egovframework.com.devjitsu.model.user.TblUser;
import egovframework.com.devjitsu.repository.login.LettnemplyrinfoRepository;
import egovframework.com.devjitsu.repository.user.TblUserRepository;
import egovframework.com.devjitsu.service.common.RedisApiService;
import egovframework.com.jwt.EgovJwtTokenUtil;
import egovframework.let.utl.sim.service.EgovFileScrty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

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
    private final TblUserRepository TblUserRepository;

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

        QTblUser qTblUser = QTblUser.tblUser;
        TblUser tblUser = new JPAQueryFactory(em)
                .selectFrom(qTblUser)
                .where(qTblUser.userId.eq(dto.get("userId").toString()))
                .fetchOne();

        if (tblUser != null) {
            resultVO.setResultCode(400);
            resultVO.putResult("usedCnt", 1); // 아이디가 사용 중인 경우
        } else {
            resultVO.setResultCode(200);
            resultVO.putResult("usedCnt", 0); // 아이디가 사용 가능
        }

        return resultVO;
    }


    public ResultVO checkBusiness(SearchDto dto) {
        ResultVO resultVO = new ResultVO();

        QTblMvnEnt tblMvnEnt = QTblMvnEnt.tblMvnEnt;

        TblMvnEnt businessInfo = new JPAQueryFactory(em)
                .selectFrom(tblMvnEnt)
                .where(tblMvnEnt.brno.eq(dto.get("businessNumber").toString()))
                .fetchOne();

        if (businessInfo != null) {
            resultVO.setResultCode(200);
            resultVO.putResult("businessCnt", 1);
            resultVO.putResult("businessData", businessInfo);
        } else {
            resultVO.setResultCode(400);
            resultVO.putResult("businessCnt", 0);
            resultVO.putResult("businessData", null);
        }

        return resultVO;
    }


    public ResultVO insertMember(SearchDto dto) throws Exception {
        ResultVO resultVO = new ResultVO();

        TblUser member = new TblUser(); // TblUser 엔티티로 변경
        member.setKornFlnm((String) dto.get("kornFlnm")); // 성명
        member.setUserId((String) dto.get("userId")); // 사용자 ID
        // 비밀번호 해시 처리
        String hashedPswd = EgovFileScrty.encryptPassword((String) dto.get("userPw"), (String) dto.get("userId"));
        member.setUserPw(hashedPswd); // 비밀번호
        member.setAddr((String) dto.get("addr")); // 주소
        member.setDaddr((String) dto.get("daddr")); // 상세 주소
        member.setZip((String) dto.get("zip")); // 우편번호
        member.setEmail(dto.get("emailPrefix") + "@" + dto.get("emailDomain")); // 이메일
        member.setMblTelno((String) dto.get("mblTelno")); // 휴대폰 번호
        member.setEmlRcptnAgreYn((String) dto.get("emlRcptnAgreYn")); // 이메일 수신 동의 여부
        member.setSmsRcptnAgreYn((String) dto.get("smsRcptnAgreYn")); // SMS 수신 동의 여부
        member.setInfoRlsYn((String) dto.get("infoRlsYn")); // 정보 공개 여부
        member.setActvtnYn("W"); // 활성 여부 기본값 설정

        // 추가적인 필드 설정이 필요할 경우 여기에 추가

        // 회원 정보 저장
        TblUserRepository.save(member);

        resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        resultVO.setResultMessage("회원 등록이 성공적으로 완료되었습니다.");
        return resultVO;
    }



    public ResultVO findId(SearchDto dto) {
        ResultVO resultVO = new ResultVO();

        QLettnemplyrinfoVO qLettnemplyrinfoVO = QLettnemplyrinfoVO.lettnemplyrinfoVO;

        LettnemplyrinfoVO lettnemplyrinfoVO = new JPAQueryFactory(em)
                .selectFrom(qLettnemplyrinfoVO)
                .where(
                        qLettnemplyrinfoVO.userNm.eq(dto.get("name").toString())
                                .and(qLettnemplyrinfoVO.emailAdres.eq(dto.get("email").toString()))
                )
                .fetchOne();

        if (lettnemplyrinfoVO != null) {
            resultVO.setResultCode(200);
            resultVO.putResult("memberId", lettnemplyrinfoVO.getEmplyrId());
        } else {
            resultVO.setResultCode(400);
            resultVO.putResult("memberId", null);
        }

        return resultVO;
    }

    public ResultVO findPassword(SearchDto dto) {
        ResultVO resultVO = new ResultVO();

        QLettnemplyrinfoVO qLettnemplyrinfoVO = QLettnemplyrinfoVO.lettnemplyrinfoVO;
        LettnemplyrinfoVO lettnemplyrinfoVO = new JPAQueryFactory(em)
                .selectFrom(qLettnemplyrinfoVO)
                .where(
                        qLettnemplyrinfoVO.emplyrId.eq((String) dto.get("id"))
                                .and(qLettnemplyrinfoVO.userNm.eq((String) dto.get("name")))
                                .and(qLettnemplyrinfoVO.emailAdres.eq((String) dto.get("email")))
                )
                .fetchOne();

        // 회원 정보가 없을 경우
        if (lettnemplyrinfoVO == null) {
            resultVO.setResultCode(400);
            resultVO.putResult("message", "회원 정보를 찾을 수 없습니다.");
            return resultVO;
        }

        // 임시 비밀번호 생성
        String tempPassword = generateRandomPassword();

        // 비밀번호를 VO에 설정 및 저장
        lettnemplyrinfoVO.setPassword(tempPassword);
        lettnemplyrinfoRepository.save(lettnemplyrinfoVO);

        // 이메일 전송
        boolean emailSent = sendEmail(
                lettnemplyrinfoVO.getEmailAdres(),
                "안녕하세요. 회원님의 임시 비밀번호는 다음과 같습니다: " + tempPassword + "\n로그인 후 비밀번호를 변경해 주세요."
        );

        // 이메일 전송 실패 처리
        if (!emailSent) {
            resultVO.setResultCode(400);
            resultVO.putResult("message", "임시 비밀번호 이메일 전송에 실패했습니다.");
            return resultVO;
        }

        // 성공 응답 처리
        resultVO.setResultCode(200);
        resultVO.putResult("message", "임시 비밀번호가 이메일로 발송되었습니다.");
        return resultVO;
    }


    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(chars.length());
            password.append(chars.charAt(index));
        }

        return password.toString();
    }


    private boolean sendEmail(String to, String body) {
        String host = "smtp.gmail.com";
        final String username = "gksthe@gmail.com";
        final String password = "mqhgtubmkdtpsoaa";

        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("임시 비밀번호 발급");
            message.setText(body);

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}