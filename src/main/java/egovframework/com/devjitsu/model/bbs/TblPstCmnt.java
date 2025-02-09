package egovframework.com.devjitsu.model.bbs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_PST_CMNT", catalog = "SCHM_BIO_CMS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblPstCmnt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PST_CMNT_SN", length = 22)
    @Comment("게시물댓글일련번호")
    private Long pstCmntSn;

    @Column(name = "PST_SN", length = 22, nullable = false, updatable = false)
    @Comment("게시물일련번호")
    private long pstSn;

    @Column(name = "CMNT_CN", length = 2000, nullable = false)
    @Comment("댓글내용")
    private String cmntCn;

    @Column(name = "CMNT_GRP", length = 10)
    @Comment("댓글그룹")
    private Long cmntGrp;

    @Column(name = "UP_PST_CMNT_SN", length = 22)
    @Comment("상위댓글일련번호")
    private Long upPstCmntSn;

    @Column(name = "CMNT_SEQ", length = 10)
    @Comment("댓글순서")
    private Long cmntSeq;

    @Column(name = "CMNT_STP", length = 10)
    @Comment("댓글단계")
    private Long cmntStp;

    @Column(name = "RLS_EN", columnDefinition = "CHAR(1) DEFAULT 'Y'")
    @Comment("공개유무")
    private String rlsEn = "Y";

    @Column(name = "PRVT_PSWD", length = 50)
    @Comment("비공개비밀번호")
    private String prvtPswd;

    @Column(name = "ACTVTN_YN", columnDefinition = "CHAR(1) DEFAULT 'Y'")
    @Comment("활성여부")
    private String actvtnYn = "Y";

    @Column(name = "CREATR_SN", columnDefinition = "INT(10)", updatable=false, nullable = false)
    @Comment("생성자일련번호")
    private long creatrSn;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "FRST_CRT_DT", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Comment("최초생성일시")
    private LocalDateTime frstCrtDt = LocalDateTime.now();

    @Column(name = "MDFR_SN", columnDefinition = "INT(10)", insertable = false)
    @Comment("수정자일련번호")
    private Long mdfrSn;

    @Column(name = "MDFCN_DT", columnDefinition = "DATETIME ON UPDATE CURRENT_TIMESTAMP", updatable = false)
    @Comment("수정일")
    private LocalDateTime mdfcnDt;
}
