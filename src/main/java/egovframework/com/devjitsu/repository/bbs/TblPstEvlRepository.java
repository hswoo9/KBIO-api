package egovframework.com.devjitsu.repository.bbs;

import egovframework.com.devjitsu.model.bbs.TblPst;
import egovframework.com.devjitsu.model.bbs.TblPstEvl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TblPstEvlRepository extends JpaRepository<TblPstEvl, String> {

}
