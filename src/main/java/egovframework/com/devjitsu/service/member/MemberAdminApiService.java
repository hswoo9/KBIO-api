package egovframework.com.devjitsu.service.member;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.bbs.QTblPst;
import egovframework.com.devjitsu.model.bbs.TblBbs;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.login.QLettnemplyrinfoVO;
import egovframework.com.devjitsu.model.login.LettnemplyrinfoVO;
import egovframework.com.devjitsu.repository.member.TblMemberRepository;
import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberAdminApiService {

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    private final EntityManager em;

    /**
     * jpa 부등호
     * gt : >
     * lt : <
     * goe : >=
     * loe : <=
     */

    private final TblMemberRepository tblMemberRepository;

    public ResultVO getNormalMemberList(SearchDto dto) {
        ResultVO resultVO = new ResultVO();
        PaginationInfo paginationInfo = new PaginationInfo();

        try {
            if (!StringUtils.isEmpty(dto.get("pageIndex"))) {
                paginationInfo.setCurrentPageNo(Integer.parseInt(dto.get("pageIndex").toString()));
            }
            paginationInfo.setRecordCountPerPage(propertyService.getInt("Globals.pageUnit"));
            paginationInfo.setPageSize(propertyService.getInt("Globals.pageSize"));

            JPAQueryFactory q = new JPAQueryFactory(em);
            QLettnemplyrinfoVO qLettnemplyrinfoVO = QLettnemplyrinfoVO.lettnemplyrinfoVO;

            BooleanBuilder builder = new BooleanBuilder();
            if (!StringUtils.isEmpty(dto.get("userSn"))) {
                builder.and(qLettnemplyrinfoVO.userSn.eq(Long.valueOf((String) dto.get("userSn"))));
            }
            if (!StringUtils.isEmpty(dto.get("userNm"))) {
                builder.and(qLettnemplyrinfoVO.userNm.eq((String) dto.get("userNm")));
            }

            List<LettnemplyrinfoVO> getNormalMemberList = q.selectFrom(qLettnemplyrinfoVO)
                    .where(builder)
                    .orderBy(qLettnemplyrinfoVO.userSn.desc())
                    .offset(paginationInfo.getFirstRecordIndex())
                    .limit(paginationInfo.getRecordCountPerPage())
                    .fetch();

            Long totCnt = q.select(qLettnemplyrinfoVO.count())
                    .from(qLettnemplyrinfoVO)
                    .where(builder)
                    .fetchOne();
            if (totCnt == null) totCnt = 0L;
            paginationInfo.setTotalRecordCount(totCnt.intValue());

            // Set response data
            resultVO.putResult("getNormalMemberList", getNormalMemberList);
            resultVO.putPaginationInfo(paginationInfo);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getNormalMember(LettnemplyrinfoVO lettnemplyrinfoVO) {
        ResultVO resultVO = new ResultVO();

        try {
            resultVO.putResult("member", tblMemberRepository.findByUserSn(lettnemplyrinfoVO.getUserSn()));
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;

    }

    public ResultVO setNormalMember(LettnemplyrinfoVO lettnemplyrinfoVO) {
        ResultVO resultVO = new ResultVO();

        try {
            tblMemberRepository.save(lettnemplyrinfoVO);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
        }

        return resultVO;
    }

    /*public ResultVO setNormalMemberDel(LettnemplyrinfoVO lettnemplyrinfoVO) {
        ResultVO resultVO = new ResultVO();

        try {
            QTblPst tblPst = QTblPst.tblPst;
            JPAQueryFactory q = new JPAQueryFactory(em);
            q.delete(tblPst).where(lettnemplyrinfoVO.userSn.eq(lettnemplyrinfoVO.getUserSn())).execute();
            tblBbsRepository.delete(lettnemplyrinfoVO);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }

        return resultVO;
    }*/


}
