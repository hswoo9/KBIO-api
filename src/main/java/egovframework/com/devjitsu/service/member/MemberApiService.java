package egovframework.com.devjitsu.service.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.login.LettnemplyrinfoVO;
import egovframework.com.devjitsu.model.login.QLettnemplyrinfoVO;
import egovframework.com.devjitsu.model.user.*;
import egovframework.com.devjitsu.repository.login.LettnemplyrinfoRepository;
import egovframework.com.devjitsu.repository.user.TblMvnEntMbrRepository;
import egovframework.com.devjitsu.repository.user.TblUserRepository;
import egovframework.com.devjitsu.repository.user.TblUserSnsCertInfoRepository;
import egovframework.com.devjitsu.service.common.RedisApiService;
import egovframework.com.jwt.EgovJwtTokenUtil;
import egovframework.let.utl.sim.service.EgovFileScrty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.Optional;
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
    private final TblMvnEntMbrRepository tblMvnEntMbrRepository;
    private final TblUserSnsCertInfoRepository tblUserSnsCertInfoRepository;

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
        String hashedPswd = EgovFileScrty.encryptPassword((String) dto.get("userPw"), (String) dto.get("userId"));
        member.setUserPw(hashedPswd); // 비밀번호
        member.setAddr((String) dto.get("addr")); // 주소
        member.setDaddr((String) dto.get("daddr")); // 상세 주소
        member.setZip((String) dto.get("zip")); // 우편번호
        member.setEmail(dto.get("emailPrefix") + "@" + dto.get("emailDomain")); // 이메일
        /*member.setMblTelno((String) dto.get("mblTelno")); // 휴대폰 번호*/
        String encryptedMblTelno = EgovFileScrty.encode((String) dto.get("mblTelno")); // 휴대폰 번호 암호화
        member.setMblTelno(encryptedMblTelno);
        member.setEmlRcptnAgreYn((String) dto.get("emlRcptnAgreYn")); // 이메일 수신 동의 여부
        member.setSmsRcptnAgreYn((String) dto.get("smsRcptnAgreYn")); // SMS 수신 동의 여부
        member.setInfoRlsYn((String) dto.get("infoRlsYn")); // 정보 공개 여부
        member.setActvtnYn("W"); // 활성 여부 기본값 설정
        member.setMbrType((Integer) dto.get("mbrType")); // 회원 타입

        TblUser savedMember = TblUserRepository.save(member);
        Long userSn = savedMember.getUserSn();
        if(!StringUtils.isEmpty(dto.get("snsType"))){
            if(dto.get("snsType").toString().equals("naver")){
                TblUserSnsCertInfo tblUserSnsCertInfo = new TblUserSnsCertInfo();
                tblUserSnsCertInfo.setUserSn(userSn);
                tblUserSnsCertInfo.setSnsClsf(dto.get("snsType").toString());
                tblUserSnsCertInfo.setSnsUnqNo(dto.get("snsId").toString());
                tblUserSnsCertInfo.setCreatrSn(userSn);
                tblUserSnsCertInfoRepository.save(tblUserSnsCertInfo);
            }
        }
        Object mbrTypeObj = dto.get("mbrType");
        Integer mbrType = (mbrTypeObj instanceof Integer) ? (Integer) mbrTypeObj : null;

        if (Integer.valueOf(1).equals(mbrType)) {
            TblMvnEntMbr mvnEntMbr = new TblMvnEntMbr();

            Object mvnEntSnObj = dto.get("mvnEntSn");
            Long mvnEntSn = (mvnEntSnObj instanceof Number) ? ((Number) mvnEntSnObj).longValue() : null;
            System.out.println("**mvnEntSn : **" + mvnEntSn);

            mvnEntMbr.setUserSn(userSn);
            mvnEntMbr.setMvnEntSn(mvnEntSn);


            tblMvnEntMbrRepository.save(mvnEntMbr);
        }


        resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        resultVO.setResultMessage("회원 등록이 성공적으로 완료되었습니다.");
        return resultVO;
    }



    public ResultVO findId(SearchDto dto) {
        ResultVO resultVO = new ResultVO();

        QTblUser qTblUser = QTblUser.tblUser;

        TblUser tblUser = new JPAQueryFactory(em)
                .selectFrom(qTblUser)
                .where(
                        qTblUser.kornFlnm.eq(dto.get("name").toString())
                                .and(qTblUser.email.eq(dto.get("email").toString()))
                )
                .fetchOne();

        if (tblUser != null) {
            resultVO.setResultCode(200);
            resultVO.putResult("userId", tblUser.getUserId());
        } else {
            resultVO.setResultCode(400);
            resultVO.putResult("userId", null);
        }

        return resultVO;
    }

    public ResultVO findPassword(SearchDto dto) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblUser qTblUser = QTblUser.tblUser;

            TblUser tblUser = new JPAQueryFactory(em)
                    .selectFrom(qTblUser)
                    .where(
                            qTblUser.userId.eq((String) dto.get("id"))
                                    .and(qTblUser.kornFlnm.eq((String) dto.get("name")))
                                    .and(qTblUser.email.eq((String) dto.get("email")))
                    )
                    .fetchOne();

            // 회원 정보가 없을 경우
            if (tblUser == null) {
                resultVO.setResultCode(400);
                resultVO.putResult("message", "회원 정보를 찾을 수 없습니다.");
                return resultVO;
            }

            String tempPassword = generateRandomPassword();

            // 비밀번호를 해시 처리하여 VO에 설정 및 저장
            String hashedTempPassword = EgovFileScrty.encryptPassword(tempPassword, tblUser.getUserId());
            tblUser.setUserPw(hashedTempPassword);
            TblUserRepository.save(tblUser);

            // 이메일 전송
            boolean emailSent = sendEmail(
                    tblUser.getEmail(),
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
        } catch (Exception e) {
            resultVO.setResultCode(500);
            resultVO.setResultMessage("비밀번호 찾기 중 오류가 발생했습니다: " + e.getMessage());
            e.printStackTrace();
        }

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