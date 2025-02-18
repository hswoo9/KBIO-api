package egovframework.com.devjitsu.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TblOrgchtDTO {

    private Long orgchtSn;
    private String kornFlnm;
    private String email;
    private String telno;
    private Long deptSn;
    private String deptNm;
    private Long jbttlSn;
    private String jbttlNm;
    private String tkcgTask;
    private Integer sortSeq;
    private String actvtnYn;
    private LocalDateTime frstCrtDt;
}
