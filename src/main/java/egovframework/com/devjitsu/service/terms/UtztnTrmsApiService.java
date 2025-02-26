package egovframework.com.devjitsu.service.terms;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.bannerPopup.QTblBnrPopup;
import egovframework.com.devjitsu.model.bannerPopup.TblBnrPopup;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.terms.QTblUtztnTrms;
import egovframework.com.devjitsu.model.terms.TblUtztnTrms;
import egovframework.com.devjitsu.model.user.TblUser;
import egovframework.com.devjitsu.repository.terms.TblUtztnTrmsRepository;
import egovframework.com.devjitsu.service.common.RedisApiService;
import egovframework.com.jwt.EgovJwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class UtztnTrmsApiService {

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    private final EntityManager em;

    private final TblUtztnTrmsRepository tblUtztnTrmsRepository;

    public ResultVO setPrivacyPilicy(TblUtztnTrms tblUtztnTrms) {
        ResultVO resultVO = new ResultVO();

        try {
            tblUtztnTrms.setUtztnTrmsKnd("1");

            tblUtztnTrmsRepository.save(tblUtztnTrms);

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
            resultVO.setResultMessage("개인정보처리 방침이 성공적으로 완료되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getPrivacyPolicyList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();

        try {
            if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
                paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
            }
            if (!StringUtils.isEmpty(dto.get("pageSize"))) {
                paginationInfo.setPageSize(Integer.parseInt(dto.get("pageIndex").toString()));
            }else{
                paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));
            }

            if (!StringUtils.isEmpty(dto.get("pageUnit"))) {
                paginationInfo.setRecordCountPerPage(Integer.parseInt(dto.get("pageUnit").toString()));
            }else{
                paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            }

            QTblUtztnTrms qTblUtztnTrms = QTblUtztnTrms.tblUtztnTrms;
            JPAQueryFactory q = new JPAQueryFactory(em);

            BooleanBuilder builder = new BooleanBuilder();

            if (!StringUtils.isEmpty(dto.get("useYn"))) {
                builder.and(qTblUtztnTrms.useYn.eq((String) dto.get("useYn")));
            }

            if (!StringUtils.isEmpty(dto.get("searchType"))) {
                if(dto.get("searchType").equals("utztnTrmsCn")){
                    builder.and(qTblUtztnTrms.utztnTrmsCn.contains((String) dto.get("searchVal")));
                }else if(dto.get("searchType").equals("utztnTrmsTtl")){
                    builder.and(qTblUtztnTrms.utztnTrmsTtl.contains((String) dto.get("searchVal")));
                }
            }else{
                builder.and(
                        qTblUtztnTrms.utztnTrmsCn.contains((String) dto.get("searchVal"))
                                .or(qTblUtztnTrms.utztnTrmsTtl.contains((String) dto.get("searchVal")))
                );
            }

            List<TblUtztnTrms> getPrivacyPolicyList = q.selectFrom(qTblUtztnTrms)
                    .where(builder)
                    .where(qTblUtztnTrms.useYn.in("Y", "N"))
                    .where(qTblUtztnTrms.utztnTrmsKnd.eq("1"))
                    .orderBy(qTblUtztnTrms.frstCrtDt.desc())
                    .offset(paginationInfo.getFirstRecordIndex())
                    .limit(paginationInfo.getRecordCountPerPage())
                    .fetch();
            Long totCnt = q.select(qTblUtztnTrms.count())
                    .from(qTblUtztnTrms)
                    .where(qTblUtztnTrms.useYn.in("Y", "N"))
                    .where(qTblUtztnTrms.utztnTrmsKnd.eq("1"))
                    .where(builder)
                    .fetchOne();
            if (totCnt == null) totCnt = 0L;
            paginationInfo.setTotalRecordCount(totCnt.intValue());

            resultVO.putResult("getPrivacyPolicyList", getPrivacyPolicyList);
            resultVO.putPaginationInfo(paginationInfo);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getPrivacyPolicyListNotPaging(SearchDto dto) {
        ResultVO resultVO = new ResultVO();

        try {

            QTblUtztnTrms qTblUtztnTrms = QTblUtztnTrms.tblUtztnTrms;
            JPAQueryFactory q = new JPAQueryFactory(em);

            BooleanBuilder builder = new BooleanBuilder();
            builder.and(qTblUtztnTrms.useYn.eq("Y"));

            if (!StringUtils.isEmpty(dto.get("utztnTrmsKnd"))) {
                builder.and(qTblUtztnTrms.utztnTrmsKnd.eq((String) dto.get("utztnTrmsKnd")));
            }

            List<TblUtztnTrms> dataList = q.selectFrom(qTblUtztnTrms)
                    .where(builder)
                    .orderBy(qTblUtztnTrms.frstCrtDt.desc())
                    .fetch();
            resultVO.putResult("dataList", dataList);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setPrivacyPolicyDel(TblUtztnTrms tblUtztnTrms) {
        ResultVO resultVO = new ResultVO();
        try {
            tblUtztnTrmsRepository.delete(tblUtztnTrms);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getPrivacyPolicy(TblUtztnTrms tblUtztnTrms) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblUtztnTrms qTblUtztnTrms = QTblUtztnTrms.tblUtztnTrms;
            JPAQueryFactory q = new JPAQueryFactory(em);
            TblUtztnTrms tblUtztnTrmsData = tblUtztnTrmsRepository.findByUtztnTrmsSn(tblUtztnTrms.getUtztnTrmsSn());
            resultVO.putResult("tblUtztnTrms", tblUtztnTrmsData);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setTermsAgreement(TblUtztnTrms tblUtztnTrms) {
        ResultVO resultVO = new ResultVO();

        try {
            tblUtztnTrms.setUtztnTrmsKnd("2");

            tblUtztnTrmsRepository.save(tblUtztnTrms);

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
            resultVO.setResultMessage("개인정보처리 방침이 성공적으로 완료되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }

        return resultVO;
    }


    public ResultVO getTermsAgreementList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();

        try {
            if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
                paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
            }
            if (!StringUtils.isEmpty(dto.get("pageSize"))) {
                paginationInfo.setPageSize(Integer.parseInt(dto.get("pageIndex").toString()));
            }else{
                paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));
            }

            if (!StringUtils.isEmpty(dto.get("pageUnit"))) {
                paginationInfo.setRecordCountPerPage(Integer.parseInt(dto.get("pageUnit").toString()));
            }else{
                paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            }

            QTblUtztnTrms qTblUtztnTrms = QTblUtztnTrms.tblUtztnTrms;
            JPAQueryFactory q = new JPAQueryFactory(em);

            BooleanBuilder builder = new BooleanBuilder();

            if (!StringUtils.isEmpty(dto.get("useYn"))) {
                builder.and(qTblUtztnTrms.useYn.eq((String) dto.get("useYn")));
            }

            if (!StringUtils.isEmpty(dto.get("searchType"))) {
                if(dto.get("searchType").equals("utztnTrmsCn")){
                    builder.and(qTblUtztnTrms.utztnTrmsCn.contains((String) dto.get("searchVal")));
                }else if(dto.get("searchType").equals("utztnTrmsTtl")){
                    builder.and(qTblUtztnTrms.utztnTrmsTtl.contains((String) dto.get("searchVal")));
                }
            }else{
                builder.and(
                        qTblUtztnTrms.utztnTrmsCn.contains((String) dto.get("searchVal"))
                                .or(qTblUtztnTrms.utztnTrmsTtl.contains((String) dto.get("searchVal")))
                );
            }

            List<TblUtztnTrms> getTermsAgreementList = q.selectFrom(qTblUtztnTrms)
                    .where(builder)
                    .where(qTblUtztnTrms.useYn.in("Y", "N"))
                    .where(qTblUtztnTrms.utztnTrmsKnd.eq("2"))
                    .orderBy(qTblUtztnTrms.frstCrtDt.desc())
                    .offset(paginationInfo.getFirstRecordIndex())
                    .limit(paginationInfo.getRecordCountPerPage())
                    .fetch();
            Long totCnt = q.select(qTblUtztnTrms.count())
                    .from(qTblUtztnTrms)
                    .where(builder)
                    .where(qTblUtztnTrms.useYn.in("Y", "N"))
                    .where(qTblUtztnTrms.utztnTrmsKnd.eq("2"))
                    .fetchOne();
            if (totCnt == null) totCnt = 0L;
            paginationInfo.setTotalRecordCount(totCnt.intValue());

            resultVO.putResult("getTermsAgreementList", getTermsAgreementList);
            resultVO.putPaginationInfo(paginationInfo);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }


    public ResultVO setTermsAgreementDel(TblUtztnTrms tblUtztnTrms) {
        ResultVO resultVO = new ResultVO();
        try {
            tblUtztnTrmsRepository.delete(tblUtztnTrms);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getTermsAgreemet(TblUtztnTrms tblUtztnTrms) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblUtztnTrms qTblUtztnTrms = QTblUtztnTrms.tblUtztnTrms;
            JPAQueryFactory q = new JPAQueryFactory(em);
            TblUtztnTrms tblUtztnTrmsData = tblUtztnTrmsRepository.findByUtztnTrmsSn(tblUtztnTrms.getUtztnTrmsSn());
            resultVO.putResult("tblUtztnTrms", tblUtztnTrmsData);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }


}
