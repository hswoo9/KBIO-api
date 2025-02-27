package egovframework.com.devjitsu.repository.user;

import org.springframework.transaction.annotation.Transactional;
import egovframework.com.devjitsu.model.user.TblUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface TblUserRepository extends JpaRepository<TblUser, String> {

    TblUser findByUserSn(long userSn);

    @Modifying
    @Query("UPDATE TblUser U SET U.lgnFailNmtm = :lgnFailNmtm WHERE U.userSn = :userSn")
    void setLgnFailNmtmUpd(@Param("userSn") long userSn, @Param("lgnFailNmtm") long lgnFailNmtm);

    @Modifying
    @Query("UPDATE TblUser U SET U.mbrStts = 'C' WHERE U.userSn = :userSn")
    void setSuspensionOfUseUpd(@Param("userSn") long userSn);
}
