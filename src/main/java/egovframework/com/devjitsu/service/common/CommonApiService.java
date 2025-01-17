package egovframework.com.devjitsu.service.common;

import com.querydsl.core.BooleanBuilder;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.QTblComCdGroup;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.common.TblComFile;
import egovframework.com.devjitsu.repository.code.TblComCdGroupRepository;
import egovframework.com.devjitsu.repository.code.TblComCdRepository;
import egovframework.com.devjitsu.repository.common.TblComFileRepository;
import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
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

        resultVO.putResult("rs", q.selectFrom(qTblComCdGroup).where(builder).orderBy(qTblComCdGroup.frstCrtDt.desc()).fetch());
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

    public ResultVO setFileDel(TblComFile tblComFile) {
        ResultVO resultVO = new ResultVO();

        try {
            boolean isDelete = fileUtil.deleteFile(new String[]{tblComFile.getStrgFileNm()}, tblComFile.getAtchFilePathNm());
            if (isDelete) {
                tblComFileRepository.delete(tblComFile);
            } else {
                throw new Exception();
            }
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }

        return resultVO;
    }
}
