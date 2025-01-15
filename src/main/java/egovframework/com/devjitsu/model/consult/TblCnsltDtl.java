package egovframework.com.devjitsu.model.consult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_CNSLT_DTL")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblCnsltDtl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CNSLT_DTL_SN", length = 22)
    @Comment("컨설팅신청상세일련번호")
    private long cnsltDtlSn;

    @Column(name = "CNSLT_APLY_SN", length = 22, nullable = false, updatable = false)
    @Comment("컨설팅신청일련번호")
    private long cnsltAplySn;

    @Column(name = "DRCT_APLY_YN", nullable = false, columnDefinition = "CHAR(1) DEFAULT 'N'")
    @Comment("직접신청여부")
    private String drctAplyYn;

    @Column(name = "CNSLTT_USER_SN", length = 22)
    @Comment("컨설턴트사용자일련번호")
    private Integer cnslttUserSn;

    @Column(name = "CNSLTT_DSGN_DT")
    @Comment("컨설턴트지정일")
    private LocalDateTime cnslttDsgnDt;

    @Column(name = "CNSLT_STTS_CD", length = 4, nullable = false, columnDefinition = "VARCHAR(4) DEFAULT '0'")
    @Comment("컨설팅상태")
    private String cnsltSttsCd;

    @Column(name = "CNSLT_ACPT_DT")
    @Comment("컨설팅수락일")
    private LocalDateTime cnsltAcptDt;

    @Column(name = "CNSLT_CMPTN_DT")
    @Comment("컨설팅완료일")
    private LocalDateTime cnsltCmptnDt;

    @Column(name = "CNSLT_RTRCN_DT")
    @Comment("컨설팅취소일")
    private LocalDateTime cnsltRtrcnDt;

    @Column(name = "ACTVTN_YN", columnDefinition = "CHAR(1) DEFAULT 'Y'")
    @Comment("활성여부")
    private String actvtnYn;

    @Column(name = "CREATR_SN", columnDefinition = "INT(10)", updatable=false, nullable = false)
    @Comment("생성자일련번호")
    private long creatrSn;

    @Column(name = "FRST_CRT_DT", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Comment("최초생성일시")
    private LocalDateTime frstCrtDt = LocalDateTime.now();

    @Column(name = "MDFR_SN", columnDefinition = "INT(10)", insertable = false)
    @Comment("수정자일련번호")
    private Long mdfrSn;

    @Column(name = "MDFCN_DT", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Comment("수정일")
    private LocalDateTime mdfcnDt;
}
