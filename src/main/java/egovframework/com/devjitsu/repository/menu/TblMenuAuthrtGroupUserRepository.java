package egovframework.com.devjitsu.repository.menu;

import egovframework.com.devjitsu.model.menu.TblMenuAuthrtGroup;
import egovframework.com.devjitsu.model.menu.TblMenuAuthrtGroupUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TblMenuAuthrtGroupUserRepository extends JpaRepository<TblMenuAuthrtGroupUser, String> {

}
