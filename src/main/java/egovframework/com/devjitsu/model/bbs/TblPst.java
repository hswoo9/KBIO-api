package egovframework.com.devjitsu.model.bbs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_PST")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblPst {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PST_SN", length = 22)
    @Comment("게시물일련번호")
    private int pstSn;

    @Column(name = "UPEND_NTC_YN", columnDefinition = "CHAR(1) DEFAULT 'N'")
    @Comment("상단공지여부")
    private String upendNtcYn;

    @Column(name = "NTC_BGNG_DT")
    @Comment("공지시작일")
    private LocalDateTime ntcBgngDt;

    @Column(name = "NTC_END_DATE")
    @Comment("공지종료일")
    private LocalDateTime ntcEndDate;

    @Column(name = "OTSD_LINK", length = 100)
    @Comment("외부링크")
    private String otsdLink;

    @Column(name = "BBS_SN", length = 22, nullable = false)
    @Comment("게시판일련번호")
    private int bbsSn;

    @Column(name = "BBS_CTGRY_SN", length = 22)
    @Comment("게시판카테고리일련번호")
    private int bbsCtgrySn;

    @Column(name = "PST_TTL", length = 256)
    @Comment("게시글제목")
    private String pstTtl;

    @Column(name = "PST_CN", columnDefinition = "LONGTEXT")
    @Comment("게시글내용")
    private String pstCn;

    @Column(name = "PST_INQ_CNT", length = 10, columnDefinition = "INT(10) DEFAULT 0")
    @Comment("조회수")
    private int pstInqCnt = 0;

    @Column(name = "PST_GROUP", length = 10)
    @Comment("게시글그룹")
    private int pstGroup;

    @Column(name = "ORGNL_PST_SN", length = 22)
    @Comment("원글일련번호")
    private Integer orgnlPstSn;

    @Column(name = "CMNT_LEVEL", length = 10)
    @Comment("답글레벨")
    private Integer cmntLevel;

    @Column(name = "CMNT_PIC_SN", length = 22)
    @Comment("답글담당자사번")
    private Integer cmntPicSn;

    @Column(name = "RLS_YN", columnDefinition = "CHAR(1)")
    @Comment("공개여부")
    private String rlsYn;

    @Column(name = "PRVT_PSWD", length = 50)
    @Comment("비공개패스워드")
    private String prvtPswd;

    @Column(name = "POPUP_USE_YN", columnDefinition = "CHAR(1)")
    @Comment("팝업사용유무")
    private String popupUseYn;

    @Column(name = "POPUP_BGNG_DT")
    @Comment("팝업게시시작일")
    private LocalDateTime popupBgngDt;

    @Column(name = "POPUP_END_DT")
    @Comment("팝업게시종료일")
    private LocalDateTime popupEndDt;

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
