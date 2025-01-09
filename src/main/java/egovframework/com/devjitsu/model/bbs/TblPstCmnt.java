package egovframework.com.devjitsu.model.bbs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_PST_CMNT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblPstCmnt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PST_RELY_SN", length = 22)
    @Comment("게시물댓글일련번호")
    private int pstRelySn;

    @Column(name = "PST_SN", length = 22)
    @Comment("게시물일련번호")
    private int pstSn;

    @Column(name = "CMNT_CN", length = 4000)
    @Comment("댓글내용")
    private String cmntCn;

    @Column(name = "CMNT_GRP", length = 22)
    @Comment("원글일련번호")
    private Integer cmntGrp;

    @Column(name = "CMNT_SEQ", length = 10)
    @Comment("댓글순서")
    private int cmntSeq;

    @Column(name = "CMNT_LEVEL", length = 10)
    @Comment("댓글깊이")
    private int cmntLevel;

    @Column(name = "CMNT_RLS_EN", length = 10)
    @Comment("공개유무")
    private int cmntRlsEn;

    @Column(name = "PRVT_PSWD", length = 50)
    @Comment("게시글제목")
    private String prvtPswd;

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
