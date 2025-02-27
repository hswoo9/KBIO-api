package egovframework.com.devjitsu.service.member;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.bbs.QTblPst;
import egovframework.com.devjitsu.model.bbs.TblBbs;
import egovframework.com.devjitsu.model.common.QTblComFile;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.user.*;
import egovframework.com.devjitsu.repository.user.TblUserLgnHstryRepository;
import egovframework.com.devjitsu.repository.user.TblUserRepository;
import egovframework.let.utl.sim.service.EgovFileScrty;
import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberAdminApiService {

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    private final EntityManager em;

    private final TblUserRepository tblUserRepository;
    private final TblUserLgnHstryRepository tblUserLgnHstryRepository;

    public ResultVO getNormalMemberList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();

        try {
            if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
                paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
            }

            if (!StringUtils.isEmpty(dto.get("pageUnit"))) {
                paginationInfo.setRecordCountPerPage(Integer.parseInt(dto.get("pageUnit").toString()));
            }else{
                paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            }

            paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));

            JPAQueryFactory q = new JPAQueryFactory(em);
            QTblUser qTblUser = QTblUser.tblUser;

            BooleanBuilder builder = new BooleanBuilder();
            builder.and(qTblUser.actvtnYn.eq("Y"));

            if (!StringUtils.isEmpty(dto.get("mbrType"))) {
                builder.and(qTblUser.mbrType.eq(Long.valueOf((String) dto.get("mbrType"))));
            }
            if (!StringUtils.isEmpty(dto.get("mbrStts"))) {
                builder.and(qTblUser.mbrStts.eq((String) dto.get("mbrStts")));
            }
            if (!StringUtils.isEmpty(dto.get("searchType"))) {
                if(dto.get("searchType").equals("userId")){
                    builder.and(qTblUser.userId.contains((String) dto.get("searchVal")));
                }else if(dto.get("searchType").equals("kornFlnm")){
                    builder.and(qTblUser.kornFlnm.contains((String) dto.get("searchVal")));
                }
            }else{
                if (!StringUtils.isEmpty(dto.get("searchVal"))) {
                    builder.and(
                            qTblUser.userId.contains((String) dto.get("searchVal"))
                                    .or(qTblUser.kornFlnm.contains((String) dto.get("searchVal")))
                    );
                }
            }

            List<TblUser> getNormalMemberList = q.selectFrom(qTblUser)
                    .where(builder)
                    .orderBy(qTblUser.userSn.desc())
                    .offset(paginationInfo.getFirstRecordIndex())
                    .limit(paginationInfo.getRecordCountPerPage())
                    .fetch();

            Long totCnt = q.select(qTblUser.count())
                    .from(qTblUser)
                    .where(builder)
                    .fetchOne();
            if (totCnt == null) totCnt = 0L;
            paginationInfo.setTotalRecordCount(totCnt.intValue());

            resultVO.putResult("getNormalMemberList", getNormalMemberList);
            resultVO.putPaginationInfo(paginationInfo);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getNormalMember(TblUser tblUser) {
        ResultVO resultVO = new ResultVO();

        try {
            TblUser member = tblUserRepository.findByUserSn(tblUser.getUserSn());


            TblUserLgnHstry latestLogin = tblUserLgnHstryRepository.findLatestLoginByUserSn(tblUser.getUserSn());

            if (latestLogin != null) {
                member.setLastLoginDate(latestLogin.getLgnDt());
            }

            resultVO.putResult("member", member);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setNormalMember(TblUser tblUser) {
        ResultVO resultVO = new ResultVO();

        try {
            tblUserRepository.save(tblUser);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setNormalMemberDel(TblUser tblUser) {
        ResultVO resultVO = new ResultVO();

        try {
            tblUserRepository.save(tblUser);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setApprovalMemberDel(TblUser tblUser) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblUser qTblUser = QTblUser.tblUser;

            long updatedCount = new JPAQueryFactory(em)
                    .update(qTblUser)
                    .set(qTblUser.mbrStts, "N")
                    .where(qTblUser.userSn.eq(tblUser.getUserSn()))
                    .execute();

            if (updatedCount > 0) {
                resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
                resultVO.setResultMessage("이용이 정지되었습니다.");
            } else {
                resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
                resultVO.setResultMessage("해당 회원이 존재하지 않습니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
            resultVO.setResultMessage("이용 정지 처리 중 오류가 발생했습니다.");
        }

        return resultVO;
    }

    public ResultVO resetMemberPassword(TblUser tblUser) {
        ResultVO resultVO = new ResultVO();

        try {
            tblUserRepository.save(tblUser);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getApprovalMemberList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();

        try {
            if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
                paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
            }
            paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));

            JPAQueryFactory q = new JPAQueryFactory(em);
            QTblUser qTblUser = QTblUser.tblUser;

            BooleanBuilder builder = new BooleanBuilder();
            builder.and(qTblUser.actvtnYn.eq("Y").and(qTblUser.mbrStts.eq("Y")));

            if (!StringUtils.isEmpty(dto.get("userSn"))) {
                builder.and(qTblUser.userSn.eq(Long.valueOf((String) dto.get("userSn"))));
            }
            if (!StringUtils.isEmpty(dto.get("mbrType"))) {
                builder.and(qTblUser.mbrType.eq(Long.valueOf((String) dto.get("mbrType"))));
            }
            if (!StringUtils.isEmpty(dto.get("searchType"))) {
                if (dto.get("searchType").equals("userId")) {
                    builder.and(qTblUser.userId.contains((String) dto.get("searchVal")));
                } else if (dto.get("searchType").equals("kornFlnm")) {
                    builder.and(qTblUser.kornFlnm.contains((String) dto.get("searchVal")));
                }
            } else {
                builder.and(
                        qTblUser.userId.contains((String) dto.get("searchVal"))
                                .or(qTblUser.kornFlnm.contains((String) dto.get("searchVal")))
                );
            }

            List<TblUser> getApprovalMemberList = q.selectFrom(qTblUser)
                    .where(builder)
                    .orderBy(qTblUser.userSn.desc())
                    .offset(paginationInfo.getFirstRecordIndex())
                    .limit(paginationInfo.getRecordCountPerPage())
                    .fetch();

            Long totCnt = q.select(qTblUser.count())
                    .from(qTblUser)
                    .where(builder)
                    .fetchOne();
            if (totCnt == null) totCnt = 0L;
            paginationInfo.setTotalRecordCount(totCnt.intValue());

            resultVO.putResult("getApprovalMemberList", getApprovalMemberList);
            resultVO.putPaginationInfo(paginationInfo);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getApprovalMember(TblUser tblUser) {
        ResultVO resultVO = new ResultVO();

        try {
            resultVO.putResult("member", tblUserRepository.findByUserSn(tblUser.getUserSn()));
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getWaitMemberList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();

        try {
            if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
                paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
            }
            paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));

            JPAQueryFactory q = new JPAQueryFactory(em);
            QTblUser qTblUser = QTblUser.tblUser;

            BooleanBuilder builder = new BooleanBuilder();
            builder.and(qTblUser.actvtnYn.eq("Y").and(qTblUser.mbrStts.eq("W")));

            if (!StringUtils.isEmpty(dto.get("mbrType"))) {
                builder.and(qTblUser.mbrType.eq(Long.valueOf((String) dto.get("mbrType"))));
            }
            if (!StringUtils.isEmpty(dto.get("searchType"))) {
                if(dto.get("searchType").equals("userId")){
                    builder.and(qTblUser.userId.contains((String) dto.get("searchVal")));
                }else if(dto.get("searchType").equals("kornFlnm")){
                    builder.and(qTblUser.kornFlnm.contains((String) dto.get("searchVal")));
                }
            }else{
                builder.and(
                        qTblUser.userId.contains((String) dto.get("searchVal"))
                                .or(qTblUser.kornFlnm.contains((String) dto.get("searchVal")))
                );
            }

            List<TblUser> getWaitMemberList = q.selectFrom(qTblUser)
                    .where(builder)
                    .orderBy(qTblUser.userSn.desc())
                    .offset(paginationInfo.getFirstRecordIndex())
                    .limit(paginationInfo.getRecordCountPerPage())
                    .fetch();

            Long totCnt = q.select(qTblUser.count())
                    .from(qTblUser)
                    .where(builder)
                    .fetchOne();
            if (totCnt == null) totCnt = 0L;
            paginationInfo.setTotalRecordCount(totCnt.intValue());

            resultVO.putResult("getWaitMemberList", getWaitMemberList);
            resultVO.putPaginationInfo(paginationInfo);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setWaitMemberApproval(TblUser tblUser) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblUser qTblUser = QTblUser.tblUser;

            long updatedCount = new JPAQueryFactory(em)
                    .update(qTblUser)
                    .set(qTblUser.mbrStts, "Y")
                    .where(qTblUser.userSn.eq(tblUser.getUserSn()))
                    .execute();

            if (updatedCount > 0) {
                resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
                resultVO.setResultMessage("승인되었습니다.");
            } else {
                resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
                resultVO.setResultMessage("해당 회원이 존재하지 않습니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
            resultVO.setResultMessage("이용 정지 처리 중 오류가 발생했습니다.");
        }

        return resultVO;
    }

    public ResultVO setWaitMemberReject(TblUser tblUser) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblUser qTblUser = QTblUser.tblUser;

            long updatedCount = new JPAQueryFactory(em)
                    .update(qTblUser)
                    .set(qTblUser.mbrStts, "R")
                    .where(qTblUser.userSn.eq(tblUser.getUserSn()))
                    .execute();

            if (updatedCount > 0) {
                resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
                resultVO.setResultMessage("거절되었습니다.");
            } else {
                resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
                resultVO.setResultMessage("해당 회원이 존재하지 않습니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
            resultVO.setResultMessage("이용 정지 처리 중 오류가 발생했습니다.");
        }

        return resultVO;
    }

    public ResultVO getWaitMember(TblUser tblUser) {
        ResultVO resultVO = new ResultVO();

        try {
            resultVO.putResult("member", tblUserRepository.findByUserSn(tblUser.getUserSn()));
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getRejectMemberList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();

        try {
            if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
                paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
            }
            paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));

            JPAQueryFactory q = new JPAQueryFactory(em);
            QTblUser qTblUser = QTblUser.tblUser;

            BooleanBuilder builder = new BooleanBuilder();
            builder.and(qTblUser.actvtnYn.eq("Y").and(qTblUser.mbrStts.eq("R")));

            if (!StringUtils.isEmpty(dto.get("userSn"))) {
                builder.and(qTblUser.userSn.eq(Long.valueOf((String) dto.get("userSn"))));
            }
            if (!StringUtils.isEmpty(dto.get("mbrType"))) {
                builder.and(qTblUser.mbrType.eq(Long.valueOf((String) dto.get("mbrType"))));
            }
            if (!StringUtils.isEmpty(dto.get("searchType"))) {
                if (dto.get("searchType").equals("userId")) {
                    builder.and(qTblUser.userId.contains((String) dto.get("searchVal")));
                } else if (dto.get("searchType").equals("kornFlnm")) {
                    builder.and(qTblUser.kornFlnm.contains((String) dto.get("searchVal")));
                }
            } else {
                builder.and(
                        qTblUser.userId.contains((String) dto.get("searchVal"))
                                .or(qTblUser.kornFlnm.contains((String) dto.get("searchVal")))
                );
            }

            List<TblUser> getRejectMemberList = q.selectFrom(qTblUser)
                    .where(builder)
                    .orderBy(qTblUser.userSn.desc())
                    .offset(paginationInfo.getFirstRecordIndex())
                    .limit(paginationInfo.getRecordCountPerPage())
                    .fetch();

            Long totCnt = q.select(qTblUser.count())
                    .from(qTblUser)
                    .where(builder)
                    .fetchOne();
            if (totCnt == null) totCnt = 0L;
            paginationInfo.setTotalRecordCount(totCnt.intValue());

            resultVO.putResult("getRejectMemberList", getRejectMemberList);
            resultVO.putPaginationInfo(paginationInfo);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setRejectMemberApproval(TblUser tblUser) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblUser qTblUser = QTblUser.tblUser;

            long updatedCount = new JPAQueryFactory(em)
                    .update(qTblUser)
                    .set(qTblUser.mbrStts, "Y")
                    .where(qTblUser.userSn.eq(tblUser.getUserSn()))
                    .execute();

            if (updatedCount > 0) {
                resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
                resultVO.setResultMessage("승인되었습니다.");
            } else {
                resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
                resultVO.setResultMessage("해당 회원이 존재하지 않습니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
            resultVO.setResultMessage("이용 정지 처리 중 오류가 발생했습니다.");
        }

        return resultVO;
    }

    public ResultVO getRejectMember(TblUser tblUser) {
        ResultVO resultVO = new ResultVO();

        try {
            resultVO.putResult("member", tblUserRepository.findByUserSn(tblUser.getUserSn()));
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getStopMemberList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();

        try {
            if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
                paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
            }
            paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));

            JPAQueryFactory q = new JPAQueryFactory(em);
            QTblUser qTblUser = QTblUser.tblUser;

            BooleanBuilder builder = new BooleanBuilder();
            builder.and(qTblUser.actvtnYn.eq("Y").and(qTblUser.mbrStts.eq("S")));

            if (!StringUtils.isEmpty(dto.get("userSn"))) {
                builder.and(qTblUser.userSn.eq(Long.valueOf((String) dto.get("userSn"))));
            }
            if (!StringUtils.isEmpty(dto.get("mbrType"))) {
                builder.and(qTblUser.mbrType.eq(Long.valueOf((String) dto.get("mbrType"))));
            }
            if (!StringUtils.isEmpty(dto.get("searchType"))) {
                if (dto.get("searchType").equals("userId")) {
                    builder.and(qTblUser.userId.contains((String) dto.get("searchVal")));
                } else if (dto.get("searchType").equals("kornFlnm")) {
                    builder.and(qTblUser.kornFlnm.contains((String) dto.get("searchVal")));
                }
            } else {
                builder.and(
                        qTblUser.userId.contains((String) dto.get("searchVal"))
                                .or(qTblUser.kornFlnm.contains((String) dto.get("searchVal")))
                );
            }

            List<TblUser> getStopMemberList = q.selectFrom(qTblUser)
                    .where(builder)
                    .orderBy(qTblUser.userSn.desc())
                    .offset(paginationInfo.getFirstRecordIndex())
                    .limit(paginationInfo.getRecordCountPerPage())
                    .fetch();

            Long totCnt = q.select(qTblUser.count())
                    .from(qTblUser)
                    .where(builder)
                    .fetchOne();
            if (totCnt == null) totCnt = 0L;
            paginationInfo.setTotalRecordCount(totCnt.intValue());

            resultVO.putResult("getStopMemberList", getStopMemberList);
            resultVO.putPaginationInfo(paginationInfo);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setStopMemberApproval(TblUser tblUser) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblUser qTblUser = QTblUser.tblUser;

            long updatedCount = new JPAQueryFactory(em)
                    .update(qTblUser)
                    .set(qTblUser.mbrStts, "Y")
                    .where(qTblUser.userSn.eq(tblUser.getUserSn()))
                    .execute();

            if (updatedCount > 0) {
                resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
                resultVO.setResultMessage("승인되었습니다.");
            } else {
                resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
                resultVO.setResultMessage("해당 회원이 존재하지 않습니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
            resultVO.setResultMessage("이용 정지 처리 중 오류가 발생했습니다.");
        }

        return resultVO;
    }

    public ResultVO getStopMember(TblUser tblUser) {
        ResultVO resultVO = new ResultVO();

        try {
            resultVO.putResult("member", tblUserRepository.findByUserSn(tblUser.getUserSn()));
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getCancelMemberList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();

        try {
            if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
                paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
            }
            paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));

            JPAQueryFactory q = new JPAQueryFactory(em);
            QTblUser qTblUser = QTblUser.tblUser;

            BooleanBuilder builder = new BooleanBuilder();
            builder.and(qTblUser.actvtnYn.eq("Y").and(qTblUser.mbrStts.eq("C")));

            if (!StringUtils.isEmpty(dto.get("userSn"))) {
                builder.and(qTblUser.userSn.eq(Long.valueOf((String) dto.get("userSn"))));
            }
            if (!StringUtils.isEmpty(dto.get("mbrType"))) {
                builder.and(qTblUser.mbrType.eq(Long.valueOf((String) dto.get("mbrType"))));
            }
            if (!StringUtils.isEmpty(dto.get("searchType"))) {
                if (dto.get("searchType").equals("userId")) {
                    builder.and(qTblUser.userId.contains((String) dto.get("searchVal")));
                } else if (dto.get("searchType").equals("kornFlnm")) {
                    builder.and(qTblUser.kornFlnm.contains((String) dto.get("searchVal")));
                }
            } else {
                builder.and(
                        qTblUser.userId.contains((String) dto.get("searchVal"))
                                .or(qTblUser.kornFlnm.contains((String) dto.get("searchVal")))
                );
            }

            List<TblUser> getCancelMemberList = q.selectFrom(qTblUser)
                    .where(builder)
                    .orderBy(qTblUser.userSn.desc())
                    .offset(paginationInfo.getFirstRecordIndex())
                    .limit(paginationInfo.getRecordCountPerPage())
                    .fetch();

            Long totCnt = q.select(qTblUser.count())
                    .from(qTblUser)
                    .where(builder)
                    .fetchOne();
            if (totCnt == null) totCnt = 0L;
            paginationInfo.setTotalRecordCount(totCnt.intValue());

            resultVO.putResult("getCancelMemberList", getCancelMemberList);
            resultVO.putPaginationInfo(paginationInfo);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO managerMemberInsert(TblUser tblUser){
        ResultVO resultVO = new ResultVO();

        try{
            String hashedPswd = EgovFileScrty.encryptPassword(tblUser.getUserPw(), tblUser.getUserId());
            tblUser.setUserPw(hashedPswd);
            System.out.println("Pwd" + hashedPswd);
            String encryptedMblTelno = EgovFileScrty.encode(tblUser.getMblTelno());
            tblUser.setMblTelno(encryptedMblTelno);
            System.out.println("mblTel" + encryptedMblTelno);
            System.out.println(tblUser);
            tblUserRepository.save(tblUser);


            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
        }

        return resultVO;
    }
}