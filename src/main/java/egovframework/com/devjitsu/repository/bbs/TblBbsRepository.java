package egovframework.com.devjitsu.repository.bbs;

import egovframework.com.devjitsu.model.bbs.TblBbs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TblBbsRepository extends JpaRepository<TblBbs, String> {

}
