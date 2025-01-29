package egovframework.com.devjitsu.model.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDto {
    private String sendType;
    private String userSn;
    private String title;
    private String content;
}