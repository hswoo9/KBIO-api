package egovframework.com.devjitsu.repository.code;

import egovframework.com.devjitsu.model.common.TblComFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TblComFileRepository extends JpaRepository<TblComFile, String> {

}
