package egovframework.com.devjitsu.repository.access;

import egovframework.com.devjitsu.model.access.TblMngrAcsIp;
import egovframework.com.devjitsu.model.bannerPopup.TblBnrPopup;
import egovframework.com.devjitsu.model.bbs.TblBbs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TblMngrAcsIpRepository extends JpaRepository<TblMngrAcsIp, String> {

    TblMngrAcsIp findByMngrAcsSn(long mngrAcsSn);
    List<TblMngrAcsIp> findAll();
}
