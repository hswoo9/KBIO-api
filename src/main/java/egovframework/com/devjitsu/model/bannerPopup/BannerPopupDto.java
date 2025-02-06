package egovframework.com.devjitsu.model.bannerPopup;

import egovframework.com.devjitsu.model.common.TblComFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BannerPopupDto {

    private TblBnrPopup tblBnrPopup;
    private TblComFile tblComFile;
}
