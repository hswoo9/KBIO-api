package egovframework.com.devjitsu.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
@AllArgsConstructor
public class UserMsgDto {

    TblUserMsg tblUserMsg;
    TblUser dsptchUser;

    Object rcptnUser;
}
