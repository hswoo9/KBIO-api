package egovframework.com.devjitsu.repository.user;

import egovframework.com.devjitsu.model.user.TblMvnEntMbr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TblMvnEntMbrRepository extends JpaRepository<TblMvnEntMbr, String> {

    List<TblMvnEntMbr> findUserSnByMvnEntSn(long mvnEntSn);
}
