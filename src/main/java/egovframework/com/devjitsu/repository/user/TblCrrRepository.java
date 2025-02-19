package egovframework.com.devjitsu.repository.user;

import egovframework.com.devjitsu.model.user.TblCrr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TblCrrRepository extends JpaRepository<TblCrr, String> {

    TblCrr findByCrrSn(long crrSn);

    List<TblCrr> findAllByUserSn(long userSn);

    TblCrr findByUserSn(long userSn);

}
