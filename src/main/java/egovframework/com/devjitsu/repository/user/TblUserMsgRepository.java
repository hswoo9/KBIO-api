package egovframework.com.devjitsu.repository.user;

import egovframework.com.devjitsu.model.user.TblUser;
import egovframework.com.devjitsu.model.user.TblUserMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface TblUserMsgRepository extends JpaRepository<TblUserMsg, String> {

    @Query("SELECT COALESCE(MAX(t.msgGroup), 0) + 1 FROM TblUserMsg t")
    long findNextMsgGroup();
}
