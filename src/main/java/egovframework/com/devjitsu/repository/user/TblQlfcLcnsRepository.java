package egovframework.com.devjitsu.repository.user;

import egovframework.com.devjitsu.model.user.TblQlfcLcns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TblQlfcLcnsRepository extends JpaRepository<TblQlfcLcns,String> {
    TblQlfcLcns findByQlfcLcnsSn(long qlfcLcnsSn);

    List<TblQlfcLcns> findAllByUserSn(long userSn);

    TblQlfcLcns findByUserSn(long userSn);
}
