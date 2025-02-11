package egovframework.com.devjitsu.model.consult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_DFCL_MTTR", catalog = "SCHM_BIO_CNSLT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblDfclMttr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DFCL_MTTR_SN", length = 22)
    @Comment("애로사항일련번호")
    private Long dfclMttrSn;

    @Column(name = "USER_SN", length = 22, nullable = false, updatable = false)
    @Comment("사용자 일련번호")
    private long userSn;

    @Column(name = "DFCL_MTTR_FLD", length = 8, nullable = false, columnDefinition = "CHAR(8)")
    @Comment("애로사항분야")
    private String dfclMttrFld;

    @Column(name = "TTL", length = 100, nullable = false)
    @Comment("제목")
    private String ttl;

    @Column(name = "DFCL_MTTR_CN", columnDefinition = "LONGTEXT", nullable = false)
    @Comment("애로사항내용")
    private String dfclMttrCn;

    @Column(name = "ANS_CN", columnDefinition = "LONGTEXT")
    @Comment("답변내용")
    private String ansCn;

    @Column(name = "ACTVTN_YN", columnDefinition = "CHAR(1) DEFAULT 'Y'")
    @Comment("활성여부")
    private String actvtnYn = "Y";

    @Column(name = "CREATR_SN", columnDefinition = "INT(10)", updatable=false, nullable = false)
    @Comment("생성자일련번호")
    private long creatrSn;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "FRST_CRT_DT", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP", updatable = false)
    @Comment("최초생성일시")
    private LocalDateTime frstCrtDt = LocalDateTime.now();

    @Column(name = "MDFR_SN", columnDefinition = "INT(10)", insertable = false)
    @Comment("수정자일련번호")
    private Long mdfrSn;

    @Column(name = "MDFCN_DT", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Comment("수정일")
    private LocalDateTime mdfcnDt;
}
