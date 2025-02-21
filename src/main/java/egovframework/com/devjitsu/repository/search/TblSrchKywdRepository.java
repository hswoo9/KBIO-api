package egovframework.com.devjitsu.repository.search;

import egovframework.com.devjitsu.model.login.LettnemplyrinfoVO;
import egovframework.com.devjitsu.model.search.TblSrchKywd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TblSrchKywdRepository extends JpaRepository<TblSrchKywd, String> {

    TblSrchKywd findByKywd(String kywd);
}
