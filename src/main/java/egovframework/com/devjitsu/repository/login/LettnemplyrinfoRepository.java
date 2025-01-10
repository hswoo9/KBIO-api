package egovframework.com.devjitsu.repository.login;

import egovframework.com.devjitsu.model.consult.TblCnsltDtl;
import egovframework.com.devjitsu.model.login.LettnemplyrinfoVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LettnemplyrinfoRepository extends JpaRepository<LettnemplyrinfoVO, String> {

}
