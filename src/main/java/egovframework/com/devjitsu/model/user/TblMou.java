package egovframework.com.devjitsu.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_MOU", catalog = "SCHM_BIO_MBR")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblMou {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MOU_SN", length = 22)
    @Comment("MOU 일련번호")
    private Long mouSn;

    @Column(name = "MOU_NM", length = 100)
    @Comment("기관명")
    private String mouNm;

    @Column(name = "MOU_JOIN_YMD", columnDefinition = "DATETIME")
    @Comment("가입일")
    private LocalDateTime mouJoinYmd;

    @Column(name = "RPRSV_NM", length = 100)
    @Comment("대표자명")
    private String rpsvNm;

    @Column(name = "BRNO", columnDefinition = "CHAR(10)")
    @Comment("사업자등록번호")
    private String brno;

    @Column(name = "BZENTY_EML_ADDR", length = 320)
    @Comment("업체이메일주소")
    private String bzentyEmlAddr;

    @Column(name = "MOU_CLSF", length = 20)
    @Comment("기업분류")
    private String mouClsf;

    @Column(name = "MOU_TPBIZ", length = 20)
    @Comment("기업업종")
    private String mouTpbiz;

    @Column(name = "MOU_ADDR", length = 200)
    @Comment("기업주소")
    private String mouAddr;

    @Column(name = "MOU_TELNO", length = 11)
    @Comment("기업전화번호")
    private String mouTelno;

    @Column(name = "ZIP", columnDefinition = "CHAR(5)")
    @Comment("우편번호")
    private String zip;

    @Column(name = "HMPG_ADDR", length = 2000)
    @Comment("누리집주소")
    private String hmpgAddr;

    @Column(name = "CREATR_SN", columnDefinition = "INT(10)", updatable = false, nullable = false)
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