package egovframework.com.devjitsu.repository.user;

import egovframework.com.devjitsu.model.login.LettnemplyrinfoVO;
import egovframework.com.devjitsu.model.user.TblUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TblUserRepository extends JpaRepository<TblUser, String> {

    TblUser findByUserSn(long userSn);
}
