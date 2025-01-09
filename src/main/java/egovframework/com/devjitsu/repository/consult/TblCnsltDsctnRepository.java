package egovframework.com.devjitsu.repository.consult;

import egovframework.com.devjitsu.model.consult.TblCnsltDgstfn;
import egovframework.com.devjitsu.model.consult.TblCnsltDsctn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TblCnsltDsctnRepository extends JpaRepository<TblCnsltDsctn, String> {

}
