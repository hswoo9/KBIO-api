package egovframework.com.devjitsu.repository.user;

import egovframework.com.devjitsu.model.user.TblMvnEnt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TblMvnEntRepository extends JpaRepository<TblMvnEnt, String>{

}
