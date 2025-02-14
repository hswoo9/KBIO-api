package egovframework.com.devjitsu.repository.consult;

import egovframework.com.devjitsu.model.consult.TblCnsltAply;
import egovframework.com.devjitsu.model.consult.TblCnsltDgstfn;
import egovframework.com.devjitsu.model.login.LettnemplyrinfoVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TblCnsltDgstfnRepository extends JpaRepository<TblCnsltDgstfn, String> {

    List<TblCnsltDgstfn> findByCnsltAplySn(Long cnsltAplySn);
}
