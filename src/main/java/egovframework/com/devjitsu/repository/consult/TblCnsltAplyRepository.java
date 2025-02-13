package egovframework.com.devjitsu.repository.consult;

import egovframework.com.devjitsu.model.consult.TblCnsltAply;
import egovframework.com.devjitsu.model.user.TblMvnEnt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TblCnsltAplyRepository extends JpaRepository<TblCnsltAply, String> {

    TblCnsltAply findByCnsltAplySn(long findByCnsltAplySn);
}
