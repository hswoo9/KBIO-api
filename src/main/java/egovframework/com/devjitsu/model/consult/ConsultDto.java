package egovframework.com.devjitsu.model.consult;

import egovframework.com.devjitsu.model.common.TblComFile;
import egovframework.com.devjitsu.model.user.TblCnslttMbr;
import egovframework.com.devjitsu.model.user.TblUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ConsultDto {

    private TblCnslttMbr tblCnslttMbr;
    private TblUser tblUser;
    private TblComFile tblComFile;

}
