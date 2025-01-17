package egovframework.com.devjitsu.repository.code;

import egovframework.com.devjitsu.model.common.TblComCd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TblComCdRepository extends JpaRepository<TblComCd, String> {

    TblComCd findByComCdSn(Long comCdSn);

}
