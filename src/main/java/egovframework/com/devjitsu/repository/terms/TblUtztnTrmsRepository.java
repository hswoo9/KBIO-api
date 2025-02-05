package egovframework.com.devjitsu.repository.terms;

import egovframework.com.devjitsu.model.terms.TblUtztnTrms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TblUtztnTrmsRepository extends JpaRepository<TblUtztnTrms, String> {

}
