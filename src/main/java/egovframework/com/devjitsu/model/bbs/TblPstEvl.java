package egovframework.com.devjitsu.model.bbs;

import egovframework.com.devjitsu.model.common.TblComFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "TBL_PST_EVL")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblPstEvl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PST_EVL_SN", length = 22)
    @Comment("게시물평가일련번호")
    private Long pstEvlSn;

    @Column(name = "PST_SN", length = 22, nullable = false)
    @Comment("게시글일련번호")
    private long pstSn;

    @Column(name = "EVL_USER_SN", length = 22, nullable = false)
    @Comment("평가사용자일련번호")
    private long evlUserSn;

    @Column(name = "EVL_YMD", nullable = false, columnDefinition = "CHAR(8)")
    @Comment("평가일자")
    private String evlYmd;

    @Column(name = "EVL_ARTCL_NO", length = 10, nullable = false)
    @Comment("평가항목번호")
    private String evlArtclNo;

    @Column(name = "IMPV_OPNN_CN", length = 4000)
    @Comment("개선의견내용")
    private String impvOpnnCn;

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

    @Column(name = "MDFCN_DT", columnDefinition = "DATETIME ON UPDATE CURRENT_TIMESTAMP")
    @Comment("수정일")
    private LocalDateTime mdfcnDt;
}
