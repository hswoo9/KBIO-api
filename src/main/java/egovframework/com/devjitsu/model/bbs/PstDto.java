package egovframework.com.devjitsu.model.bbs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PstDto {

    private long pstSn;
    private String upendNtcYn;
    private long bbsSn;
    private String pstTtl;
    private long pstInqCnt;
    private String rlsYn;
    private String actvtnYn;
    private long creatrSn;
    private LocalDateTime frstCrtDt;
    private long fileCnt;
}