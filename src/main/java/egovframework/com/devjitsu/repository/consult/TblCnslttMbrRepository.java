package egovframework.com.devjitsu.repository.consult;

import egovframework.com.devjitsu.model.consult.TblCnslttMbr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TblCnslttMbrRepository extends JpaRepository<TblCnslttMbr, String> {
    TblCnslttMbr findByUserSn(long userSn);
}
