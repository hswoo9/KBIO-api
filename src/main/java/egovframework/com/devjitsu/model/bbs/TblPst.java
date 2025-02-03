package egovframework.com.devjitsu.model.bbs;

import egovframework.com.devjitsu.model.common.TblComFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "TBL_PST", catalog = "SCHM_BIO_CMS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblPst {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PST_SN", length = 22)
    @Comment("게시물일련번호")
    private Long pstSn;

    @Column(name = "UPEND_NTC_YN", columnDefinition = "CHAR(1) DEFAULT 'N'")
    @Comment("상단공지여부")
    private String upendNtcYn = "N";

    @Column(name = "NTC_BGNG_DT", columnDefinition = "CHAR(8)")
    @Comment("공지시작일")
    private String ntcBgngDt;

    @Column(name = "NTC_END_DATE", columnDefinition = "CHAR(8)")
    @Comment("공지종료일")
    private String ntcEndDate;

    @Column(name = "LINK_URL_ADDR", length = 100)
    @Comment("연계URL주소")
    private String linkUrlAddr;

    @Column(name = "BBS_SN", length = 22, nullable = false)
    @Comment("게시판일련번호")
    private long bbsSn;

    @Column(name = "BBS_CLSF", length = 22)
    @Comment("게시판분류(공통코드일련번호)")
    private Long bbsClsf;

    @Column(name = "PST_TTL", length = 256)
    @Comment("게시글제목")
    private String pstTtl;

    @Column(name = "PST_CN", columnDefinition = "LONGTEXT")
    @Comment("게시글내용")
    private String pstCn;

    @Column(name = "PST_INQ_CNT", length = 10, columnDefinition = "INT(10) DEFAULT 0")
    @Comment("조회수")
    private long pstInqCnt = 0;

    @Column(name = "PST_GROUP", length = 10)
    @Comment("게시글그룹")
    private Long pstGroup;

    @Column(name = "UP_PST_SN", length = 22)
    @Comment("상위게시물일련번호")
    private Long upPstSn;

    @Column(name = "ANS_STP", length = 10)
    @Comment("답글단계")
    private Integer ansStp;

    @Column(name = "ANS_USER_SN", length = 22)
    @Comment("답글사용자일련번호")
    private Long ansUserSn;

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

    @Transient
    private List<TblPstCmnt> pstCmnt;

    @Transient
    private List<TblComFile> pstFiles;
}
