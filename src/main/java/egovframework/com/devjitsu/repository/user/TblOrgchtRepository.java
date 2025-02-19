package egovframework.com.devjitsu.repository.user;

import egovframework.com.devjitsu.model.user.TblOrgcht;
import egovframework.com.devjitsu.model.user.TblUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TblOrgchtRepository extends JpaRepository<TblOrgcht, String> {

    TblOrgcht findByOrgchtSn(Long orgchtSn);
}
