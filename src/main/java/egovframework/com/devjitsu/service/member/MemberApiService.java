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
            resultVO.setResultCode(400);
            resultVO.putResult("usedCnt", 1);
        } else {
            resultVO.setResultCode(200);
            resultVO.putResult("usedCnt", 0);
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


    public ResultVO insertMember(SearchDto dto) {
        ResultVO resultVO = new ResultVO();

        LettnemplyrinfoVO member = new LettnemplyrinfoVO();
        member.setUserNm((String) dto.get("mberNm"));
        member.setEmplyrId((String) dto.get("mberId"));
        member.setPassword((String) dto.get("password"));
        member.setHouseAdres((String) dto.get("searchAddress"));
        member.setEmailAdres(dto.get("emailPrefix") + "@" + dto.get("emailDomain"));
        member.setMbtlnum((String) dto.get("phonenum"));

        lettnemplyrinfoRepository.save(member);

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