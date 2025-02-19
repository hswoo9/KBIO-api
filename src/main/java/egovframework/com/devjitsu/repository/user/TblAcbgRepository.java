package egovframework.com.devjitsu.repository.user;

import egovframework.com.devjitsu.model.user.TblAcbg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TblAcbgRepository extends JpaRepository<TblAcbg, String> {

    TblAcbg findByAcbgSn(long acbgSn);

    List<TblAcbg> findAllByUserSn(long userSn);

    TblAcbg findByUserSn(long userSn);

}
