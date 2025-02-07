package egovframework.com.devjitsu.repository.user;

import egovframework.com.devjitsu.model.user.TblMvnEntMbr;
import egovframework.com.devjitsu.model.user.TblUserSnsCertInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TblUserSnsCertInfoRepository extends JpaRepository<TblUserSnsCertInfo, String> {

}
