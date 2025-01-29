package egovframework.com.devjitsu.model.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationMessage {
    private String userSn;
    private String title;
    private String content;
}