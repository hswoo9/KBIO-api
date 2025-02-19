package egovframework.com.devjitsu.repository.user;

import egovframework.com.devjitsu.model.user.TblAcbg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TblAcbgRepository extends JpaRepository<TblAcbg, String> {
}
