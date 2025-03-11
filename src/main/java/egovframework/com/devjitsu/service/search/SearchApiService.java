package egovframework.com.devjitsu.service.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.QTblComCdGroup;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.common.TblComCdGroup;
import egovframework.com.devjitsu.model.menu.QTblMenu;
import egovframework.com.devjitsu.model.menu.TblContent;
import egovframework.com.devjitsu.model.menu.TblMenu;
import egovframework.com.devjitsu.model.search.*;
import egovframework.com.devjitsu.model.search.QTblIntgSrch;
import egovframework.com.devjitsu.model.search.QTblSrchKywd;
import egovframework.com.devjitsu.model.user.QTblUser;
import egovframework.com.devjitsu.repository.menu.TblContentRepository;
import egovframework.com.devjitsu.repository.menu.TblMenuRepository;
import egovframework.com.devjitsu.repository.search.TblSrchKywdRepository;
import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SearchApiService {

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    @Autowired
    private TblSrchKywdRepository tblSrchKywdRepository;

    private final EntityManager em;

    public ResultVO getSearchDataListPage(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();
        try{

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

            QTblIntgSrch qTblIntgSrch = QTblIntgSrch.tblIntgSrch;
            QTblMenu qTblMenu = QTblMenu.tblMenu;
            QTblUser qTblUser = QTblUser.tblUser;
            QTblSrchKywd qTblSrchKywd = QTblSrchKywd.tblSrchKywd;
            JPAQueryFactory q = new JPAQueryFactory(em);

            /** query DSL 조건 추가하는 방법 */
            BooleanBuilder builder = new BooleanBuilder();
            if (!StringUtils.isEmpty(dto.get("searchType"))) {
                if(dto.get("searchType").equals("1")){
                    builder.and(qTblIntgSrch.ttl.contains((String) dto.get("searchVal")));
                }else if(dto.get("searchType").equals("2")){
                    builder.and(qTblIntgSrch.cn.contains((String) dto.get("searchVal")));
                }else if(dto.get("searchType").equals("3")){
                    builder.and(qTblIntgSrch.atchFileNm.contains((String) dto.get("searchVal")));
                }else{
                    builder.and(qTblIntgSrch.ttl.contains((String) dto.get("searchVal")).or(qTblIntgSrch.atchFileNm.contains((String) dto.get("searchVal"))).or(qTblIntgSrch.cn.contains((String) dto.get("searchVal"))));
                }
            }else{
                builder.and(qTblIntgSrch.ttl.contains((String) dto.get("searchVal")).or(qTblIntgSrch.atchFileNm.contains((String) dto.get("searchVal"))).or(qTblIntgSrch.cn.contains((String) dto.get("searchVal"))));
            }

            if(!StringUtils.isEmpty(dto.get("searchVal"))){
                TblSrchKywd tblSrchKywd = tblSrchKywdRepository.findByKywd((String) dto.get("searchVal"));
                if(tblSrchKywd == null){
                    tblSrchKywd = new TblSrchKywd();
                    tblSrchKywd.setKywd((String) dto.get("searchVal"));
                }
                tblSrchKywd.incrementSrchNmtm();
                tblSrchKywdRepository.save(tblSrchKywd);
            }

            if (!StringUtils.isEmpty(dto.get("searchStartDt")) && !StringUtils.isEmpty(dto.get("searchEndDt"))) {
                builder.and(qTblIntgSrch.frstCrtDt.between(
                        LocalDateTime.parse(
                                dto.get("searchStartDt").toString()
                        ).with(LocalTime.MIN),
                        LocalDateTime.parse(
                                dto.get("searchEndDt").toString()
                        ).with(LocalTime.MAX)
                ));
            }else if (!StringUtils.isEmpty(dto.get("searchStartDt"))){
                builder.and(qTblIntgSrch.frstCrtDt.goe(
                    LocalDateTime.parse(
                        dto.get("searchStartDt").toString()
                    )
                ));
            }else if (!StringUtils.isEmpty(dto.get("searchEndDt"))){
                builder.and(qTblIntgSrch.frstCrtDt.loe(
                        LocalDateTime.parse(
                                dto.get("searchEndDt").toString()
                        )
                ));
            }


            List<TblIntgSrchDTO> searchList =
                    q.select(
                        Projections.constructor(
                                TblIntgSrchDTO.class,
                                qTblIntgSrch.intgSrchSn,
                                qTblIntgSrch.menuSn,
                                qTblMenu.upperMenuSn,
                                qTblIntgSrch.atchFileSn,
                                qTblIntgSrch.pstSn,
                                qTblIntgSrch.knd,
                                qTblIntgSrch.url,
                                qTblIntgSrch.ttl,
                                qTblIntgSrch.cn,
                                qTblIntgSrch.atchFileNm,
                                qTblIntgSrch.atchFileExtnNm,
                                qTblMenu.menuNmPath,
                                JPAExpressions.select(qTblUser.kornFlnm).from(qTblUser).where(qTblUser.userSn.eq(qTblIntgSrch.creatrSn)),
                                qTblIntgSrch.frstCrtDt
                        )
                    )
                    .from(qTblIntgSrch)
                    .join(qTblMenu)
                    .on(qTblIntgSrch.menuSn.eq(qTblMenu.menuSn))
                    .where(builder)
                    .orderBy(qTblIntgSrch.frstCrtDt.desc())
                    .offset(paginationInfo.getFirstRecordIndex())
                    .limit(paginationInfo.getRecordCountPerPage())
                    .fetch();

            Long totCnt = q.select(qTblIntgSrch.count())
                    .from(qTblIntgSrch)
                    .where(builder).orderBy(qTblIntgSrch.frstCrtDt.desc()).fetchOne();

            List<TblSrchKywd> returnSrchKywd = q.selectFrom(qTblSrchKywd).orderBy(qTblSrchKywd.srchNmtm.desc()).limit(3).fetch();

            if(totCnt == null) totCnt = 0L;
            paginationInfo.setTotalRecordCount(totCnt.intValue());
            resultVO.putResult("returnSrchKywd", returnSrchKywd);
            resultVO.putResult("searchList", searchList);
            resultVO.putPaginationInfo(paginationInfo);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (NullPointerException e){
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }
        return resultVO;
    }
}
