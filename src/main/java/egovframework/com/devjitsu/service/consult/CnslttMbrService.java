package egovframework.com.devjitsu.service.consult;

import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;
import egovframework.com.devjitsu.model.common.SearchDto;
import egovframework.com.devjitsu.model.consult.TblCnslttMbr;
import egovframework.com.devjitsu.repository.consult.TblCnslttMbrRepository;
import egovframework.com.devjitsu.repository.user.TblUserRepository;
import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

@Service
@RequiredArgsConstructor
@Transactional
public class CnslttMbrService {

    private final EntityManager em;
    private final TblCnslttMbrRepository tblCnslttMbrRepository;
    private final TblUserRepository tblUserRepository;

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    public ResultVO setCnslttMbr(TblCnslttMbr tblCnslttMbr){
        ResultVO resultVO = new ResultVO();

        try{

            tblCnslttMbrRepository.save(tblCnslttMbr);
            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());

        } catch (Exception e) {

            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());

        }

        return resultVO;
    }

    public ResultVO getCnslttMbrList(SearchDto searchDto){
        ResultVO resultVO = new ResultVO();

        try {

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {

            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }

    public ResultVO getCnslttMbr(TblCnslttMbr tblCnslttMbr){
        ResultVO resultVO = new ResultVO();

        try {

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {

            resultVO.setResultCode(ResponseCode.SELECT_ERROR.getCode());
        }

        return resultVO;
    }



}
