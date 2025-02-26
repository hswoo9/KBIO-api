package egovframework.com.devjitsu.repository.common;

import egovframework.com.devjitsu.model.common.TblAtchFileDwnldCnt;
import egovframework.com.devjitsu.model.common.TblPstCntnHstry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TblPstCntnHstryRepository extends JpaRepository<TblPstCntnHstry, String> {
    TblPstCntnHstry findByTrgtTblNm(String trgtTblNm);
    TblPstCntnHstry findByTrgtTblNmAndTrgtSn(String trgtTblNm, Long trgtSn);
}
