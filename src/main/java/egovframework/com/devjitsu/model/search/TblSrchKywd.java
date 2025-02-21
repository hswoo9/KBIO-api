package egovframework.com.devjitsu.model.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_SRCH_KYWD", catalog = "SCHM_BIO_COM")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblSrchKywd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SRCH_KYWD_SN", length = 22)
    @Comment("검색키워드일련번호")
    private Long srchKywdSn;

    @Column(name = "KYWD", length = 256)
    @Comment("키워드")
    private String kywd;

    @Column(name = "SRCH_NMTM", length = 22)
    @Comment("검색횟수")
    private Long srchNmtm = 0L;

    @Column(name = "FRST_CRT_DT", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Comment("최초생성일시")
    private LocalDateTime frstCrtDt = LocalDateTime.now();

    @Column(name = "MDFCN_DT", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Comment("수정일시")
    private LocalDateTime mdfcnDt;

    public void incrementSrchNmtm() {
        this.srchNmtm++;
    }
}
