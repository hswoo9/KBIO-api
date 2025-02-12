package egovframework.com.devjitsu.model.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class StatisticsUserDto {

    private Long mbrType;
    private Long cnt;
}