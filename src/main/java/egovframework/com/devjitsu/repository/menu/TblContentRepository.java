package egovframework.com.devjitsu.repository.menu;

import egovframework.com.devjitsu.model.menu.TblAuthrtGroupMenu;
import egovframework.com.devjitsu.model.menu.TblContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TblContentRepository extends JpaRepository<TblContent, String> {

}
