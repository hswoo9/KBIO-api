package egovframework.com.devjitsu.repository.bannerPopup;

import egovframework.com.devjitsu.model.bannerPopup.TblBnrPopup;
import egovframework.com.devjitsu.model.common.TblComCdGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TblBnrPopupRepository extends JpaRepository<TblBnrPopup, String> {

    TblBnrPopup findByBnrPopupSn(Long bnrPopupSn);
}
