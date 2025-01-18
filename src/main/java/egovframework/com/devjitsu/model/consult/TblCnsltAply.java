package egovframework.com.devjitsu.model.consult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_CNSLT_APLY")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblCnsltAply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CNSLT_APLY_SN", length = 22)
    @Comment("컨설팅신청일련번호")
    private Long cnsltAplySn;

    @Column(name = "CNSLT_FLD", length = 8, nullable = false, columnDefinition = "CHAR(8)")
    @Comment("컨설팅분야")
    private String cnsltFld;

    @Column(name = "USER_SN", length = 22, nullable = false, updatable = false)
    @Comment("사용자 일련번호")
    private long userSn;

    @Column(name = "TTL", length = 100, nullable = false)
    @Comment("제목")
    private String ttl;

    @Column(name = "CN", columnDefinition = "LONGTEXT", nullable = false)
    @Comment("내용")
    private String cn;

    @Column(name = "ACTVTN_YN", columnDefinition = "CHAR(1) DEFAULT 'Y'")
    @Comment("활성여부")
    private String actvtnYn;

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
