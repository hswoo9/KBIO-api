package egovframework.com.devjitsu.service.user;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;

import egovframework.com.devjitsu.model.user.TblMvnEnt;
import egovframework.com.devjitsu.model.user.QTblMvnEnt;

import egovframework.com.devjitsu.repository.user.TblMvnEntRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MvnEntApiService {

    private final EntityManager em;
    private final TblMvnEntRepository tblMvnEntRepository;

    public ResultVO setMvnEnt(TblMvnEnt tblMvnEnt){
        ResultVO resultVO = new ResultVO();

        try{
            TblMvnEnt mvnEnt = new TblMvnEnt();

            mvnEnt.setBrno(tblMvnEnt.getBrno());
            mvnEnt.setMvnEntNm(tblMvnEnt.getMvnEntNm());
            mvnEnt.setRpsvNm(tblMvnEnt.getRpsvNm());
            mvnEnt.setZip(tblMvnEnt.getZip());
            mvnEnt.setEntAddr(tblMvnEnt.getEntAddr());
            mvnEnt.setEntDaddr(tblMvnEnt.getEntDaddr());
            mvnEnt.setClsNm(tblMvnEnt.getClsNm());
            mvnEnt.setEntTelno(tblMvnEnt.getEntTelno());
            //mvnEnt.setFrstCrtDt(tblMvnEnt.getFrstCrtDt());

            System.out.println("***mvnEnt*** : "+mvnEnt);

            QTblMvnEnt qTblMvnEnt = QTblMvnEnt.tblMvnEnt;
            JPAQueryFactory q = new JPAQueryFactory(em);

            tblMvnEntRepository.save(mvnEnt);

            resultVO.setResultCode(ResponseCode.SUCCESS.getCode());
        }catch (Exception e){
            e.printStackTrace();
            resultVO.setResultCode(ResponseCode.SAVE_ERROR.getCode());
        }

        return resultVO;
    }

}
