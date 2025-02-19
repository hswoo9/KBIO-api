package egovframework.com.devjitsu.model.user;

import egovframework.com.devjitsu.model.common.TblComFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserMsgDto {

    TblUserMsg tblUserMsg;
    TblUser tblUser;
}
