package egovframework.com.devjitsu.model.bbs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TblPstSimpleDTO {

    private Long pstSn;
    private String ntcBgngDt;
    private String ntcEndDate;
    private long bbsSn;
    private String pstTtl;
    private String pstCn;
    private String pstEngCn;
    private String rlsYn;
    private String actvtnYn;
    private LocalDateTime frstCrtDt;
}
