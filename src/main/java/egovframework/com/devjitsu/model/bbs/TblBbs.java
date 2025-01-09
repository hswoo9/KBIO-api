package egovframework.com.devjitsu.model.bbs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_BBS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblBbs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BBS_SN", length = 22)
    @Comment("게시판일련번호")
    private int bbsSn;

    @Column(name = "BBS_NM", length = 256, nullable = false)
    @Comment("게시판명")
    private String bbsNm;

    @Column(name = "BBS_TYPE", length = 20, nullable = false)
    @Comment("게시판종류명")
    private String bbsType;

    @Column(name = "ATCH_FILE_YN", nullable = false, columnDefinition = "CHAR(1)")
    @Comment("파일첨부가능여부")
    private String atchFileYn;

    @Column(name = "ATCH_FILE_KND_NM", length = 200)
    @Comment("첨부파일종류명")
    private String atchFileKndNm;

    @Column(name = "WRTR_RLS_YN", columnDefinition = "CHAR(1)")
    @Comment("작성자 공개유무")
    private String wrtrRlsYn;

    @Column(name = "WRTR_AUTHRT_STNG", length = 100)
    @Comment("작성자 권한설정")
    private String wrtrAuthrtStng;

    @Column(name = "CMNT_PSBLTY_YN", columnDefinition = "CHAR(1)")
    @Comment("댓글가능여부")
    private String cmntPsbltyYn;

    @Column(name = "REPLY_PSBLTY_YN", columnDefinition = "CHAR(1)")
    @Comment("답글 사용유무")
    private String replyPsbltyYn;

    @Column(name = "BSC_PST_CNT", columnDefinition = "INT(10)")
    @Comment("기본 게시글 개수")
    private int bscPstCnt = 10;

    @Column(name = "PST_CTGRY_YN", columnDefinition = "CHAR(1)")
    @Comment("게시글 카테고리 사용유무")
    private String pstCtgryYn;

    @Column(name = "WRTR_AUTHRT_YN", columnDefinition = "CHAR(1)")
    @Comment("작성권한 사용유무")
    private String wrtrAuthrtYn;

    @Column(name = "WRTR_AUTHRT_GROUP_PK", length = 50)
    @Comment("작성권한 권한그룹 기본키")
    private String wrtrAuthrtGroupPk;

    @Column(name = "SORT_SEQ", columnDefinition = "INT(10)")
    @Comment("정렬순서")
    private Integer sortSeq;

    @Column(name = "RMRK_CN", length = 2000)
    @Comment("비고내용")
    private String rmrkCn;

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
