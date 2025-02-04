package egovframework.com.devjitsu.repository.common;

import egovframework.com.devjitsu.model.common.TblComFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TblComFileRepository extends JpaRepository<TblComFile, String> {
    TblComFile findByAtchFileSn(Long atchFileSn);
    List<TblComFile> findAllByPsnTblPk(String psnTblPk);
    TblComFile findByPsnTblPk(String psnTblPk);
}
