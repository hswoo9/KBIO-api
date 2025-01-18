package egovframework.com.devjitsu.model.bbs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.format.annotation.DateTimeFormat;

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
    private long bbsSn;

    @Column(name = "BBS_NM", length = 256, nullable = false)
    @Comment("게시판명")
    private String bbsNm;

    @Column(name = "BBS_TYPE", length = 20, nullable = false)
    @Comment("게시판종류명")
    private String bbsType;

    @Column(name = "ATCH_FILE_YN", nullable = false, columnDefinition = "CHAR(1)")
    @Comment("파일첨부가능여부")
    private String atchFileYn = "N";

    @Column(name = "ATCH_FILE_KND_NM", length = 200)
    @Comment("첨부파일종류명")
    private String atchFileKndNm;

    @Column(name = "WRTR_RLS_YN", columnDefinition = "CHAR(1)")
    @Comment("작성자 공개유무")
    private String wrtrRlsYn = "N";

    @Column(name = "CMNT_PSBLTY_YN", columnDefinition = "CHAR(1)")
    @Comment("댓글가능여부")
    private String cmntPsbltyYn = "N";

    @Column(name = "REPLY_PSBLTY_YN", columnDefinition = "CHAR(1)")
    @Comment("답글 사용유무")
    private String replyPsbltyYn = "N";

    @Column(name = "BSC_PST_CNT", columnDefinition = "INT(10)")
    @Comment("기본 게시글 개수")
    private int bscPstCnt = 10;

    @Column(name = "PST_CTGRY_YN", columnDefinition = "CHAR(1)")
    @Comment("게시글 카테고리 사용유무")
    private String pstCtgryYn = "N";

    @Column(name = "RMRK_CN", length = 2000)
    @Comment("비고내용")
    private String rmrkCn;

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
}
