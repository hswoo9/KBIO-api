package egovframework.com.devjitsu.repository.bbs;

import egovframework.com.devjitsu.model.bbs.TblBbs;
import egovframework.com.devjitsu.model.bbs.TblPst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TblPstRepository extends JpaRepository<TblPst, String> {

    TblPst findByPstSn(long pstSn);
}
