package egovframework.com.devjitsu.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_PST_CNTN_HSTRY", catalog = "SCHM_BIO_COM")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblPstCntnHstry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CNTN_SN", length = 22)
    @Comment("접속일련번호")
    private Long cntnSn;

    @Column(name = "TRGT_TBL_NM", length = 300, nullable = false)
    @Comment("대상테이블명")
    private String trgtTblNm;

    @Column(name = "TRGT_SN", length = 22)
    @Comment("대상일련번호")
    private Long trgtSn;

    @Column(name = "CNTN_NMTM", length = 10, nullable = false)
    @Comment("접속횟수")
    private long cntnNmtm = 0;

    @Column(name = "ACTVTN_YN", columnDefinition = "CHAR(1) DEFAULT 'Y'")
    @Comment("활성여부")
    private String actvtnYn = "Y";

    @Column(name = "FRST_CRT_DT", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    @Comment("최초생성일시")
    private LocalDateTime frstCrtDt = LocalDateTime.now();

    @Column(name = "MDFCN_DT", columnDefinition = "DATETIME ON UPDATE CURRENT_TIMESTAMP")
    @Comment("수정일")
    private LocalDateTime mdfcnDt;
}
