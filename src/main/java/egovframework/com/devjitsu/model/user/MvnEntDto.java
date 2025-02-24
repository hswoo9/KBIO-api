package egovframework.com.devjitsu.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MvnEntDto {


    private TblMvnEnt tblMvnEnt;
    private String entClsfNm;
    private String entTpbizNm;

}
