package egovframework.com.devjitsu.service.bannerPopup;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.bannerPopup.QTblBnrPopup;
import egovframework.com.devjitsu.model.bannerPopup.TblBnrPopup;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.repository.bannerPopup.TblBnrPopupRepository;
import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BnrPopupApiService {

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    private final EntityManager em;
    private final TblBnrPopupRepository tblBnrPopupRepository;

    public ResultVO getBnrPopupListOnPage(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();

        try {
            if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
                paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
            }
            paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));

            QTblBnrPopup qTblBnrPopup = QTblBnrPopup.tblBnrPopup;
            JPAQueryFactory q = new JPAQueryFactory(em);

            BooleanBuilder builder = new BooleanBuilder();
            if (!StringUtils.isEmpty(dto.get("bnrPopupKnd"))) {
                builder.and(qTblBnrPopup.bnrPopupKnd.eq((String) dto.get("bnrPopupKnd")));
            }

            List<TblBnrPopup> bannerPopupList = q.selectFrom(qTblBnrPopup).where(builder).orderBy(qTblBnrPopup.frstCrtDt.desc()).offset(paginationInfo.getFirstRecordIndex()).limit(paginationInfo.getRecordCountPerPage()).fetch();

            Long totCnt = q.select(qTblBnrPopup.count())
                    .from(qTblBnrPopup)
                    .where(builder).orderBy(qTblBnrPopup.frstCrtDt.desc()).fetchOne();
            if(totCnt == null) totCnt = 0L;
            paginationInfo.setTotalRecordCount(totCnt.intValue());

            resultVO.putResult("bannerPopupList", bannerPopupList);
            resultVO.putPaginationInfo(paginationInfo);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getBnrPopup(TblBnrPopup tblBnrPopup) {
        ResultVO resultVO = new ResultVO();
        try {
            QTblBnrPopup qTblBnrPopup = QTblBnrPopup.tblBnrPopup;
            JPAQueryFactory q = new JPAQueryFactory(em);
            resultVO.putResult("tblBnrPopup", tblBnrPopupRepository.findByBnrPopupSn(tblBnrPopup.getBnrPopupSn()));
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }
}
