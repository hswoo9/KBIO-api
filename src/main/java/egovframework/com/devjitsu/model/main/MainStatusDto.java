package egovframework.com.devjitsu.model.main;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainStatusDto {

    private long mbrType2Cnt;
    private long mbrType4Cnt;

    private long mvnEntCnt;
    private long relInstCnt;

    private long dfclCnt;
    private long cnsltAply26Cnt;
    private long cnsltAply27Cnt;

    private long mbrSttsYCnt;
    private long mbrSttsWCnt;
    private long mbrSttsRCnt;
    private long mbrSttsSCnt;
}
