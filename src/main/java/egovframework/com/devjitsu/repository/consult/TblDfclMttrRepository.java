package egovframework.com.devjitsu.repository.consult;

import egovframework.com.devjitsu.model.consult.TblCnsltAply;
import egovframework.com.devjitsu.model.consult.TblDfclMttr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TblDfclMttrRepository extends JpaRepository<TblDfclMttr, String> {

}
