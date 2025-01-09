package egovframework.com.devjitsu.repository.menu;

import egovframework.com.devjitsu.model.menu.TblMenuAuthrtGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TblMenuAuthrtGroupRepository extends JpaRepository<TblMenuAuthrtGroup, String> {

}
