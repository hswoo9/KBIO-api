package egovframework.com.devjitsu.service.bbs;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.bbs.*;
import egovframework.com.devjitsu.model.bbs.QTblBbs;
import egovframework.com.devjitsu.model.bbs.QTblPst;
import egovframework.com.devjitsu.model.common.QTblComFile;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.common.TblComFile;
import egovframework.com.devjitsu.model.menu.*;
import egovframework.com.devjitsu.model.menu.QTblAuthrtGroupMenu;
import egovframework.com.devjitsu.model.menu.QTblMenu;
import egovframework.com.devjitsu.model.menu.QTblMenuAuthrtGroup;
import egovframework.com.devjitsu.model.menu.QTblMenuAuthrtGroupUser;
import egovframework.com.devjitsu.model.search.TblIntgSrchDTO;
import egovframework.com.devjitsu.repository.bbs.TblBbsRepository;
import egovframework.com.devjitsu.repository.bbs.TblPstRepository;
import egovframework.com.devjitsu.repository.common.TblComFileRepository;
import egovframework.com.devjitsu.service.menu.MenuAuthGroupApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BbsAdminApiService {

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    @Resource(name = "EgovFileMngUtil")
    private EgovFileMngUtil fileUtil;

    @Autowired
    private MenuAuthGroupApiService menuAuthGroupApiService;

    private final EntityManager em;

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
    private final TblBbsRepository tblBbsRepository;
    private final TblPstRepository tblPstRepository;
    private final TblComFileRepository tblComFileRepository;

    public ResultVO getBbsList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();

        try {
            if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
                paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
            }
            paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));

            QTblBbs qTblBbs = QTblBbs.tblBbs;
            JPAQueryFactory q = new JPAQueryFactory(em);

            /** query DSL 조건 추가하는 방법 */
            BooleanBuilder builder = new BooleanBuilder();
            if (!StringUtils.isEmpty(dto.get("bbsNm"))) {
                builder.and(qTblBbs.bbsNm.contains((String) dto.get("bbsNm")));
            }
            if (!StringUtils.isEmpty(dto.get("bbsType"))) {
                builder.and(qTblBbs.bbsTypeNm.eq((String) dto.get("bbsTypeNm")));
            }

            List<TblBbs> bbsList = q.selectFrom(qTblBbs)
                    .where(builder)
                    .orderBy(qTblBbs.frstCrtDt.desc())
                    .offset(paginationInfo.getFirstRecordIndex())
                    .limit(paginationInfo.getRecordCountPerPage())
                    .fetch();

            Long totCnt = q.select(qTblBbs.count())
                    .from(qTblBbs)
                    .where(builder)
                    .fetchOne();
            if(totCnt == null) totCnt = 0L;
            paginationInfo.setTotalRecordCount(totCnt.intValue());

            resultVO.putResult("bbsList", bbsList);
            resultVO.putPaginationInfo(paginationInfo);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (NullPointerException e) {
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getBbsAllList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        try {
            QTblBbs qTblBbs = QTblBbs.tblBbs;
            JPAQueryFactory q = new JPAQueryFactory(em);
            BooleanBuilder builder = new BooleanBuilder();
            if (!StringUtils.isEmpty(dto.get("bbsNm"))) {
                builder.and(qTblBbs.bbsNm.contains((String) dto.get("bbsNm")));
            }
            if (!StringUtils.isEmpty(dto.get("bbsType"))) {
                builder.and(qTblBbs.bbsTypeNm.eq((String) dto.get("bbsTypeNm")));
            }
            List<TblBbs> bbsList = q.selectFrom(qTblBbs).where(builder).orderBy(qTblBbs.frstCrtDt.desc()).fetch();
            resultVO.putResult("bbsList", bbsList);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (NullPointerException e) {
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }
        return resultVO;
    }

    public ResultVO setBbs(TblBbs tblBbs) {
        ResultVO resultVO = new ResultVO();

        try {
            tblBbsRepository.save(tblBbs);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (NullPointerException e) {
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getBbs(TblBbs tblBbs) {
        ResultVO resultVO = new ResultVO();

        try {
            resultVO.putResult("bbs", getBbs(tblBbs.getBbsSn()));
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (NullPointerException e) {
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setBbsDel(TblBbs tblBbs) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblPst qTblPst = QTblPst.tblPst;
            JPAQueryFactory q = new JPAQueryFactory(em);
            List<TblPst> tblPsts = q.selectFrom(qTblPst).where(qTblPst.bbsSn.eq(tblBbs.getBbsSn())).fetch();
            for(TblPst tblPst : tblPsts){
                deletePstRecursively(tblPst);
            }

            tblBbsRepository.delete(tblBbs);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (NullPointerException e) {
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public TblBbs getBbs(long bbsSn) {
        return tblBbsRepository.findByBbsSn(bbsSn);
    }

    private void deletePstRecursively(TblPst tblPst) {
        QTblPst qTblPst = QTblPst.tblPst;
        QTblComFile qTblComFile = QTblComFile.tblComFile;

        JPAQueryFactory q = new JPAQueryFactory(em);
        /** 답글 삭제 */
        List<TblPst> replyPsts = q.selectFrom(qTblPst).where(qTblPst.upPstSn.eq(tblPst.getPstSn())).fetch();
        for (TblPst replyPst : replyPsts) {
            deletePstRecursively(replyPst);
        }

        List<TblComFile> pstFiles = q.selectFrom(qTblComFile).where(qTblComFile.psnTblSn.eq("pst_" + tblPst.getPstSn())).fetch();
        for (TblComFile pstFile : pstFiles) {
            boolean isDelete = fileUtil.deleteFile(new String[]{pstFile.getStrgFileNm()}, pstFile.getAtchFilePathNm());
            if(isDelete){
                tblComFileRepository.delete(pstFile);
            }else{
                throw new NullPointerException();
            }
        }

        tblPstRepository.delete(tblPst);
    }

    public AuthrtDto getUserBbsAuthrt(TblBbs tblBbs, Long userSn) {
        AuthrtDto authrtDto = new AuthrtDto("N", "N", "N", "N");

        try {
            QTblMenu qTblMenu = QTblMenu.tblMenu;
            QTblMenuAuthrtGroup qTblMenuAuthrtGroup = QTblMenuAuthrtGroup.tblMenuAuthrtGroup;
            QTblAuthrtGroupMenu qTblAuthrtGroupMenu = QTblAuthrtGroupMenu.tblAuthrtGroupMenu;
            QTblMenuAuthrtGroupUser qTblMenuAuthrtGroupUser = QTblMenuAuthrtGroupUser.tblMenuAuthrtGroupUser;
            JPAQueryFactory q = new JPAQueryFactory(em);

            if(userSn != null){
                authrtDto = q
                    .select(
                        Projections.constructor(
                            AuthrtDto.class,
                            Expressions.stringTemplate("MAX({0})", qTblMenuAuthrtGroup.inqAuthrt),
                            Expressions.stringTemplate("MAX({0})", qTblMenuAuthrtGroup.wrtAuthrt),
                            Expressions.stringTemplate("MAX({0})", qTblMenuAuthrtGroup.mdfcnAuthrt),
                            Expressions.stringTemplate("MAX({0})", qTblMenuAuthrtGroup.delAuthrt)
                        )
                    ).from(qTblMenuAuthrtGroup)
                    .join(qTblAuthrtGroupMenu).on(qTblMenuAuthrtGroup.authrtGroupSn.eq(qTblAuthrtGroupMenu.authrtGroupSn))
                    .join(qTblMenuAuthrtGroupUser).on(qTblMenuAuthrtGroupUser.authrtGroupSn.eq(qTblMenuAuthrtGroup.authrtGroupSn))
                    .join(qTblMenu).on(qTblAuthrtGroupMenu.menuSn.eq(qTblMenu.menuSn))
                    .where(
                        qTblMenuAuthrtGroupUser.userSn.eq(userSn)
                            .and(
                                Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", qTblMenuAuthrtGroupUser.authrtGrntDt).loe(
                                    Expressions.stringTemplate("DATE_FORMAT(NOW(), '%Y-%m-%d')")
                                )
                            ).and(
                                    qTblMenu.bbsSn.eq(tblBbs.getBbsSn())
                            )
                    ).groupBy(qTblMenu.bbsSn).fetchFirst();
            }else{
                authrtDto = q
                    .select(
                        Projections.constructor(
                            AuthrtDto.class,
                            qTblMenuAuthrtGroup.inqAuthrt,
                            qTblMenuAuthrtGroup.wrtAuthrt,
                            qTblMenuAuthrtGroup.mdfcnAuthrt,
                            qTblMenuAuthrtGroup.delAuthrt
                        )
                    ).from(qTblMenuAuthrtGroup)
                    .join(qTblAuthrtGroupMenu).on(qTblMenuAuthrtGroup.authrtGroupSn.eq(qTblAuthrtGroupMenu.authrtGroupSn))
                    .join(qTblMenu).on(qTblAuthrtGroupMenu.menuSn.eq(qTblMenu.menuSn))
                    .where(qTblMenuAuthrtGroup.authrtGroupSn.eq(1L).and(qTblMenu.bbsSn.eq(tblBbs.getBbsSn())))
                    .groupBy(qTblMenu.bbsSn).fetchFirst();
            }
        }catch (NullPointerException e) {
            log.error(ResponseCode.SELECT_ERROR.getMessage());
        }

        return authrtDto;
    }

    public ResultVO getBbsInPstList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        try {
            QTblBbs qTblBbs = QTblBbs.tblBbs;
            QTblPst qTblPst = QTblPst.tblPst;
            QTblMenu qTblMenu = QTblMenu.tblMenu;
            QTblAuthrtGroupMenu qTblAuthrtGroupMenu = QTblAuthrtGroupMenu.tblAuthrtGroupMenu;

            JPAQueryFactory q = new JPAQueryFactory(em);
            BooleanBuilder builder = new BooleanBuilder();
            BooleanBuilder subBuilder = new BooleanBuilder();
            if (!StringUtils.isEmpty(dto.get("bbsNm"))) {
                builder.and(qTblBbs.bbsNm.contains((String) dto.get("bbsNm")));
            }
            if (!StringUtils.isEmpty(dto.get("bbsTypeNm"))) {
                builder.and(qTblBbs.bbsTypeNm.eq((String) dto.get("bbsTypeNm")));
            }
            if (!StringUtils.isEmpty(dto.get("actvtnYn"))) {
                builder.and(qTblBbs.actvtnYn.eq((String) dto.get("actvtnYn")));
                subBuilder.and(qTblPst.actvtnYn.eq("Y"));
            }
            if (!StringUtils.isEmpty(dto.get("selectDt"))) {
                subBuilder.and(
                    Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m')", dto.get("selectDt")).between(
                            Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m')", qTblPst.ntcBgngDt),
                            Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m')", qTblPst.ntcEndDate)
                    )
                );
            }

            String menuAuthrtGroups = "1";
            if(!StringUtils.isEmpty(dto.get("userSn"))){
                menuAuthrtGroups = menuAuthGroupApiService.getMenuAuthrtGroups(Long.parseLong(String.valueOf(dto.get("userSn"))));
            }
            builder.and(Expressions.booleanTemplate("FIND_IN_SET({0}, {1}) > 0", qTblAuthrtGroupMenu.authrtGroupSn, menuAuthrtGroups));


            List<TblBbsInPstDTO> bbsList = q.select(
                    Projections.constructor(
                            TblBbsInPstDTO.class,
                            qTblBbs.bbsSn,
                            qTblBbs.bbsNm,
                            qTblBbs.bbsTypeNm,
                            qTblBbs.atchFileYn,
                            qTblBbs.atchFileKndNm,
                            qTblBbs.wrtrRlsYn,
                            qTblBbs.cmntPsbltyYn,
                            qTblBbs.ansPsbltyYn,
                            qTblBbs.pstCtgryYn,
                            qTblBbs.rmrkCn,
                            qTblBbs.actvtnYn,
                            qTblBbs.creatrSn,
                            qTblBbs.frstCrtDt,
                            Expressions.constant(Collections.emptyList())
                    )
                ).from(qTblBbs)
                .join(qTblMenu).on(qTblBbs.bbsSn.eq(qTblMenu.bbsSn))
                .join(qTblAuthrtGroupMenu).on(qTblMenu.menuSn.eq(qTblAuthrtGroupMenu.menuSn))
                .where(builder).fetch();

            subBuilder.and(
                    qTblPst.bbsSn.in(
                            bbsList.stream().map(
                                    TblBbsInPstDTO::getBbsSn
                            ).collect(Collectors.toList())
                    )
            );

            Map<Long, List<TblPstSimpleDTO>> pstList = q.select(
                        Projections.constructor(
                            TblPstSimpleDTO.class,
                            qTblPst.pstSn,
                            qTblPst.ntcBgngDt,
                            qTblPst.ntcEndDate,
                            qTblPst.bbsSn,
                            qTblPst.pstTtl,
                            qTblPst.pstCn,
                            qTblPst.rlsYn,
                            qTblPst.actvtnYn,
                            qTblPst.frstCrtDt
                        )
                    ).from(qTblPst)
                    .where(subBuilder).orderBy(qTblPst.frstCrtDt.desc())
                    .limit(20)
                    .fetch().stream()
                    .collect(Collectors.groupingBy(TblPstSimpleDTO::getBbsSn));

            bbsList.forEach( bbs ->
                    bbs.setTblPstList(
                            pstList.getOrDefault(
                                    bbs.getBbsSn(),
                                    new ArrayList<>()
                            )
                    )
            );

            resultVO.putResult("bbsList", bbsList);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (NullPointerException e) {
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }
        return resultVO;
    }
}
