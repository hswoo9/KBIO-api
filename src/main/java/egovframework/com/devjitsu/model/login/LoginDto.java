package egovframework.com.devjitsu.model.login;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {

    private String id;
    private String password;
    private String loginType;
    private String confirmPass;
    private String snsType;
    private String code;

    /** google, naver **/
    private String email;
    private String name;

    /** naver, kakao, google [고유값] **/
    private String snsId;

    /** naver **/
    private String state;

    private int statusCode;

    private Object totalData;
}
