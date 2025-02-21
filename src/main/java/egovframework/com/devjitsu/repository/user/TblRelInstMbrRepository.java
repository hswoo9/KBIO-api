package egovframework.com.devjitsu.repository.user;

import egovframework.com.devjitsu.model.user.TblRelInstMbr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TblRelInstMbrRepository extends JpaRepository<TblRelInstMbr, String> {

    List<TblRelInstMbr> findUserSnByRelInstSn(long relInstSn);

     List<TblRelInstMbr> findByUserSn(long userSn);
}
