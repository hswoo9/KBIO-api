package egovframework.com.devjitsu.repository.member;

import egovframework.com.devjitsu.model.login.LettnemplyrinfoVO;
import egovframework.com.devjitsu.model.menu.TblMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TblMemberRepository extends JpaRepository<LettnemplyrinfoVO, String> {

    LettnemplyrinfoVO findByUserSn(long userSn);
}
