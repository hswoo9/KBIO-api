package egovframework.com.devjitsu.service.consult;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
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
import egovframework.com.devjitsu.model.consult.ConsultDto;
import egovframework.com.devjitsu.model.consult.TblCnsltAply;
import egovframework.com.devjitsu.model.consult.TblCnsltDtl;
import egovframework.com.devjitsu.model.consult.TblDfclMttr;
import egovframework.com.devjitsu.model.menu.MenuDto;
import egovframework.com.devjitsu.model.menu.QTblAuthrtGroupMenu;
import egovframework.com.devjitsu.model.menu.QTblMenu;
import egovframework.com.devjitsu.model.menu.TblMenu;
import egovframework.com.devjitsu.model.user.QTblCnslttMbr;
import egovframework.com.devjitsu.model.user.QTblUser;
import egovframework.com.devjitsu.model.user.TblCnslttMbr;
import egovframework.com.devjitsu.model.user.TblUser;
import egovframework.com.devjitsu.repository.code.TblComCdGroupRepository;
import egovframework.com.devjitsu.repository.code.TblComCdRepository;
import egovframework.com.devjitsu.repository.common.TblComFileRepository;
import egovframework.com.devjitsu.repository.consult.TblCnsltAplyRepository;
import egovframework.com.devjitsu.repository.consult.TblCnsltDtlRepository;
import egovframework.com.devjitsu.repository.consult.TblCnslttMbrRepository;
import egovframework.com.devjitsu.repository.consult.TblDfclMttrRepository;
import egovframework.com.devjitsu.repository.menu.TblMenuAuthrtGroupRepository;
import egovframework.com.devjitsu.repository.menu.TblMenuRepository;
import egovframework.com.devjitsu.repository.user.TblUserRepository;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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


    private final TblCnsltAplyRepository tblCnsltAplyRepository;
    private final TblDfclMttrRepository tblDfclMttrRepository;
    private final TblComFileRepository tblComFileRepository;
    private final TblUserRepository tblUserRepository;
    private final TblCnslttMbrRepository tblCnslttMbrRepository;
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

    public ResultVO getConsultantList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();
        Map<String, Object> conditions = new HashMap<>();

        try {
            if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
                paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
            }

            paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));

            QTblUser qTblUser = QTblUser.tblUser;
            QTblCnslttMbr qTblCnslttMbr = QTblCnslttMbr.tblCnslttMbr;
            QTblComFile qTblComFile = QTblComFile.tblComFile;
            JPAQueryFactory q = new JPAQueryFactory(em);

            /** query DSL 조건 추가하는 방법 */
            BooleanBuilder builder = new BooleanBuilder();
            if (!StringUtils.isEmpty(dto.get("kornFlnm"))) {
                builder.and(qTblUser.kornFlnm.contains((String) dto.get("kornFlnm")));
            }
            if (!StringUtils.isEmpty(dto.get("ogdpNm"))) { //소속 검색조건
                builder.and(qTblCnslttMbr.ogdpNm.eq((String) dto.get("ogdpNm")));
            }

            List<ConsultDto> consultantList = q
                    .select(
                            Projections.constructor(
                                ConsultDto.class,
                                qTblCnslttMbr,
                                qTblUser
//                                ,qTblComFile
                            )
                    ).from(qTblUser)
                    .join(qTblCnslttMbr)
                    .on(
                            qTblUser.userSn.eq(qTblCnslttMbr.userSn)
                    )
/*                    .join(qTblComFile)
                    .on(
                            qTblComFile.psnTblSn.eq(
                                    Expressions.stringTemplate("CONCAT()") //사진
                            )
                    )*/
                    .where(builder)
                    .orderBy(qTblUser.frstCrtDt.desc())
                    .offset(paginationInfo.getFirstRecordIndex())
                    .limit(paginationInfo.getRecordCountPerPage())
                    .fetch();


            /*Long totCnt = q.select(qTblUser.count())
                    .join(qTblCnslttMbr).on(qTblUser.userSn.eq(qTblCnslttMbr.userSn)) //컨설턴트
//                    .join().on() 컨설턴트 사진
                    .from(qTblUser)
                    .where(builder)
                    .fetchOne();*/

            Long totCnt = q.select(qTblUser.count())
                    .from(qTblUser)
                    .join(qTblCnslttMbr).on(qTblUser.userSn.eq(qTblCnslttMbr.userSn))
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

    public ResultVO getConsultantDetail(SearchDto dto){
        ResultVO resultVO = new ResultVO();

        try{
            TblUser tblUser = tblUserRepository.findByUserSn(Long.parseLong(dto.get("userSn").toString()));
            TblCnslttMbr tblCnslttMbr = tblCnslttMbrRepository.findByUserSn(Long.parseLong(dto.get("userSn").toString()));



            resultVO.putResult("memberDetail",tblUser);
            resultVO.putResult("consultant",tblCnslttMbr);

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setConsulting(TblCnsltAply tblCnsltAply, TblCnsltDtl tblCnsltDtl, List<MultipartFile> files) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblComFile qTblComFile = QTblComFile.tblComFile;
            JPAQueryFactory q = new JPAQueryFactory(em);

            tblCnsltAplyRepository.save(tblCnsltAply);
            if(files != null){
                long fileCnt = q.selectFrom(qTblComFile).where(qTblComFile.psnTblSn.eq("consulting_" + tblCnsltAply.getCnsltAplySn())).fetchCount();
                tblComFileRepository.saveAll(
                    fileUtil.devFileInf(
                            files,
                            "/consulting/" + tblCnsltAply.getCnsltAplySn(),
                            "consulting_" + tblCnsltAply.getCnsltAplySn(),
                            fileCnt
                    )
                );
            }

            tblCnsltDtl.setCnsltAplySn(tblCnsltAply.getCnsltAplySn());
            if(!StringUtils.isEmpty(tblCnsltDtl.getCnslttUserSn())){
                tblCnsltDtl.setCnslttDsgnDt(LocalDateTime.now());
            }
            tblCnsltDtlRepository.save(tblCnsltDtl);

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO setDfclMttr(TblDfclMttr tblDfclMttr, List<MultipartFile> files) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblComFile qTblComFile = QTblComFile.tblComFile;
            JPAQueryFactory q = new JPAQueryFactory(em);

            tblDfclMttrRepository.save(tblDfclMttr);
            if(files != null){
                long fileCnt = q.selectFrom(qTblComFile).where(qTblComFile.psnTblSn.eq("dfclMttr_" + tblDfclMttr.getDfclMttrSn())).fetchCount();
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
}
