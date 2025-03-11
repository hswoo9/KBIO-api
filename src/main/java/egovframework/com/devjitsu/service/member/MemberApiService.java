package egovframework.com.devjitsu.service.member;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.QTblComCd;
import egovframework.com.devjitsu.model.common.QTblComFile;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.common.TblComFile;
import egovframework.com.devjitsu.model.consult.*;
import egovframework.com.devjitsu.model.menu.TblMenuAuthrtGroupUser;
import egovframework.com.devjitsu.model.user.TblCnslttMbr;
import egovframework.com.devjitsu.model.user.*;
import egovframework.com.devjitsu.repository.common.TblComFileRepository;
import egovframework.com.devjitsu.repository.consult.*;
import egovframework.com.devjitsu.repository.login.LettnemplyrinfoRepository;
import egovframework.com.devjitsu.repository.menu.TblMenuAuthrtGroupUserRepository;
import egovframework.com.devjitsu.repository.user.*;
import egovframework.com.devjitsu.service.common.RedisApiService;
import egovframework.com.jwt.EgovJwtTokenUtil;
import egovframework.let.utl.sim.service.EgovFileScrty;
import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.stream.Collectors;

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
    private final TblRelInstMbrRepository tblRelInstMbrRepository;
    private final TblCnslttMbrRepository tblCnslttMbrRepository;
    private final TblUserSnsCertInfoRepository tblUserSnsCertInfoRepository;
    private final TblComFileRepository tblComFileRepository;
    private final TblDfclMttrRepository tblDfclMttrRepository;
    private final TblCnsltAplyRepository tblCnsltAplyRepository;
    private final TblCnsltDsctnRepository tblCnsltDsctnRepository;
    private final TblCnsltDgstfnRepository tblCnsltDgstfnRepository;
    private final TblQlfcLcnsRepository tblQlfcLcnsRepository;
    private final TblCrrRepository tblCrrRepository;
    private final TblAcbgRepository tblAcbgRepository;
    private final TblMvnEntRepository tblMvnEntRepository;
    private final TblRelInstRepository tblRelInstRepository;
    private final TblMenuAuthrtGroupUserRepository tblMenuAuthrtGroupUserRepository;

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    @Resource(name = "EgovFileMngUtil")
    private EgovFileMngUtil fileUtil;

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
        QTblRelInst tblRelInst = QTblRelInst.tblRelInst;

        TblMvnEnt businessInfo = new JPAQueryFactory(em)
                .selectFrom(tblMvnEnt)
                .where(tblMvnEnt.brno.eq(dto.get("businessNumber").toString()))
                .fetchOne();

        if (businessInfo != null) {
            resultVO.setResultCode(200);
            resultVO.putResult("businessType", "입주기업");
            resultVO.putResult("businessData", businessInfo);
            return resultVO;
        }

        TblRelInst relatedInstInfo = new JPAQueryFactory(em)
                .selectFrom(tblRelInst)
                .where(tblRelInst.brno.eq(dto.get("businessNumber").toString()))
                .fetchOne();

        if (relatedInstInfo != null) {
            resultVO.setResultCode(200);
            resultVO.putResult("businessType", "유관기관");
            resultVO.putResult("businessData", relatedInstInfo);
            return resultVO;
        }

        // 3. 둘 다 없으면 비입주기업 처리
        resultVO.setResultCode(400);
        resultVO.putResult("businessType", "비입주기업");
        resultVO.putResult("businessData", null);

        return resultVO;
    }




    /*
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
        String emailDomain = "direct".equals(dto.get("emailProvider")) ? (String) dto.get("emailDomain") : (String) dto.get("emailProvider");
        member.setEmail(dto.get("emailPrefix") + "@" + emailDomain); // 이메일
        //*member.setMblTelno((String) dto.get("mblTelno")); // 휴대폰 번호*//*
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
            } else if (dto.get("snsType").toString().equals("kakao")) {
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

        //입주기업회원
        if (Integer.valueOf(1).equals(mbrType)) {
            TblMvnEntMbr mvnEntMbr = new TblMvnEntMbr();

            Object mvnEntSnObj = dto.get("mvnEntSn");
            Long mvnEntSn = (mvnEntSnObj instanceof Number) ? ((Number) mvnEntSnObj).longValue() : null;

            mvnEntMbr.setUserSn(userSn);
            mvnEntMbr.setMvnEntSn(mvnEntSn);


            tblMvnEntMbrRepository.save(mvnEntMbr);

        //컨설턴트회원
        } else if(Integer.valueOf(2).equals(mbrType)) {
            TblCnslttMbr tblCnslttMbr = new TblCnslttMbr();

            tblCnslttMbr.setUserSn(userSn);
            tblCnslttMbr.setCnsltActv((String) dto.get("cnsltActv"));
            tblCnslttMbr.setOgdpNm((String) dto.get("ogdpNm"));
            tblCnslttMbr.setJbpsNm((String) dto.get("jbpsNm"));
            tblCnslttMbr.setCrrPrd(Integer.parseInt(dto.get("crrPrd").toString()) );
            tblCnslttMbr.setCnsltFld(Long.parseLong((String) dto.get("cnsltFld")));
            tblCnslttMbr.setCnsltArtcl((String) dto.get("cnsltArtcl"));
            tblCnslttMbr.setCnsltSlfint((String) dto.get("cnsltSlfint"));

            tblCnslttMbrRepository.save(tblCnslttMbr);
        }


        resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        resultVO.setResultMessage("회원 등록이 성공적으로 완료되었습니다.");
        return resultVO;
    }
    */

    public ResultVO insertMember(SearchDto dto,
                                 List<Map<String, Object>> certInfoList,
                                 List<Map<String, Object>> careerInfoList,
                                 List<Map<String, Object>> acbgInfoList,
                                 List<MultipartFile> cnsltProfileFiles,
                                 List<MultipartFile> certFiles,
                                 List<MultipartFile> careerFiles,
                                 List<MultipartFile> acbgFiles
    ) throws Exception {
        ResultVO resultVO = new ResultVO();

        TblUser member = new TblUser(); // TblUser 엔티티로 변경
        member.setKornFlnm((String) dto.get("kornFlnm")); // 성명
        member.setUserId((String) dto.get("userId")); // 사용자 ID
        String hashedPswd = EgovFileScrty.encryptPassword((String) dto.get("userPw"), (String) dto.get("userId"));
        member.setUserPw(hashedPswd); // 비밀번호
        member.setAddr((String) dto.get("addr")); // 주소
        member.setDaddr((String) dto.get("daddr")); // 상세 주소
        member.setZip((String) dto.get("zip")); // 우편번호
        String emailDomain = "direct".equals(dto.get("emailProvider")) ? (String) dto.get("emailDomain") : (String) dto.get("emailProvider");
        member.setEmail(dto.get("emailPrefix") + "@" + emailDomain); // 이메일
        //*member.setMblTelno((String) dto.get("mblTelno")); // 휴대폰 번호*//*
        String encryptedMblTelno = EgovFileScrty.encode((String) dto.get("mblTelno")); // 휴대폰 번호 암호화
        member.setMblTelno(encryptedMblTelno);
        member.setEmlRcptnAgreYn((String) dto.get("emlRcptnAgreYn")); // 이메일 수신 동의 여부
        member.setSmsRcptnAgreYn((String) dto.get("smsRcptnAgreYn")); // SMS 수신 동의 여부
        member.setInfoRlsYn((String) dto.get("infoRlsYn")); // 정보 공개 여부
        member.setActvtnYn("Y");
        member.setMbrStts("W"); // 활성 여부 기본값 설정
        member.setMbrType((Integer) dto.get("mbrType")); // 회원 타입

        TblUser savedMember = TblUserRepository.save(member);
        Long userSn = savedMember.getUserSn();

        if (Integer.valueOf(2).equals(member.getMbrType())) {
            TblMenuAuthrtGroupUser authrtGroupUser = new TblMenuAuthrtGroupUser();
            authrtGroupUser.setUserSn(userSn);
            authrtGroupUser.setAuthrtGroupSn(6); // mbrType이 2일 때 authrtGroupSn은 6
            authrtGroupUser.setActvtnYn("Y"); // 활성 여부
            authrtGroupUser.setCreatrSn(1); // 생성자 일련번호
            tblMenuAuthrtGroupUserRepository.save(authrtGroupUser);
        } else {
            TblMenuAuthrtGroupUser authrtGroupUser = new TblMenuAuthrtGroupUser();
            authrtGroupUser.setUserSn(userSn);
            authrtGroupUser.setAuthrtGroupSn(5); // 나머지 경우 authrtGroupSn은 5
            authrtGroupUser.setActvtnYn("Y"); // 활성 여부
            authrtGroupUser.setCreatrSn(1); // 생성자 일련번호
            tblMenuAuthrtGroupUserRepository.save(authrtGroupUser);
        }

        if(!StringUtils.isEmpty(dto.get("snsType"))){
            if(dto.get("snsType").toString().equals("naver")){
                TblUserSnsCertInfo tblUserSnsCertInfo = new TblUserSnsCertInfo();
                tblUserSnsCertInfo.setUserSn(userSn);
                tblUserSnsCertInfo.setSnsClsf(dto.get("snsType").toString());
                tblUserSnsCertInfo.setSnsUnqNo(dto.get("snsId").toString());
                tblUserSnsCertInfo.setCreatrSn(userSn);
                tblUserSnsCertInfoRepository.save(tblUserSnsCertInfo);
            } else if (dto.get("snsType").toString().equals("kakao")) {
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

        //입주기업회원
        if (Integer.valueOf(1).equals(mbrType)) {
            TblMvnEntMbr mvnEntMbr = new TblMvnEntMbr();

            Object mvnEntSnObj = dto.get("mvnEntSn");
            Long mvnEntSn = (mvnEntSnObj instanceof Number) ? ((Number) mvnEntSnObj).longValue() : null;

            mvnEntMbr.setUserSn(userSn);
            mvnEntMbr.setMvnEntSn(mvnEntSn);


            tblMvnEntMbrRepository.save(mvnEntMbr);

            //컨설턴트회원
        } else if(Integer.valueOf(2).equals(mbrType)) {
            TblCnslttMbr tblCnslttMbr = new TblCnslttMbr();
            QTblComFile qTblComFile = QTblComFile.tblComFile;

            JPAQueryFactory q = new JPAQueryFactory(em);

            tblCnslttMbr.setUserSn(userSn);
            tblCnslttMbr.setCnsltActv((String) dto.get("cnsltActv"));
            tblCnslttMbr.setOgdpNm((String) dto.get("ogdpNm"));
            tblCnslttMbr.setJbpsNm((String) dto.get("jbpsNm"));
            tblCnslttMbr.setCrrPrd(Integer.parseInt(dto.get("crrPrd").toString()) );
            tblCnslttMbr.setCnsltFld(Long.parseLong((String) dto.get("cnsltFld")));
            tblCnslttMbr.setCnsltArtcl((String) dto.get("cnsltArtcl"));
            tblCnslttMbr.setCnsltSlfint((String) dto.get("cnsltSlfint"));
            tblCnslttMbr.setRmrkCn((String) dto.get("rmrkCn"));

            TblCnslttMbr fileCnsrttMbr =
            tblCnslttMbrRepository.save(tblCnslttMbr);

            if(certInfoList != null){
                List<TblQlfcLcns> certListToSave = new ArrayList<>();

                for (Map<String, Object> certInfo : certInfoList) {
                    TblQlfcLcns cert = new TblQlfcLcns();

                    cert.setUserSn(userSn);
                    cert.setQlfcLcnsNm((String) certInfo.get("qlfcLcnsNm"));
                    cert.setAcqsYmd((String) certInfo.get("acqsYmd"));
                    cert.setPblcnInstNm((String) certInfo.get("pblcnInstNm"));
                    cert.setActvtnYn("Y");

                    certListToSave.add(cert);
                }
                certListToSave = tblQlfcLcnsRepository.saveAll(certListToSave);

                List<Long> qlfcLcnsSnList = certListToSave.stream()
                        .map(TblQlfcLcns::getQlfcLcnsSn)
                        .collect(Collectors.toList());

                if (certFiles != null) {
                    for (int i = 0; i < certFiles.size(); i++) {
                        Long qlfcLcnsSn = qlfcLcnsSnList.get(i);

                        long fileCnt = q.selectFrom(qTblComFile)
                                .where(qTblComFile.psnTblSn.eq("cnsltCertificate_" + qlfcLcnsSn))
                                .fetchCount();

                        tblComFileRepository.saveAll(
                                fileUtil.devFileInf(
                                        Collections.singletonList(certFiles.get(i)),
                                        "/cnsltCertificate/" + qlfcLcnsSn,
                                        "cnsltCertificate_" + qlfcLcnsSn,
                                        fileCnt
                                )
                        );
                    }
                }
            }

            if(careerInfoList != null){
                List<TblCrr> careerListToSave = new ArrayList<>();

                for (Map<String, Object> careerInfo : careerInfoList) {

                    TblCrr crr = new TblCrr();

                    crr.setUserSn(userSn);
                    crr.setOgdpCoNm((String) careerInfo.get("ogdpCoNm"));
                    crr.setJbgdNm((String) careerInfo.get("ogdpJbpsNm"));
                    crr.setJncmpYmd((String) careerInfo.get("jncmpYmd"));
                    crr.setRsgntnYmd((String) careerInfo.get("rsgntnYmd"));
                    crr.setActvtnYn("Y");

                    careerListToSave.add(crr);
                }
                careerListToSave = tblCrrRepository.saveAll(careerListToSave);

                List<Long> crrSnList = careerListToSave.stream()
                        .map(TblCrr::getCrrSn)
                        .collect(Collectors.toList());

                if (careerFiles != null) {
                    for (int i = 0; i < careerFiles.size(); i++) {
                        Long crrSn = crrSnList.get(i);

                        long fileCnt = q.selectFrom(qTblComFile)
                                .where(qTblComFile.psnTblSn.eq("cnsltCareer_" + crrSn))
                                .fetchCount();

                        tblComFileRepository.saveAll(
                                fileUtil.devFileInf(
                                        Collections.singletonList(careerFiles.get(i)),
                                        "/cnsltCareer/" + crrSn,
                                        "cnsltCareer_" + crrSn,
                                        fileCnt
                                )
                        );
                    }
                }
            }

            if (acbgInfoList != null) {
                List<TblAcbg> acbgListToSave = new ArrayList<>();

                for (Map<String, Object> acbgInfo : acbgInfoList) {
                    TblAcbg acbg = new TblAcbg();

                    acbg.setUserSn(userSn);
                    acbg.setSchlNm((String) acbgInfo.get("schlNm"));
                    acbg.setScsbjtNm((String) acbgInfo.get("scsbjtNm"));
                    acbg.setMjrNm((String) acbgInfo.get("majrNm"));
                    acbg.setDgrNm((String) acbgInfo.get("dgrNm"));
                    acbg.setGrdtnYmd((String) acbgInfo.get("grdtnYmd"));
                    acbg.setActvtnYn("Y");

                    acbgListToSave.add(acbg);
                }

                acbgListToSave = tblAcbgRepository.saveAll(acbgListToSave);

                List<Long> acbgSnList = acbgListToSave.stream()
                        .map(TblAcbg::getAcbgSn)
                        .collect(Collectors.toList());

                if (acbgFiles != null) {
                    for (int i = 0; i < acbgFiles.size(); i++) {
                        Long acbgSn = acbgSnList.get(i);

                        long fileCnt = q.selectFrom(qTblComFile)
                                .where(qTblComFile.psnTblSn.eq("cnsltAcbg_" + acbgSn))
                                .fetchCount();

                        tblComFileRepository.saveAll(
                                fileUtil.devFileInf(
                                        Collections.singletonList(acbgFiles.get(i)),
                                        "/cnsltAcbg/" + acbgSn,
                                        "cnsltAcbg_" + acbgSn,
                                        fileCnt
                                )
                        );
                    }
                }
            }





            /*if(certFiles != null){
                long fileCnt = q.selectFrom(qTblComFile).where(qTblComFile.psnTblSn.eq("cnsltCertificate_"+fileCnsrttMbr.getUserSn())).fetchCount();
                tblComFileRepository.saveAll(
                        fileUtil.devFileInf(
                                certFiles,
                                "/cnsltCertificate/" +fileCnsrttMbr.getUserSn(),
                                "cnsltCertificate_" +fileCnsrttMbr.getUserSn(),
                                fileCnt
                                )
                );
            }*/

            if(cnsltProfileFiles != null){
                long fileCnt = q.selectFrom(qTblComFile).where(qTblComFile.psnTblSn.eq("cnsltProfile_"+fileCnsrttMbr.getUserSn())).fetchCount();
                tblComFileRepository.saveAll(
                        fileUtil.devFileInf(
                                cnsltProfileFiles,
                                "/cnsltProfile/" +fileCnsrttMbr.getUserSn(),
                                "cnsltProfile_" +fileCnsrttMbr.getUserSn(),
                                fileCnt
                        )
                );
            }

        }//컨설턴트회원 가입절차 끝
        //유관기관
        else if (Integer.valueOf(3).equals(mbrType)) {
            TblRelInstMbr relInstMbr = new TblRelInstMbr();

            Object relInstSnObj = dto.get("relInstSn");
            Long relInstSn = (relInstSnObj instanceof Number) ? ((Number) relInstSnObj).longValue() : null;

            relInstMbr.setUserSn(userSn);
            relInstMbr.setRelInstSn(relInstSn);


            tblRelInstMbrRepository.save(relInstMbr);

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

    public ResultVO setMemberMyPageModify(TblUser tblUser, TblCnslttMbr tblCnslttMbr,
                                          String hasCertData,
                                          String hasCrrData,
                                          String hasAcbgData,
                                          List<MultipartFile> certFiles,
                                          List<MultipartFile> careerFiles,
                                          List<MultipartFile> acbgFiles) {
        ResultVO resultVO = new ResultVO();
        QTblComFile qTblComFile = QTblComFile.tblComFile;

        JPAQueryFactory q = new JPAQueryFactory(em);

        try {
            if (tblUser.getMblTelno() != null && !tblUser.getMblTelno().isEmpty()) {
                String encryptedMblTelno = EgovFileScrty.encode(tblUser.getMblTelno());
                tblUser.setMblTelno(encryptedMblTelno);
            }

            if (tblUser.getUserPwdRe() != null && !tblUser.getUserPwdRe().isEmpty()) {
                String encryptedUserPw = EgovFileScrty.encryptPassword(tblUser.getUserPwdRe(), tblUser.getUserId());
                tblUser.setUserPw(encryptedUserPw);
            }

            TblUserRepository.save(tblUser);

            if (tblCnslttMbr != null) {
                tblCnslttMbrRepository.save(tblCnslttMbr);
            }

            Gson gson = new Gson();

            if (!StringUtils.isEmpty(hasCertData)) {
                List<TblQlfcLcns> certList = gson.fromJson(hasCertData, new TypeToken<List<TblQlfcLcns>>() {}.getType());

                int certFileIndex = 0;

                for (int i = 0; i < certList.size(); i++) {
                    TblQlfcLcns cert = certList.get(i);

                    if (cert.getQlfcLcnsSn() == null) {
                        tblQlfcLcnsRepository.save(cert);
                        Long qlfcLcnsSn = cert.getQlfcLcnsSn();

                        if (certFiles != null && certFileIndex < certFiles.size()) {
                            MultipartFile certFile = certFiles.get(certFileIndex);
                            long fileCnt = tblComFileRepository.count();
                            List<TblComFile> fileList = fileUtil.devFileInf(
                                    Collections.singletonList(certFile),
                                    "/cnsltCertificate/" + qlfcLcnsSn,
                                    "cnsltCertificate_" + qlfcLcnsSn,
                                    fileCnt
                            );

                            for (TblComFile file : fileList) {
                                tblComFileRepository.save(file);
                            }

                            certFileIndex++;
                        }
                    } else {
                        tblQlfcLcnsRepository.save(cert);
                    }
                }
            }

            if (!StringUtils.isEmpty(hasCrrData)) {
                List<TblCrr> crrList = gson.fromJson(hasCrrData, new TypeToken<List<TblCrr>>() {}.getType());

                int crrFileIndex = 0;

                for (int i = 0; i < crrList.size(); i++) {
                    TblCrr crr = crrList.get(i);

                    if (crr.getCrrSn() == null) {
                        tblCrrRepository.save(crr);
                        Long qCrrSn = crr.getCrrSn();

                        tblCrrRepository.save(crr);

                        if (careerFiles != null && crrFileIndex < careerFiles.size()) {
                            MultipartFile careerFile = careerFiles.get(crrFileIndex);
                            long fileCnt = tblComFileRepository.count();
                            List<TblComFile> fileList = fileUtil.devFileInf(
                                    Collections.singletonList(careerFile),
                                    "/cnsltCareer/" + qCrrSn,
                                    "cnsltCareer_" + qCrrSn,
                                    fileCnt
                            );

                            for (TblComFile file : fileList) {
                                tblComFileRepository.save(file);
                            }

                            crrFileIndex++;
                        }
                    } else {
                        tblCrrRepository.save(crr);
                    }
                }
            }

            if (!StringUtils.isEmpty(hasAcbgData)) {
                List<TblAcbg> acbgList = gson.fromJson(hasAcbgData, new TypeToken<List<TblAcbg>>() {}.getType());

                int acbgFileIndex = 0;

                for (int i = 0; i < acbgList.size(); i++) {
                    TblAcbg acbg = acbgList.get(i);

                    if (acbg.getAcbgSn() == null) {
                        tblAcbgRepository.save(acbg);
                        Long qAcbgSn = acbg.getAcbgSn();

                        tblAcbgRepository.save(acbg);

                        if (acbgFiles != null && acbgFileIndex < acbgFiles.size()) {
                            MultipartFile acbgFile = acbgFiles.get(acbgFileIndex);
                            long fileCnt = tblComFileRepository.count();
                            List<TblComFile> fileList = fileUtil.devFileInf(
                                    Collections.singletonList(acbgFile),
                                    "/cnsltAcbg/" + qAcbgSn,
                                    "cnsltAcbg_" + qAcbgSn,
                                    fileCnt
                            );

                            for (TblComFile file : fileList) {
                                tblComFileRepository.save(file);
                            }

                            acbgFileIndex++;
                        }
                    } else {
                        tblAcbgRepository.save(acbg);
                    }
                }
            }

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
            resultVO.setResultMessage("회원 수정이 완료되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
            resultVO.setResultMessage("회원 수정 중 오류가 발생했습니다.");
        }

        return resultVO;
    }





    public ResultVO checkUser(SearchDto dto) throws Exception {
        ResultVO resultVO = new ResultVO();

        QTblUser qTblUser = QTblUser.tblUser;
        JPAQueryFactory q = new JPAQueryFactory(em);

        // 사용자 ID와 비밀번호를 사용하여 암호화
        String encryptedPassword = EgovFileScrty.encryptPassword(dto.get("password").toString(), dto.get("id").toString());

        // 데이터베이스에서 사용자 조회
        TblUser tblUser = q.selectFrom(qTblUser)
                .where(
                        qTblUser.userId.eq(dto.get("id").toString())  // 사용자 ID 확인
                                .and(qTblUser.userPw.eq(encryptedPassword)) // 암호화된 비밀번호 비교
                )
                .fetchOne();

        if (tblUser != null) {
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
            resultVO.setResultMessage("회원 인증 성공");
        } else {
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
            resultVO.setResultMessage("회원 인증 실패");
        }

        return resultVO;
    }

    public ResultVO getMyPageNormalMember(TblUser tblUser) {
        ResultVO resultVO = new ResultVO();
        QTblComFile qTblComFile = QTblComFile.tblComFile;
        JPAQueryFactory q = new JPAQueryFactory(em);

        try {
            TblUser member = TblUserRepository.findByUserSn(tblUser.getUserSn());
            TblCnslttMbr cnslttMbr = tblCnslttMbrRepository.findByUserSn(tblUser.getUserSn());

            List<TblQlfcLcns> tblQlfcLcnsList = tblQlfcLcnsRepository.findAllByUserSn(tblUser.getUserSn());
            List<TblAcbg> tblAcbgList = tblAcbgRepository.findAllByUserSn(tblUser.getUserSn());
            List<TblCrr> tblCrrList = tblCrrRepository.findAllByUserSn(tblUser.getUserSn());

            List<Long> qlfcLcnsSnList = tblQlfcLcnsList.stream()
                    .map(TblQlfcLcns::getQlfcLcnsSn)
                    .collect(Collectors.toList());

            List<Long> qCareerSnList = tblCrrList.stream()
                    .map(TblCrr::getCrrSn)
                    .collect(Collectors.toList());

            List<Long> qAcbgSnList = tblAcbgList.stream()
                    .map(TblAcbg::getAcbgSn)
                    .collect(Collectors.toList());


            if (member != null) {
                resultVO.putResult("member", member);

                if (cnslttMbr != null) {

                    TblComFile cnsltProfileFile = q.selectFrom(qTblComFile).where(
                            qTblComFile.psnTblSn.eq(
                                    Expressions.stringTemplate("CONCAT('cnsltProfile_',{0})", cnslttMbr.getUserSn()) //사진
                            )
                    ).fetchOne();
                    List<TblComFile> cnsltCertificateFile = q.selectFrom(qTblComFile)
                            .where(qTblComFile.psnTblSn.in(
                                    qlfcLcnsSnList.stream()
                                            .map(sn -> "cnsltCertificate_" + sn)
                                            .collect(Collectors.toList())
                            ))
                            .fetch();

                    List<TblComFile> cnsltCareerFile = q.selectFrom(qTblComFile)
                            .where(qTblComFile.psnTblSn.in(
                                    qCareerSnList.stream()
                                            .map(sn -> "cnsltCareer_" + sn)
                                            .collect(Collectors.toList())
                            ))
                            .fetch();

                    List<TblComFile> cnsltAcbgFile = q.selectFrom(qTblComFile)
                            .where(qTblComFile.psnTblSn.in(
                                    qAcbgSnList.stream()
                                            .map(sn -> "cnsltAcbg_" + sn)
                                            .collect(Collectors.toList())
                            ))
                            .fetch();

                    resultVO.putResult("cnslttMbr", cnslttMbr);
                    resultVO.putResult("cnsltProfileFile", cnsltProfileFile);
                    resultVO.putResult("cnsltCertificateFile", cnsltCertificateFile);
                    resultVO.putResult("cnsltCareerFile", cnsltCareerFile);
                    resultVO.putResult("cnsltAcbgFile", cnsltAcbgFile);
                    resultVO.putResult("tblQlfcLcnsList", tblQlfcLcnsList);
                    resultVO.putResult("tblAcbgList", tblAcbgList);
                    resultVO.putResult("tblCrrList", tblCrrList);
                }

                long userSn = tblUser.getUserSn();

                TblRelInstMbr relInstMbr = tblRelInstMbrRepository.findByUserSn(userSn);
                TblMvnEntMbr mvnEntMbr = tblMvnEntMbrRepository.findByUserSn(userSn);

                Long mvnEntSn = null;
                Long relInstSn = null;

                if (mvnEntMbr != null) {
                    mvnEntSn = mvnEntMbr.getMvnEntSn();

                    if (mvnEntSn != null) {
                        TblMvnEnt mvnEnt = tblMvnEntRepository.findByMvnEntSn(mvnEntSn);
                        if (mvnEnt != null) {
                            String sysMngrYn = mvnEntMbr.getSysMngrYn();
                            mvnEnt.setSysMngrYn(sysMngrYn);

                            resultVO.putResult("rc", mvnEnt);
                        }
                    }
                } else if (relInstMbr != null) {
                    relInstSn = relInstMbr.getRelInstSn();

                    if (relInstSn != null) {
                        TblRelInst relInst = tblRelInstRepository.findByRelInstSn(relInstSn);
                        if (relInst != null) {
                            String sysMngrYn = relInstMbr.getSysMngrYn();
                            relInst.setSysMngrYn(sysMngrYn);

                            resultVO.putResult("rc", relInst);
                        }
                    }
                } else {
                }



                resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
            } else {
                resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }


    public ResultVO myPageCancelMember(TblUser tblUser) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblUser qTblUser = QTblUser.tblUser;

            long updatedCount = new JPAQueryFactory(em)
                    .update(qTblUser)
                    .set(qTblUser.mbrStts, "C")
                    .set(qTblUser.whdwlDt, LocalDateTime.now())
                    .where(qTblUser.userSn.eq(tblUser.getUserSn()))
                    .execute();

            if (updatedCount > 0) {
                resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
                resultVO.setResultMessage("회원탈퇴가 완료되었습니다.");
            } else {
                resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
                resultVO.setResultMessage("회원탈퇴가 실패하였습니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
            resultVO.setResultMessage("이용 정지 처리 중 오류가 발생했습니다.");
        }

        return resultVO;
    }

    public ResultVO getMyPageDfclMttrList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();

        try {
            if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
                paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
            }

            paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));

            QTblUser qTblUser = QTblUser.tblUser;
            QTblDfclMttr qTblDfclMttr = QTblDfclMttr.tblDfclMttr;
            QTblComCd qTblComCd = QTblComCd.tblComCd;
            QTblComFile qTblComFile = QTblComFile.tblComFile;

            JPAQueryFactory q = new JPAQueryFactory(em);

            /** query DSL 조건 추가하는 방법 */
            BooleanBuilder builder = new BooleanBuilder();

            if (dto.get("userSn") == null) {
                throw new IllegalArgumentException("userSn 값이 null입니다."); // 예외 처리
            }

            Long userSn = Long.parseLong(dto.get("userSn").toString());
            builder.and(qTblDfclMttr.userSn.eq(userSn));

            if (!StringUtils.isEmpty(dto.get("startDt"))) {
                builder.and(
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", qTblDfclMttr.frstCrtDt).goe(
                                Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", dto.get("startDt"))
                        )
                );
            }
            if (!StringUtils.isEmpty(dto.get("endDt"))) {
                builder.and(
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", qTblDfclMttr.frstCrtDt).loe(
                                Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", dto.get("endDt"))
                        )
                );
            }

            if (!StringUtils.isEmpty(dto.get("answerYn"))) {
                if(dto.get("answerYn").equals("Y")){
                    builder.and(qTblDfclMttr.ansCn.isNotNull());
                }else if(dto.get("answerYn").equals("N")){
                    builder.and(qTblDfclMttr.ansCn.isNull());
                }
            }
            /*
            if (!StringUtils.isEmpty(dto.get("dfclMttrFld"))) {
                builder.and(qTblDfclMttr.dfclMttrFld.eq(Long.parseLong(dto.get("dfclMttrFld").toString())));
            }*/

            if (!StringUtils.isEmpty(dto.get("searchType"))) {
                if(dto.get("searchType").equals("ttl")){
                    builder.and(qTblDfclMttr.ttl.contains((String) dto.get("searchVal")));
                }else if(dto.get("searchType").equals("dfclMttrCn")){
                    builder.and(qTblDfclMttr.dfclMttrCn.contains((String) dto.get("searchVal")));
                }
            }else{
                builder.and(
                        qTblDfclMttr.ttl.contains((String) dto.get("searchVal"))
                                .or(qTblDfclMttr.dfclMttrCn.contains((String) dto.get("searchVal")))
                );
            }

            JPQLQuery<Long> fileCnt = JPAExpressions
                    .select(qTblComFile.count())
                    .from(qTblComFile)
                    .innerJoin(qTblDfclMttr)
                    .on(qTblComFile.psnTblSn.eq(Expressions.stringTemplate("CONCAT('dfclMttr_', {0})", qTblDfclMttr.dfclMttrSn)))
                    .where(qTblDfclMttr.dfclMttrSn.eq(qTblDfclMttr.dfclMttrSn));

            List<DfclMttrDto> diffList = q
                    .select(
                            Projections.constructor(
                                    DfclMttrDto.class,
                                    qTblDfclMttr.dfclMttrSn,
                                    qTblDfclMttr.userSn,
                                    qTblDfclMttr.dfclMttrFld,
                                    qTblComCd.comCdNm,
                                    qTblDfclMttr.ttl,
                                    qTblUser.kornFlnm,
                                    qTblDfclMttr.frstCrtDt,
                                    fileCnt,
                                    new CaseBuilder()
                                            .when(qTblDfclMttr.ansCn.isNotNull().and(qTblDfclMttr.ansCn.ne("")))
                                            .then("Y")
                                            .otherwise("N")
                            )
                    ).from(qTblDfclMttr)
                    .join(qTblUser).on(qTblDfclMttr.userSn.eq(qTblUser.userSn))
                    .join(qTblComCd).on(qTblDfclMttr.dfclMttrFld.eq(qTblComCd.comCdSn))
                    .where(builder)
                    .orderBy(qTblUser.frstCrtDt.desc())
                    .offset(paginationInfo.getFirstRecordIndex())
                    .limit(paginationInfo.getRecordCountPerPage())
                    .fetch();


            Long totCnt = q.select(qTblDfclMttr.count())
                    .from(qTblDfclMttr)
                    .join(qTblUser).on(qTblDfclMttr.userSn.eq(qTblUser.userSn))
                    .join(qTblComCd).on(qTblDfclMttr.dfclMttrFld.eq(qTblComCd.comCdSn))
                    .where(builder)
                    .fetchOne();
            if(totCnt == null) totCnt = 0L;
            paginationInfo.setTotalRecordCount(totCnt.intValue());

            resultVO.putResult("diffList", diffList);
            resultVO.putPaginationInfo(paginationInfo);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }
        return resultVO;
    }

    public ResultVO getDifficultiesDetail(TblDfclMttr tblDfclMttr) {
        ResultVO resultVO = new ResultVO();

        try{
            QTblComCd qTblComCd = QTblComCd.tblComCd;
            QTblComFile qTblComFile = QTblComFile.tblComFile;

            JPAQueryFactory q = new JPAQueryFactory(em);

            tblDfclMttr = tblDfclMttrRepository.findByDfclMttrSn(tblDfclMttr.getDfclMttrSn());
            tblDfclMttr.setDfclMttrFldNm(q.select(qTblComCd.comCdNm).from(qTblComCd).where(qTblComCd.comCdSn.eq(tblDfclMttr.getDfclMttrFld())).fetchOne());
            tblDfclMttr.setDiffFiles(q.selectFrom(qTblComFile).where(qTblComFile.psnTblSn.eq("dfclMttr_" + tblDfclMttr.getDfclMttrSn())).fetch());
            tblDfclMttr.setAnswerFiles(q.selectFrom(qTblComFile).where(qTblComFile.psnTblSn.eq("dfclMttrAnswer_" + tblDfclMttr.getDfclMttrSn())).fetch());

            resultVO.putResult("difficulties", tblDfclMttr);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setDifficultiesData(TblDfclMttr tblDfclMttr, List<MultipartFile> files) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblComFile qTblComFile = QTblComFile.tblComFile;
            JPAQueryFactory q = new JPAQueryFactory(em);

            if(tblDfclMttr.getAnsRegDt() == null) {
                tblDfclMttr.setAnsRegDt(LocalDateTime.now());
            }
            tblDfclMttrRepository.save(tblDfclMttr);

            if(files != null){
                long fileCnt = q.selectFrom(qTblComFile).where(qTblComFile.psnTblSn.eq("dfclMttrAnswer_" + tblDfclMttr.getDfclMttrSn())).fetchCount();
                tblComFileRepository.saveAll(
                        fileUtil.devFileInf(
                                files,
                                "/dfclMttr/" + tblDfclMttr.getDfclMttrSn(),
                                "dfclMttr_" + tblDfclMttr.getDfclMttrSn(),
                                fileCnt
                        )
                );
            }

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }

        return resultVO;
    }


    public ResultVO getMyPageSimpleList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();

        try {
            if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
                paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
            }

            paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));

            QTblUser qTblUser = QTblUser.tblUser;
            QTblCnslttMbr qTblCnslttMbr = QTblCnslttMbr.tblCnslttMbr;
            QTblCnsltAply qTblCnsltAply = QTblCnsltAply.tblCnsltAply;
            QTblCnsltDtl qTblCnsltDtl = QTblCnsltDtl.tblCnsltDtl;
            QTblCnsltDsctn qTblCnsltDsctn = QTblCnsltDsctn.tblCnsltDsctn;
            QTblCnsltDgstfn qTblCnsltDgstfn = QTblCnsltDgstfn.tblCnsltDgstfn;

            QTblComCd qTblComCd = QTblComCd.tblComCd;
            QTblComFile qTblComFile = QTblComFile.tblComFile;

            JPAQueryFactory q = new JPAQueryFactory(em);

            /** query DSL 조건 추가하는 방법 */
            BooleanBuilder builder = new BooleanBuilder();


            Long userSn = Long.parseLong(dto.get("userSn").toString());
            builder.and(
                    qTblCnsltAply.userSn.eq(userSn)
                            .or(qTblCnsltDtl.cnslttUserSn.eq(userSn))
            );

            builder.and(qTblCnsltAply.cnsltSe
                    .eq(Long.valueOf(dto.get("cnsltSe").toString())));

            JPQLQuery<Long> fileCnt = JPAExpressions
                    .select(qTblComFile.count())
                    .from(qTblComFile)
                    .innerJoin(qTblCnsltDsctn)
                    .on(qTblComFile.psnTblSn.eq(Expressions.stringTemplate("CONCAT('consulting_', {0})", qTblCnsltDsctn.cnsltDsctnSn)))
                    .where(qTblCnsltDsctn.cnsltAplySn.eq(qTblCnsltAply.cnsltAplySn));
            if (!StringUtils.isEmpty(dto.get("searchType"))) {
                if(dto.get("searchType").equals("ttl")){
                    builder.and(qTblCnsltAply.ttl.contains((String) dto.get("searchVal")));
                }else if(dto.get("searchType").equals("cn")){
                    builder.and(qTblCnsltAply.cn.contains((String) dto.get("searchVal")));
                }
            }else{
                builder.and(
                        qTblCnsltAply.ttl.contains((String) dto.get("searchVal"))
                                .or(qTblCnsltAply.cn.contains((String) dto.get("searchVal")))
                );
            }

            List<SimpleDTO> consultantList = q.
                    select(
                            Projections.constructor(
                                    SimpleDTO.class,
                                    qTblCnsltAply.cnsltAplySn,
                                    qTblCnsltAply.userSn,
                                    qTblCnsltDtl.cnslttUserSn,
                                    qTblUser.kornFlnm,
                                     JPAExpressions
                                            .select(qTblUser.kornFlnm)
                                            .from(qTblUser)
                                            .where(qTblUser.userSn.eq(qTblCnsltDtl.cnslttUserSn)),
                                    qTblCnsltAply.frstCrtDt,
                                    qTblCnsltAply.cnsltFld,
                                    JPAExpressions
                                            .select(qTblComCd.comCdNm)
                                            .from(qTblComCd)
                                            .where(qTblComCd.comCdSn.eq(qTblCnsltAply.cnsltFld)),
                                    qTblCnslttMbr.ogdpNm,
                                    qTblCnsltDtl.cnsltSttsCd,
                                    JPAExpressions
                                            .select(
                                                    qTblCnsltDgstfn.dgstfnArtcl.count().coalesce(0L)
                                            )
                                            .from(qTblCnsltDgstfn)
                                            .where(qTblCnsltDtl.cnsltAplySn.eq(qTblCnsltDgstfn.cnsltAplySn)),
                                    qTblCnsltAply.ttl,
                                    fileCnt
                            )
                    ).from(qTblCnsltAply)

                    //조인
                    .join(qTblCnsltDtl)
                    .on(
                            qTblCnsltAply.cnsltAplySn.eq(qTblCnsltDtl.cnsltAplySn)
                    )
                    .join(qTblUser)
                    .on(
                            qTblCnsltAply.userSn.eq(qTblUser.userSn)
                    )
                    .join(qTblCnslttMbr)
                    .on(
                            qTblCnsltDtl.cnslttUserSn.eq(qTblCnslttMbr.userSn)
                    )
                    /*.join(qTblCnsltDgstfn)
                    .on(
                              qTblCnsltAply.cnsltAplySn.eq(qTblCnsltDgstfn.cnsltAplySn)
                    )*/
                    .where(builder)
                    .orderBy(qTblCnsltAply.frstCrtDt.desc())
                    .offset(paginationInfo.getFirstRecordIndex())
                    .limit(paginationInfo.getRecordCountPerPage())
                    .fetch();

              Long totCnt = q
                    .select(qTblCnsltAply.count())
                    .from(qTblCnsltAply)
                      .join(qTblCnsltDtl).on(qTblCnsltAply.cnsltAplySn.eq(qTblCnsltDtl.cnsltAplySn))
                    .join(qTblUser).on(qTblCnsltAply.userSn.eq(qTblUser.userSn))
                    .join(qTblCnslttMbr).on(qTblCnsltDtl.cnslttUserSn.eq(qTblCnslttMbr.userSn))
                    .where(builder)
                    .fetchOne();


                if(totCnt == null) totCnt = 0L;
                paginationInfo.setTotalRecordCount(totCnt.intValue());

                resultVO.putResult("consultantList", consultantList);
                resultVO.putPaginationInfo(paginationInfo);
                resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }
        return resultVO;
    }

    public ResultVO getSimpleDetail(SearchDto dto) {
        ResultVO resultVO = new ResultVO();

        try{
            QTblComCd qTblComCd = QTblComCd.tblComCd;
            QTblComFile qTblComFile = QTblComFile.tblComFile;
            QTblCnsltDsctn qTblCnsltDsctn = QTblCnsltDsctn.tblCnsltDsctn;

            JPAQueryFactory q = new JPAQueryFactory(em);

            TblCnslttMbr consulttDtl = tblCnslttMbrRepository.findByUserSn(Long.parseLong(dto.get("cnslttUserSn").toString()));
            TblCnsltAply tblCnsltAply = tblCnsltAplyRepository.findByCnsltAplySn(Long.parseLong(dto.get("cnsltAplySn").toString()));

            TblComFile cnsltProfileFile = q.selectFrom(qTblComFile).where(
                    qTblComFile.psnTblSn.eq(
                            Expressions.stringTemplate("CONCAT('cnsltProfile_',{0})", consulttDtl.getUserSn()) //사진
                    )
            ).fetchOne();
            List<TblComFile> cnsltCertificateFile = q.selectFrom(qTblComFile).where(
                    qTblComFile.psnTblSn.eq(
                            Expressions.stringTemplate("CONCAT('cnsltCertificate_',{0})", consulttDtl.getUserSn()) //자격증
                    )
            ).fetch();

            TblUser tblUser = q.selectFrom(QTblUser.tblUser)
                    .where(QTblUser.tblUser.userSn.eq(consulttDtl.getUserSn()))
                    .fetchOne();

            resultVO.putResult("consulttUserName", tblUser);
            resultVO.putResult("consulttUser", consulttDtl);
            resultVO.putResult("cnsltProfileFile",cnsltProfileFile);
            resultVO.putResult("cnsltCertificateFile",cnsltCertificateFile);

            tblCnsltAply.setCnsltAplyFldNm(q.select(qTblComCd.comCdNm).from(qTblComCd).where(qTblComCd.comCdSn.eq(tblCnsltAply.getCnsltFld())).fetchOne());

            List<TblCnsltDsctn> tblCnsltDsctnList = q
                    .selectFrom(qTblCnsltDsctn)
                    .where(qTblCnsltDsctn.cnsltAplySn.eq(Long.parseLong(tblCnsltAply.getCnsltAplySn().toString())))
                    .orderBy(qTblCnsltDsctn.frstCrtDt.asc()).fetch();

            List<Long> dsctnSnList = tblCnsltDsctnList.stream()
                    .map(TblCnsltDsctn::getCnsltDsctnSn)
                    .collect(Collectors.toList());

            List<TblComFile> allFiles = q
                    .selectFrom(qTblComFile)
                    .where(qTblComFile.psnTblSn.in(
                            dsctnSnList.stream()
                                    .map(dsctnSn -> "consulting_" + dsctnSn)
                                    .collect(Collectors.toList())
                    ))
                    .orderBy(qTblComFile.atchFileSn.desc())
                    .fetch();

            Map<Long, List<TblComFile>> filesByDsctnSn = allFiles.stream()
                    .collect(Collectors.groupingBy(file -> {
                        String psnTblSn = file.getPsnTblSn();
                        return Long.parseLong(psnTblSn.replace("consulting_", ""));
                    }));

            resultVO.putResult("simple", tblCnsltAply);
            resultVO.putResult("cnsltDsctnList", tblCnsltDsctnList);
            resultVO.putResult("filesByDsctnSn",filesByDsctnSn);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }


    public ResultVO setSimpleData(TblCnsltDsctn tblCnsltDsctn, List<MultipartFile> files) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblComFile qTblComFile = QTblComFile.tblComFile;
            JPAQueryFactory q = new JPAQueryFactory(em);

            tblCnsltDsctnRepository.save(tblCnsltDsctn);

            if(files != null){
                long fileCnt = q.selectFrom(qTblComFile).where(qTblComFile.psnTblSn.eq("consulting" + tblCnsltDsctn.getCnsltDsctnSn())).fetchCount();
                tblComFileRepository.saveAll(
                        fileUtil.devFileInf(
                                files,
                                "/consulting/" + tblCnsltDsctn.getCnsltDsctnSn(),
                                "consulting_" + tblCnsltDsctn.getCnsltDsctnSn(),
                                fileCnt
                        )
                );
            }

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setCreateSimpleData(TblCnsltDsctn tblCnsltDsctn, List<MultipartFile> files, String cnsltSttsCd) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblComFile qTblComFile = QTblComFile.tblComFile;
            QTblCnsltDtl qTblCnsltDtl = QTblCnsltDtl.tblCnsltDtl;
            JPAQueryFactory q = new JPAQueryFactory(em);

            tblCnsltDsctnRepository.save(tblCnsltDsctn);


            if (tblCnsltDsctn.getCnsltAplySn() > 0L) {
                q.update(qTblCnsltDtl)
                        .set(qTblCnsltDtl.cnsltSttsCd, cnsltSttsCd)
                        .where(qTblCnsltDtl.cnsltAplySn.eq(tblCnsltDsctn.getCnsltAplySn()))
                        .execute();
            }

            if(files != null){
                long fileCnt = q.selectFrom(qTblComFile).where(qTblComFile.psnTblSn.eq("consulting" + tblCnsltDsctn.getCnsltDsctnSn())).fetchCount();
                tblComFileRepository.saveAll(
                        fileUtil.devFileInf(
                                files,
                                "/consulting/" + tblCnsltDsctn.getCnsltDsctnSn(),
                                "consulting_" + tblCnsltDsctn.getCnsltDsctnSn(),
                                fileCnt
                        )
                );
            }

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setComSimple(TblCnsltDtl tblCnsltDtl) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblCnsltDtl qTblCnsltDtl = QTblCnsltDtl.tblCnsltDtl;

            long updatedCount = new JPAQueryFactory(em)
                    .update(qTblCnsltDtl)
                    .set(qTblCnsltDtl.cnsltSttsCd, "201")
                    .where(qTblCnsltDtl.cnsltAplySn.eq(tblCnsltDtl.getCnsltAplySn()))
                    .execute();

            if (updatedCount > 0) {
                resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
                resultVO.setResultMessage("처리되었습니다.");
            } else {
                resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
                resultVO.setResultMessage("실패하였습니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
            resultVO.setResultMessage("오류가 발생했습니다.");
        }

        return resultVO;
    }

    public ResultVO setCancelSimple(TblCnsltDtl tblCnsltDtl) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblCnsltDtl qTblCnsltDtl = QTblCnsltDtl.tblCnsltDtl;

            long updatedCount = new JPAQueryFactory(em)
                    .update(qTblCnsltDtl)
                    .set(qTblCnsltDtl.cnsltSttsCd, "999")
                    .where(qTblCnsltDtl.cnsltAplySn.eq(tblCnsltDtl.getCnsltAplySn()))
                    .execute();

            if (updatedCount > 0) {
                resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
                resultVO.setResultMessage("처리되었습니다.");
            } else {
                resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
                resultVO.setResultMessage("실패하였습니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
            resultVO.setResultMessage("오류가 발생했습니다.");
        }

        return resultVO;
    }

    public ResultVO setAcceptCnslt(TblCnsltDtl tblCnsltDtl) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblCnsltDtl qTblCnsltDtl = QTblCnsltDtl.tblCnsltDtl;

            long updatedCount = new JPAQueryFactory(em)
                    .update(qTblCnsltDtl)
                    .set(qTblCnsltDtl.cnsltSttsCd, "101")
                    .where(qTblCnsltDtl.cnsltAplySn.eq(tblCnsltDtl.getCnsltAplySn()))
                    .execute();

            if (updatedCount > 0) {
                resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
                resultVO.setResultMessage("처리되었습니다.");
            } else {
                resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
                resultVO.setResultMessage("실패하였습니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
            resultVO.setResultMessage("오류가 발생했습니다.");
        }

        return resultVO;
    }

    public ResultVO setCancleCnsltRequest(TblCnsltDtl tblCnsltDtl) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblCnsltDtl qTblCnsltDtl = QTblCnsltDtl.tblCnsltDtl;

            long updatedCount = new JPAQueryFactory(em)
                    .update(qTblCnsltDtl)
                    .set(qTblCnsltDtl.cnsltSttsCd, "12")
                    .set(qTblCnsltDtl.cnslttUserSn, (Long) null)
                    .set(qTblCnsltDtl.cnslttDsgnDt, (LocalDateTime) null)
                    .where(qTblCnsltDtl.cnsltAplySn.eq(tblCnsltDtl.getCnsltAplySn()))
                    .execute();

            if (updatedCount > 0) {
                resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
                resultVO.setResultMessage("처리되었습니다.");
            } else {
                resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
                resultVO.setResultMessage("실패하였습니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
            resultVO.setResultMessage("오류가 발생했습니다.");
        }

        return resultVO;
    }


    public ResultVO setSatisSimpleData(TblCnsltDgstfn tblCnsltDgstfn) {
        ResultVO resultVO = new ResultVO();

        try {
            JPAQueryFactory q = new JPAQueryFactory(em);
            tblCnsltDgstfnRepository.save(tblCnsltDgstfn);

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getSatisPopup(TblCnsltDgstfn tblCnsltDgstfn) {
        ResultVO resultVO = new ResultVO();

        try {
            List<TblCnsltDgstfn> ratingsList = tblCnsltDgstfnRepository.findByCnsltAplySn(tblCnsltDgstfn.getCnsltAplySn());
            if (ratingsList.isEmpty()) {
                resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
            } else {
                resultVO.putResult("ratings", ratingsList);
                resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO checkPassword(SearchDto dto) throws Exception {
        ResultVO resultVO = new ResultVO();

        QTblUser qTblUser = QTblUser.tblUser;
        JPAQueryFactory q = new JPAQueryFactory(em);
        String encryptedPassword = EgovFileScrty.encryptPassword(dto.get("userPw").toString(), dto.get("userId").toString());

        TblUser tblUser = q.selectFrom(qTblUser)
                .where(
                        qTblUser.userId.eq(dto.get("userId").toString())
                                .and(qTblUser.userPw.eq(encryptedPassword))
                )
                .fetchOne();

        if (tblUser != null) {
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } else {
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO delCertificate(TblQlfcLcns tblQlfcLcns) {
        ResultVO resultVO = new ResultVO();

        try {
            TblQlfcLcns certificateToDelete = tblQlfcLcnsRepository.findByQlfcLcnsSn(tblQlfcLcns.getQlfcLcnsSn());

            if (certificateToDelete != null) {
                tblQlfcLcnsRepository.delete(certificateToDelete);
                resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
            } else {
                resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO delCareer(TblCrr tblCrr) {
        ResultVO resultVO = new ResultVO();

        try {
            TblCrr crrToDelete = tblCrrRepository.findByCrrSn(tblCrr.getCrrSn());

            if (crrToDelete != null) {
                tblCrrRepository.delete(crrToDelete);
                resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
            } else {
                resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO delAcbg(TblAcbg tblAcbg) {
        ResultVO resultVO = new ResultVO();

        try {
            TblAcbg acbgToDelete = tblAcbgRepository.findByAcbgSn(tblAcbg.getAcbgSn());

            if (acbgToDelete != null) {
                tblAcbgRepository.delete(acbgToDelete);
                resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
            } else {
                resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getCompanyMemberList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();
        JPAQueryFactory q = new JPAQueryFactory(em);

        try {
            QTblMvnEntMbr qTblMvnEntMbr = QTblMvnEntMbr.tblMvnEntMbr;
            QTblUser qTblUser = QTblUser.tblUser;
            QTblRelInstMbr qTblRelInstMbr = QTblRelInstMbr.tblRelInstMbr;
            BooleanBuilder builder = new BooleanBuilder();

            // 페이지 설정
            if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
                paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
            }
            paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));

            Long mvnEntSn = dto.get("mvnEntSn") != null ? ((Number) dto.get("mvnEntSn")).longValue() : null;
            Long relInstSn = dto.get("relInstSn") != null ? ((Number) dto.get("relInstSn")).longValue() : null;

            if (mvnEntSn == null && relInstSn == null) {
                resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
                return resultVO;
            }

            List<Long> userSnList = new ArrayList<>();

            if (mvnEntSn != null) {
                userSnList = q.select(qTblMvnEntMbr.userSn)
                        .from(qTblMvnEntMbr)
                        .where(qTblMvnEntMbr.mvnEntSn.eq(mvnEntSn))
                        .fetch();
            }

            if (userSnList.isEmpty() && relInstSn != null) {
                userSnList = q.select(qTblRelInstMbr.userSn)
                        .from(qTblRelInstMbr)
                        .where(qTblRelInstMbr.relInstSn.eq(relInstSn))
                        .fetch();
            }

            if (userSnList.isEmpty()) {
                resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
                return resultVO;
            }


            if (!StringUtils.isEmpty(dto.get("searchType"))) {
                String searchVal = (String) dto.get("searchVal");
                if ("kornFlnm".equals(dto.get("searchType"))) {
                    builder.and(qTblUser.kornFlnm.contains(searchVal));
                } else if ("userId".equals(dto.get("searchType"))) {
                    builder.and(qTblUser.userId.contains(searchVal));
                }
            } else if (!StringUtils.isEmpty(dto.get("searchVal"))) {
                String searchVal = (String) dto.get("searchVal");
                builder.and(qTblUser.kornFlnm.contains(searchVal)
                        .or(qTblUser.userId.contains(searchVal)));
            }

            List<TblUser> userList = q.select(qTblUser)
                    .from(qTblUser)
                    .where(qTblUser.userSn.in(userSnList).and(builder))
                    .offset(paginationInfo.getFirstRecordIndex())
                    .limit(paginationInfo.getRecordCountPerPage())
                    .fetch();

            Map<Long, String> aprvYnMap = q.select(
                            qTblMvnEntMbr.userSn,
                            qTblMvnEntMbr.aprvYn.coalesce(qTblRelInstMbr.aprvYn)
                    )
                    .from(qTblMvnEntMbr)
                    .leftJoin(qTblRelInstMbr).on(qTblMvnEntMbr.userSn.eq(qTblRelInstMbr.userSn))
                    .where(qTblMvnEntMbr.userSn.in(userSnList))
                    .transform(GroupBy.groupBy(qTblMvnEntMbr.userSn).as(qTblMvnEntMbr.aprvYn.coalesce(qTblRelInstMbr.aprvYn)));

            for (TblUser user : userList) {
                user.setAprvYn(aprvYnMap.getOrDefault(user.getUserSn(), "N"));
            }

            Long totCnt = q.select(qTblUser.count())
                    .from(qTblUser)
                    .where(qTblUser.userSn.in(userSnList).and(builder))
                    .fetchOne();

            if (totCnt == null) totCnt = 0L;
            paginationInfo.setTotalRecordCount(totCnt.intValue());

            resultVO.putResult("getCompanyMemberList", userList);
            resultVO.putPaginationInfo(paginationInfo);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());

        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }


    public ResultVO setCompanyMember(TblUser tblUser) {
        ResultVO resultVO = new ResultVO();
        JPAQueryFactory q = new JPAQueryFactory(em);

        try {
            Long userSn = tblUser.getUserSn();

            if (userSn == null) {
                resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
                resultVO.setResultMessage("userSn이 없습니다.");
                return resultVO;
            }

            long updatedMvnEntMbr = q.update(QTblMvnEntMbr.tblMvnEntMbr)
                    .set(QTblMvnEntMbr.tblMvnEntMbr.aprvYn, "Y")
                    .where(QTblMvnEntMbr.tblMvnEntMbr.userSn.eq(userSn))
                    .execute();

            long updatedRelInstMbr = q.update(QTblRelInstMbr.tblRelInstMbr)
                    .set(QTblRelInstMbr.tblRelInstMbr.aprvYn, "Y")
                    .where(QTblRelInstMbr.tblRelInstMbr.userSn.eq(userSn))
                    .execute();

            if (updatedMvnEntMbr > 0 || updatedRelInstMbr > 0) {
                resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
                resultVO.setResultMessage("승인 처리 완료");
            } else {
                resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
                resultVO.setResultMessage("해당 사용자를 찾을 수 없습니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
            resultVO.setResultMessage("승인 처리 중 오류 발생");
        }

        return resultVO;
    }

    public ResultVO setCompanyMemberDel(TblUser tblUser) {
        ResultVO resultVO = new ResultVO();
        JPAQueryFactory q = new JPAQueryFactory(em);

        try {
            Long userSn = tblUser.getUserSn();

            if (userSn == null) {
                resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
                resultVO.setResultMessage("userSn이 없습니다.");
                return resultVO;
            }

            long updatedMvnEntMbr = q.update(QTblMvnEntMbr.tblMvnEntMbr)
                    .set(QTblMvnEntMbr.tblMvnEntMbr.aprvYn, "N")
                    .where(QTblMvnEntMbr.tblMvnEntMbr.userSn.eq(userSn))
                    .execute();

            long updatedRelInstMbr = q.update(QTblRelInstMbr.tblRelInstMbr)
                    .set(QTblRelInstMbr.tblRelInstMbr.aprvYn, "N")
                    .where(QTblRelInstMbr.tblRelInstMbr.userSn.eq(userSn))
                    .execute();

            if (updatedMvnEntMbr > 0 || updatedRelInstMbr > 0) {
                resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
                resultVO.setResultMessage("취소 처리 완료");
            } else {
                resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
                resultVO.setResultMessage("해당 사용자를 찾을 수 없습니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
            resultVO.setResultMessage("승인 처리 중 오류 발생");
        }

        return resultVO;
    }

    public ResultVO setDiffDel(TblDfclMttr tblDfclMttr) {
        ResultVO resultVO = new ResultVO();

        try {
            TblDfclMttr diffDel = tblDfclMttrRepository.findByDfclMttrSn(tblDfclMttr.getDfclMttrSn());

            if (diffDel != null) {
                tblDfclMttrRepository.delete(diffDel);
                resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
            } else {
                resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }

        return resultVO;
    }


}