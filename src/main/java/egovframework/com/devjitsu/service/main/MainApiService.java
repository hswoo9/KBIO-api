package egovframework.com.devjitsu.service.main;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.bannerPopup.BannerPopupDto;
import egovframework.com.devjitsu.model.bannerPopup.QTblBnrPopup;
import egovframework.com.devjitsu.model.bannerPopup.TblBnrPopup;
import egovframework.com.devjitsu.model.common.QTblComFile;
import egovframework.com.devjitsu.model.common.SearchDto;
import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MainApiService {

    private final EntityManager em;

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
     * query DSL 조건 추가하는 방법
     * BooleanBuilder builder = new BooleanBuilder();
     * builder.and(qTblComCdGroup.actvtnYn.eq("Y"));
     */

    public ResultVO getBnrPopupList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();

        try {
            JPAQueryFactory q = new JPAQueryFactory(em);
            QTblBnrPopup qTblBnrPopup = QTblBnrPopup.tblBnrPopup;
            QTblComFile qTblComFile = QTblComFile.tblComFile;

            BooleanBuilder builder = new BooleanBuilder();
            builder.and(qTblBnrPopup.actvtnYn.eq("Y"));
            if(!StringUtils.isEmpty(dto.get("bnrPopupKnd"))){
                builder.and(qTblBnrPopup.bnrPopupKnd.eq((String) dto.get("bnrPopupKnd")));

                if(dto.get("bnrPopupKnd").equals("popup")){
                    builder.and(qTblBnrPopup.popupBgngDt.loe(LocalDateTime.now()).and(qTblBnrPopup.popupEndDt.goe(LocalDateTime.now())));
                }
            }

            List<BannerPopupDto> bnrPopupList = q
                    .select(
                        Projections.constructor(
                            BannerPopupDto.class,
                            qTblBnrPopup,
                            qTblComFile
                        )
                    ).from(qTblBnrPopup)
                    .join(qTblComFile)
                    .on(
                        qTblComFile.psnTblSn.eq(
                            Expressions.stringTemplate("CONCAT('bnrPopup_', {0})", qTblBnrPopup.bnrPopupSn)
                        )
                    )
                    .where(builder)
                    .fetch();

            resultVO.putResult("bnrPopupList", bnrPopupList);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }
}
