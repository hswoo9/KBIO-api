package egovframework.com.devjitsu.repository.common;

import egovframework.com.devjitsu.model.common.TblAtchFileDwnldCnt;
import egovframework.com.devjitsu.model.common.TblComFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TblAtchFileDwnldCntRepository extends JpaRepository<TblAtchFileDwnldCnt, String> {
    TblAtchFileDwnldCnt findByTrgtTblNm(String trgtTblNm);
    TblAtchFileDwnldCnt findByTrgtTblNmAndTrgtSn(String trgtTblNm, Long trgtSn);
}
