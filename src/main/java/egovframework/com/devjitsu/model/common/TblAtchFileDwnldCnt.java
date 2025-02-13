package egovframework.com.devjitsu.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_ATCH_FILE_DWNLD_CNT", catalog = "SCHM_BIO_COM")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblAtchFileDwnldCnt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DWNLD_SN", length = 22)
    @Comment("다운로드일련번호")
    private Long dwnldSn;

    @Column(name = "TRGT_TBL_NM", length = 300, nullable = false)
    @Comment("대상테이블명")
    private String trgtTblNm;

    @Column(name = "TRGT_SN", length = 22)
    @Comment("대상일련번호")
    private Long trgtSn;

    @Column(name = "ATCH_FILE_DWNLD_CNT", length = 10, nullable = false)
    @Comment("첨부파일다운로드수")
    private long atchFileDwnldCnt = 0;

    @Column(name = "ACTVTN_YN", columnDefinition = "CHAR(1) DEFAULT 'Y'")
    @Comment("활성여부")
    private String actvtnYn = "Y";

    @Column(name = "CREATR_SN", columnDefinition = "INT(10)", updatable=false, nullable = false)
    @Comment("생성자일련번호")
    private long creatrSn;

    @Column(name = "FRST_CRT_DT", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Comment("최초생성일시")
    private LocalDateTime frstCrtDt = LocalDateTime.now();

    @Column(name = "MDFR_SN", columnDefinition = "INT(10)", insertable = false)
    @Comment("수정자일련번호")
    private Long mdfrSn;

    @Column(name = "MDFCN_DT", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Comment("수정일")
    private LocalDateTime mdfcnDt;
}
