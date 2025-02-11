package egovframework.com.devjitsu.service.consult;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.QTblComFile;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.consult.TblCnsltAply;
import egovframework.com.devjitsu.model.consult.TblCnsltDtl;
import egovframework.com.devjitsu.model.user.QTblCnslttMbr;
import egovframework.com.devjitsu.model.user.QTblUser;
import egovframework.com.devjitsu.model.user.TblUser;
import egovframework.com.devjitsu.model.consult.*;
import egovframework.com.devjitsu.repository.common.TblComFileRepository;
import egovframework.com.devjitsu.repository.consult.TblCnsltAplyRepository;
import egovframework.com.devjitsu.repository.consult.TblCnsltDtlRepository;
import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ConsultingAdminApiService {

    private final EntityManager em;

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    @Resource(name = "EgovFileMngUtil")
    private EgovFileMngUtil fileUtil;

    private final TblCnsltAplyRepository tblCnsltAplyRepository;
    private final TblComFileRepository tblComFileRepository;
    private final TblCnsltDtlRepository tblCnsltDtlRepository;

    /**
     * jpa 부등호
     * gt : >
     * lt : <
     * goe : >=
     * loe : <=
     */
    /**
     * query DSL 조건 추가하는 방법
     * BooleanBuilder builder = new BooleanBuilder();
     * builder.and(qTblComCdGroup.actvtnYn.eq("Y"));
     */
    public ResultVO getConsultingList(SearchDto dto) {
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
            QTblCnsltDtl qtblCnsltDtl = QTblCnsltDtl.tblCnsltDtl;

            JPAQueryFactory q = new JPAQueryFactory(em);

            /** query DSL 조건 추가하는 방법 */
            BooleanBuilder builder = new BooleanBuilder();
            builder.and(qTblCnsltAply.cnsltSe.eq((String) dto.get("cnsltSe")));
            if (!StringUtils.isEmpty(dto.get("startDt"))) {
                builder.and(
                    Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", qTblCnsltAply.frstCrtDt).loe(
                            Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", dto.get("startDt"))
                    )
                );
            }
            if (!StringUtils.isEmpty(dto.get("endDt"))) {
                builder.and(
                    Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", qTblCnsltAply.frstCrtDt).loe(
                            Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", dto.get("endDt"))
                    )
                );
            }

            if (!StringUtils.isEmpty(dto.get("cnsltFld"))) {
                builder.and(qTblCnsltAply.cnsltFld.eq((String) dto.get("cnsltFld")));
            }

            if (!StringUtils.isEmpty(dto.get("cnsltSttsCd"))) {
                builder.and(qtblCnsltDtl.cnsltSttsCd.eq((String) dto.get("cnsltSttsCd")));
            }

            if (!StringUtils.isEmpty(dto.get("searchType"))) {
                if(dto.get("searchType").equals("kornFlnm")){
                    builder.and(qTblUser.kornFlnm.contains((String) dto.get("searchVal")));
                }else if(dto.get("searchType").equals("ogdpNm")){
                    builder.and(qTblCnslttMbr.ogdpNm.contains((String) dto.get("searchVal")));
                }else if(dto.get("searchType").equals("jbpsNm")){
                    builder.and(qTblCnslttMbr.jbpsNm.contains((String) dto.get("searchVal")));
                }
            }else{
                builder.and(
                    qTblUser.kornFlnm.contains((String) dto.get("searchVal"))
                    .or(qTblCnslttMbr.ogdpNm.contains((String) dto.get("searchVal")))
                    .or(qTblCnslttMbr.jbpsNm.contains((String) dto.get("searchVal")))
                );
            }

            List<TblUser> consultantList = q.selectFrom(qTblUser)
                    .where(builder)
//                    .join().on() 컨설턴트
//                    .join().on() 컨설턴트 사진
                    .orderBy(qTblUser.frstCrtDt.desc())
                    .offset(paginationInfo.getFirstRecordIndex())
                    .limit(paginationInfo.getRecordCountPerPage())
                    .fetch();

            Long totCnt = q.select(qTblUser.count())
//                    .join().on() 컨설턴트
//                    .join().on() 컨설턴트 사진
                    .from(qTblUser)
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
}
