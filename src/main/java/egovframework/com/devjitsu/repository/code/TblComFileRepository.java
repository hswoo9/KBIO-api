package egovframework.com.devjitsu.repository.code;

import egovframework.com.devjitsu.model.common.TblComFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TblComFileRepository extends JpaRepository<TblComFile, String> {
    List<TblComFile> findByPsnTblPk(String psnTblPk);
}
