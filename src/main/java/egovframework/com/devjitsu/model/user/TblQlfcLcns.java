package egovframework.com.devjitsu.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_QLFC_LCNS", catalog = "SCHM_BIO_MBR")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblQlfcLcns {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QLFC_LCNS_SN" , length = 22)
    @Comment("자격/면허일련번호")
    private Long qlfcLcnsSn;

    @Column(name = "USER_SN" , length = 22)
    @Comment("사용자일렬번호")
    private Long userSn;

    @Column(name = "QLFC_LCNS_NM" , length = 100)
    @Comment("자격/면허명")
    private String qlfcLcnsNm;

    @Column(name = "ACQS_YMD" , columnDefinition = "CHAR(8)")
    @Comment("취득일자")
    private String acqsYmd;

    @Column(name = "PBLCN_INST_NM" , length = 200)
    @Comment("발행기관명")
    private String pblcnInstNm;

    @Column(name = "RMRK_CN" , length = 2000)
    @Comment("비고내용")
    private String rmrkCn;

    @Column(name = "SORT_SEQ", length = 10)
    @Comment("정렬순서")
    private Long sortSeq;

    @Column(name = "ACTVTN_YN", columnDefinition = "CHAR(1)")
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
