package egovframework.com.devjitsu.service.common;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.querydsl.core.BooleanBuilder;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.QTblComCd;
import egovframework.com.devjitsu.model.common.QTblComCdGroup;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.common.TblComCdGroup;
import egovframework.com.devjitsu.model.login.LettnemplyrinfoVO;
import egovframework.com.devjitsu.model.menu.QTblAuthrtGroupMenu;
import egovframework.com.devjitsu.model.menu.QTblMenuAuthrtGroupUser;
import egovframework.com.devjitsu.model.menu.TblMenuAuthrtGroup;
import egovframework.com.devjitsu.repository.common.TblComCdGroupRepository;
import egovframework.com.devjitsu.repository.common.TblComCdRepository;
import egovframework.com.devjitsu.repository.common.TblComFileRepository;
import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class CommonApiService {

    private final EntityManager em;

    @Autowired
    private RedisApiService redisApiService;

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

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
    private final TblComCdRepository tblComCdRepository;
    private final TblComCdGroupRepository tblComCdGroupRepository;
    private final TblComFileRepository tblComFileRepository;

    public ResultVO getComCdGroupList(Map<String, Object> params) {
        ResultVO resultVO = new ResultVO();

        QTblComCdGroup qTblComCdGroup = QTblComCdGroup.tblComCdGroup;
        JPAQueryFactory q = new JPAQueryFactory(em);

        /** query DSL 조건 추가하는 방법 */
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qTblComCdGroup.actvtnYn.eq("Y"));

        resultVO.putResult("rs",  q.selectFrom(qTblComCdGroup).where(builder).orderBy(qTblComCdGroup.frstCrtDt.desc()).fetch());
        resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        return resultVO;
    }

    public ResultVO getRedisUserInfo(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        resultVO.putResult("rs", redisApiService.getRedis(0, String.valueOf(dto.get("userSn"))));
        resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        resultVO.setResultMessage(ResponseCode.SUCCESS.getMessage());
        return resultVO;
    }

    public ResultVO getComCdGroupListOnPageing(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();
        try{

            if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
                paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
            }
            paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));

            QTblComCdGroup qTblComCdGroup = QTblComCdGroup.tblComCdGroup;
            JPAQueryFactory q = new JPAQueryFactory(em);

            /** query DSL 조건 추가하는 방법 */
            BooleanBuilder builder = new BooleanBuilder();
            List<TblComCdGroup> cdGroupList = q.selectFrom(qTblComCdGroup).where(builder).orderBy(qTblComCdGroup.frstCrtDt.desc()).fetch();

            Long totCnt = q.select(qTblComCdGroup.count())
                    .from(qTblComCdGroup)
                    .where(builder).orderBy(qTblComCdGroup.frstCrtDt.desc()).fetchOne();

            if(totCnt == null) totCnt = 0L;
            paginationInfo.setTotalRecordCount(totCnt.intValue());
            resultVO.putResult("authGroup", cdGroupList);
            resultVO.putPaginationInfo(paginationInfo);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }
        return resultVO;
    }

    public ResultVO setComCdGroup(TblComCdGroup tblComCdGroup){
        ResultVO resultVO = new ResultVO();
        try {
            JPAQueryFactory q = new JPAQueryFactory(em);
            tblComCdGroupRepository.save(tblComCdGroup);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
        }
        return resultVO;
    }

    public ResultVO getComCdGroup(TblComCdGroup tblComCdGroup){
        ResultVO resultVO = new ResultVO();
        try {

            QTblComCdGroup qTblComCdGroup = QTblComCdGroup.tblComCdGroup;
            JPAQueryFactory q = new JPAQueryFactory(em);


            resultVO.putResult("comCdGroup", tblComCdGroupRepository.findByCdGroupSn(tblComCdGroup.getCdGroupSn()));
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
        }
        return resultVO;
    }

    public ResultVO setComCdGroupDel(TblComCdGroup tblComCdGroup) {
        ResultVO resultVO = new ResultVO();
        try {
            JPAQueryFactory q = new JPAQueryFactory(em);
            QTblComCd qTblComCd = QTblComCd.tblComCd;
            q.delete(qTblComCd).where(qTblComCd.cdGroupSn.eq(tblComCdGroup.getCdGroupSn())).execute();
            tblComCdGroupRepository.delete(tblComCdGroup);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }
        return resultVO;
    }
}
