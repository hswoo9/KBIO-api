package egovframework.com.devjitsu.model.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class StatisticsUserDto {

    private long mbrType1Cnt;
    private long mbrType3Cnt;
    private long mbrType4Cnt;
    private long mbrType2Cnt;
    private String day;
}