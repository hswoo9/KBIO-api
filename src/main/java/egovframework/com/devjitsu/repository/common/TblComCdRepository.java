package egovframework.com.devjitsu.repository.common;

import egovframework.com.devjitsu.model.common.TblComCd;
import egovframework.com.devjitsu.model.common.TblComFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TblComCdRepository extends JpaRepository<TblComCd, String> {

}
