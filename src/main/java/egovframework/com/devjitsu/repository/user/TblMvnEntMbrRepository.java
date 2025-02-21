package egovframework.com.devjitsu.repository.user;

import egovframework.com.devjitsu.model.user.TblMvnEntMbr;
import egovframework.com.devjitsu.model.user.TblRelInstMbr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TblMvnEntMbrRepository extends JpaRepository<TblMvnEntMbr, String> {

    List<TblMvnEntMbr> findUserSnByMvnEntSn(long mvnEntSn);

    List<TblMvnEntMbr> findByUserSn(long userSn);
}
