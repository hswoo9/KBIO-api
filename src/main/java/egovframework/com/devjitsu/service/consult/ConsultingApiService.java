package egovframework.com.devjitsu.service.consult;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.bbs.QTblBbs;
import egovframework.com.devjitsu.model.bbs.QTblPst;
import egovframework.com.devjitsu.model.bbs.TblBbs;
import egovframework.com.devjitsu.model.bbs.TblPst;
import egovframework.com.devjitsu.model.common.QTblComCdGroup;
import egovframework.com.devjitsu.model.common.QTblComFile;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.common.TblComFile;
import egovframework.com.devjitsu.model.consult.TblCnsltAply;
import egovframework.com.devjitsu.model.menu.MenuDto;
import egovframework.com.devjitsu.model.menu.QTblAuthrtGroupMenu;
import egovframework.com.devjitsu.model.menu.QTblMenu;
import egovframework.com.devjitsu.model.menu.TblMenu;
import egovframework.com.devjitsu.model.user.QTblUser;
import egovframework.com.devjitsu.model.user.TblUser;
import egovframework.com.devjitsu.repository.code.TblComCdGroupRepository;
import egovframework.com.devjitsu.repository.code.TblComCdRepository;
import egovframework.com.devjitsu.repository.common.TblComFileRepository;
import egovframework.com.devjitsu.repository.menu.TblMenuAuthrtGroupRepository;
import egovframework.com.devjitsu.repository.menu.TblMenuRepository;
import egovframework.com.devjitsu.service.access.MngrAcsIpApiService;
import egovframework.com.devjitsu.service.common.RedisApiService;
import egovframework.com.devjitsu.service.menu.MenuAuthGroupApiService;
import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
@Transactional
public class ConsultingApiService {

    private final EntityManager em;

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
     * query DSL 조건 추가하는 방법
     * BooleanBuilder builder = new BooleanBuilder();
     * builder.and(qTblComCdGroup.actvtnYn.eq("Y"));
     */

    public ResultVO getConsultantList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();

        try {
            if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
                paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
            }

            paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));

            QTblUser qTblUser = QTblUser.tblUser;
            JPAQueryFactory q = new JPAQueryFactory(em);

            /** query DSL 조건 추가하는 방법 */
            BooleanBuilder builder = new BooleanBuilder();
            if (!StringUtils.isEmpty(dto.get("kornFlnm"))) {
                builder.and(qTblUser.kornFlnm.contains((String) dto.get("kornFlnm")));
            }
//            if (!StringUtils.isEmpty(dto.get("ogdpNm"))) { //소속 검색조건
//                builder.and(qTblUser.ogdpNm.eq((String) dto.get("ogdpNm")));
//            }

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

    public ResultVO setConsulting(TblCnsltAply tblCnsltAply, List<MultipartFile> files) {
        ResultVO resultVO = new ResultVO();

        try {

            QTblPst qTblPst = QTblPst.tblPst;
            QTblComFile qTblComFile = QTblComFile.tblComFile;

            JPAQueryFactory q = new JPAQueryFactory(em);

//            if(!StringUtils.isEmpty(tblPst.getUpPstSn())) {
//                TblPst orgnlPst = q.selectFrom(qTblPst).where(qTblPst.pstSn.eq(tblPst.getUpPstSn())).fetchOne();
//                if(orgnlPst.getRlsYn().equals("Y")) {
//                    tblPst.setRlsYn(orgnlPst.getRlsYn());
//                    tblPst.setPrvtPswd(orgnlPst.getPrvtPswd());
//                }
//
//                if(StringUtils.isEmpty(tblPst.getAnsStp())){
//                    NumberPath<Integer> maxAnsStp = qTblPst.ansStp;
//                    JPAQuery<Integer> query = q
//                            .select(Expressions.numberTemplate(Integer.class, "COALESCE(MAX({0}), 0) + 1", maxAnsStp))
//                            .from(qTblPst)
//                            .where(qTblPst.bbsSn.eq(tblPst.getBbsSn())
//                                    .and(qTblPst.pstGroup.eq(tblPst.getPstGroup())));
//                    Integer nextAnsStp = query.fetchOne();
//                    tblPst.setAnsStp(nextAnsStp);
//                }
//            }
//
//            if(StringUtils.isEmpty(tblPst.getPstGroup())){
//                /** 등록 */
//                NumberPath<Long> maxArticleGroup = qTblPst.pstGroup;
//                JPAQuery<Integer> group  = q
//                        .select(Expressions.numberTemplate(Integer.class, "COALESCE(MAX({0}), 0) + 1", maxArticleGroup))
//                        .from(qTblPst)
//                        .where(qTblPst.bbsSn.eq(tblPst.getBbsSn()));
//                tblPst.setPstGroup(Long.valueOf(group.fetchOne()));
//            }
//
//            tblPstRepository.save(tblPst);
//            if(files != null){
//                long fileCnt = q.selectFrom(qTblComFile).where(qTblComFile.psnTblSn.eq("pst_" + tblPst.getPstSn())).fetchCount();
//                tblComFileRepository.saveAll(
//                        fileUtil.devFileInf(
//                                files,
//                                "/bbs/" + tblPst.getBbsSn() + "/pst/" + tblPst.getPstSn(),
//                                "pst_" + tblPst.getPstSn(),
//                                fileCnt
//                        )
//                );
//            }

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }

        return resultVO;
    }
}
