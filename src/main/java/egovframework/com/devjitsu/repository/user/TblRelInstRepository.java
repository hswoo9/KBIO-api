package egovframework.com.devjitsu.repository.user;

import egovframework.com.devjitsu.model.user.TblMvnEnt;
import egovframework.com.devjitsu.model.user.TblRelInst;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TblRelInstRepository extends JpaRepository<TblRelInst, String> {
}
