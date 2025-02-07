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
    private String pstClsfNm;
    private String pstTtl;
    private Long pstInqCnt;
    private String rlsYn;
    private String actvtnYn;
    private Long upPstSn;
    private Long creatrSn;
    private String kornFlnm;
    private String prvtPswd;
    private LocalDateTime frstCrtDt;
    private Long fileCnt;
    private String position;

    public PstDto(long pstSn, String pstTtl, String position) {
        this.pstSn = pstSn;
        this.pstTtl = pstTtl;
        this.position = position;
    }
}