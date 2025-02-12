package egovframework.com.devjitsu.model.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StatisticsUserAccessDto {

    private Long mbrType1Cnt;
    private Long mbrType3Cnt;
    private Long mbrType4Cnt;
    private Long mbrType2Cnt;
    private String day;
}