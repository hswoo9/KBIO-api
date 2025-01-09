package egovframework.com.devjitsu.repository.consult;

import egovframework.com.devjitsu.model.consult.TblCnsltAply;
import egovframework.com.devjitsu.model.consult.TblCnsltDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TblCnsltDtlRepository extends JpaRepository<TblCnsltDtl, String> {

}
