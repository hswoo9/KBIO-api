package egovframework.com.devjitsu.service.access;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.access.*;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.repository.access.TblMngrAcsIpRepository;
import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MngrAcsIpApiService {

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;


    private final TblMngrAcsIpRepository tblMngrAcsIpRepository;
    private final EntityManager em;

    public ResultVO getMngrAcsIpList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();

        try {
            if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
                paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
            }
            paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));

            QTblMngrAcsIp qTblMngrAcsIp = QTblMngrAcsIp.tblMngrAcsIp;
            JPAQueryFactory q = new JPAQueryFactory(em);

            /** query DSL 조건 추가하는 방법 */
            BooleanBuilder builder = new BooleanBuilder();
            if (!StringUtils.isEmpty(dto.get("actvtnYn"))) {
                builder.and(qTblMngrAcsIp.actvtnYn.eq((String) dto.get("actvtnYn")));
            }

            if (!StringUtils.isEmpty(dto.get("searchType"))) {
                if(dto.get("searchType").equals("1")){
                    builder.and(qTblMngrAcsIp.ipAddr.contains((String) dto.get("searchVal")));
                }else if(dto.get("searchType").equals("2")){
                    builder.and(qTblMngrAcsIp.plcusNm.contains((String) dto.get("searchVal")));
                }else{
                    builder.and(qTblMngrAcsIp.ipAddr.contains((String) dto.get("searchVal")).or(qTblMngrAcsIp.plcusNm.contains((String) dto.get("searchVal"))));
                }
            }

            List<TblMngrAcsIp> mngrAcsIpList = q.selectFrom(qTblMngrAcsIp)
                    .where(builder)
                    .orderBy(qTblMngrAcsIp.frstCrtDt.desc())
                    .offset(paginationInfo.getFirstRecordIndex())
                    .limit(paginationInfo.getRecordCountPerPage())
                    .fetch();

            Long totCnt = q.select(qTblMngrAcsIp.count())
                    .from(qTblMngrAcsIp)
                    .where(builder)
                    .fetchOne();
            if(totCnt == null) totCnt = 0L;
            paginationInfo.setTotalRecordCount(totCnt.intValue());

            resultVO.putResult("mngrAcsIpList", mngrAcsIpList);
            resultVO.putPaginationInfo(paginationInfo);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (NullPointerException e) {
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getMngrAcsIp(TblMngrAcsIp tblMngrAcsIp) {
        ResultVO resultVO = new ResultVO();

        try {
            resultVO.putResult("mngrAcsIp", tblMngrAcsIpRepository.findByMngrAcsSn(tblMngrAcsIp.getMngrAcsSn()));
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (NullPointerException e) {
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setMngrAcsIp(TblMngrAcsIp tblMngrAcsIp) {
        ResultVO resultVO = new ResultVO();

        try {
            tblMngrAcsIpRepository.save(tblMngrAcsIp);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (NullPointerException e) {
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setMngrAcsIpDel(TblMngrAcsIp tblMngrAcsIp) {
        ResultVO resultVO = new ResultVO();

        try {
            tblMngrAcsIpRepository.delete(tblMngrAcsIp);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (NullPointerException e) {
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }

        return resultVO;
    }

    public List<String> getMngrIps() {
        List<TblMngrAcsIp> allowedIps = tblMngrAcsIpRepository.findAll();
        return allowedIps.stream()
                .map(TblMngrAcsIp::getIpAddr)
                .collect(Collectors.toList());
    }
}
