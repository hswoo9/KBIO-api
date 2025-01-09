package egovframework.com.devjitsu.repository.menu;

import egovframework.com.devjitsu.model.consult.TblCnsltDtl;
import egovframework.com.devjitsu.model.menu.TblMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TblMenuRepository extends JpaRepository<TblMenu, String> {

}
