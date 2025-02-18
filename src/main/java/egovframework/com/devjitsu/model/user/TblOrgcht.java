package egovframework.com.devjitsu.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_ORGCHT", catalog = "SCHM_BIO_MBR")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblOrgcht {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORGCHT_SN", length = 22)
    @Comment("조직도일련번호")
    private Long orgchtSn;

    @Column(name = "KORN_FLNM", length = 100)
    @Comment("한글성명")
    private String kornFlnm;

    @Column(name = "EMAIL", length = 320)
    @Comment("이메일")
    private String email;

    @Column(name = "TELNO", length = 11)
    @Comment("전화번호")
    private String telno;

    @Column(name = "DEPT_SN", length = 22)
    @Comment("부서일련번호")
    private Long deptSn;

    @Column(name = "JBTTL_SN", length = 22)
    @Comment("직책일련번호")
    private Long jbttlSn;

    @Column(name = "TKCG_TASK", length = 4000)
    @Comment("담당업무")
    private String tkcgTask;

    @Column(name = "SORT_SEQ", columnDefinition = "INT(10)")
    @Comment("정렬순서")
    private Integer sortSeq;

    @Column(name = "ACTVTN_YN", columnDefinition = "CHAR(1) DEFAULT 'Y'")
    @Comment("활성여부")
    private String actvtnYn;

    @Column(name = "CREATR_SN", columnDefinition = "INT(10)", updatable=false, nullable = false)
    @Comment("생성자일련번호")
    private Long creatrSn;

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
