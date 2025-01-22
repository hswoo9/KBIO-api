package egovframework.com.devjitsu.repository.bbs;

import egovframework.com.devjitsu.model.bbs.TblPst;
import egovframework.com.devjitsu.model.bbs.TblPstCmnt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TblPstCmntRepository extends JpaRepository<TblPstCmnt, String> {

}
