package egovframework.com.devjitsu.service.terms;

import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.terms.QTblUtztnTrms;
import egovframework.com.devjitsu.model.terms.TblUtztnTrms;
import egovframework.com.devjitsu.repository.terms.TblUtztnTrmsRepository;
import egovframework.com.devjitsu.service.common.RedisApiService;
import egovframework.com.jwt.EgovJwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;


@Service
@RequiredArgsConstructor
@Transactional
public class UtztnTrmsApiService {

    @Autowired
    private EgovJwtTokenUtil jwtTokenUtil;

    @Autowired
    private RedisApiService redisApiService;

    private final EntityManager em;
    private final TblUtztnTrmsRepository TblUtztnTrmsRepository;

    public ResultVO setPrivacyPilicy(TblUtztnTrms tblUtztnTrms) {
        ResultVO resultVO = new ResultVO();

        try {
            tblUtztnTrms.setUtztnTrmsKnd("1");

            TblUtztnTrmsRepository.save(tblUtztnTrms);

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
            resultVO.setResultMessage("개인정보처리 방침이 성공적으로 완료되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.DELETE_ERROR.getCode());
        }

        return resultVO;
    }

}
