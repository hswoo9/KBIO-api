package egovframework.com.devjitsu.model.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StatisticsPstDto {

    private String day;
    private Long cnt;
}