package egovframework.com.devjitsu.model.main;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MainCalendarDto {

    private String type;
    private String day;
    private long cnt;
}
