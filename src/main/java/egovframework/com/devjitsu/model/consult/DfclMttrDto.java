package egovframework.com.devjitsu.model.consult;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class DfclMttrDto {
    private long dfclMttrSn;
    private long userSn;
    private long dfclMttrFld;
    private String dfclMttrFldNm;
    private String ttl;
    private String kornFlnm;
    private LocalDateTime frstCrtDt;
    private long fileCnt;
    private String answer;
}