package egovframework.com.devjitsu.model.consult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_CNSLT_DGSTFN")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblCnsltDgstfn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CNSLT_DGSTFN_SN", length = 22)
    @Comment("컨설팅만족도일련번호")
    private int cnsltDgstfnSn;

    @Column(name = "CNSLT_APLY_SN", length = 22, nullable = false, updatable = false)
    @Comment("컨설팅신청일련번호")
    private int cnsltAplySn;

    @Column(name = "DGSTFN_ARTCL", nullable = false, columnDefinition = "CHAR(8)")
    @Comment("만족도 항목")
    private String dgstfnArtcl;

    @Column(name = "CHC_SCR", length = 4, nullable = false)
    @Comment("선택점수")
    private String chcScr;

    @Column(name = "ACTVTN_YN", columnDefinition = "CHAR(1) DEFAULT 'Y'")
    @Comment("활성여부")
    private String actvtnYn;

    @Column(name = "CREATR_SN", columnDefinition = "INT(10)", updatable=false, nullable = false)
    @Comment("생성자일련번호")
    private int creatrSn;

    @Column(name = "FRST_CRT_DT", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Comment("최초생성일시")
    private LocalDateTime frstCrtDt = LocalDateTime.now();

    @Column(name = "MDFR_SN", columnDefinition = "INT(10)", insertable = false)
    @Comment("수정자일련번호")
    private Integer mdfrSn;

    @Column(name = "MDFCN_DT", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Comment("수정일")
    private LocalDateTime mdfcnDt;
}
