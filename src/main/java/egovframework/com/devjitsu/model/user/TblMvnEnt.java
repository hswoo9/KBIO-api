package egovframework.com.devjitsu.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_MVN_ENT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblMvnEnt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MVN_ENT_SN" , length = 22)
    @Comment("입주기업일련번호")
    private int mvnEntSn;

    @Column(name = "BRNO", columnDefinition = "CHAR(10)")
    @Comment("사업자등록번호")
    private String brno;

    @Column(name = "MVN_ENT_NM", length = 100)
    @Comment("입주기업명")
    private String mvnEntNm;

    @Column(name = "MVN_ENT_NM_ENG", length = 100)
    @Comment("입주기업영문명")
    private String mvnEntNmEng;

    @Column(name = "INDV_CORP_SE_NM", length = 100)
    @Comment("개인법인구분명")
    private String indvCorpSeNm;

    @Column(name = "CRNO", columnDefinition = "CHAR(13)")
    @Comment("법인등록번호")
    private String crno;

    @Column(name = "RPRSV_NM", length = 100)
    @Comment("대표자명")
    private String rpsvNm;

    @Column(name="RRNO", columnDefinition = "CHAR(13)")
    @Comment("주민등록번호")
    private String rrno;

    @Column(name = "ZIP", columnDefinition = "CHAR(5)")
    @Comment("우편번호")
    private String zip;

    @Column(name = "ENT_ADDR", length = 200)
    @Comment("기업주소")
    private String entAddr;

    @Column(name = "ENT_DADDR", length = 200)
    @Comment("기업상세주소")
    private String entDaaddr;

    @Column(name = "BZSTAT_NM", length = 100)
    @Comment("업태명")
    private String bzstatNm;

    @Column(name = "CLS_NM", length = 100)
    @Comment("종목명")
    private String clsNm;

    @Column(name = "ENT_TELNO", length = 11)
    @Comment("기업전화번호")
    private String entTelno;

    @Column(name = "RPRS_FXNO", length = 32)
    @Comment("대표팩스번호")
    private String rprsFxno;

    @Column(name = "HMPG_ADDR", length = 2000)
    @Comment("누리집주소")
    private String hmpgAddr;

    @Column(name = "BZENTY_EML_ADDR", length = 320)
    @Comment("업체이메일주소")
    private String bzentyEmlAddr;

    @Column(name = "BANK_NM", length = 32)
    @Comment("은행명")
    private String bankNm;

    @Column(name = "FNST_CD", columnDefinition = "CHAR(7)")
    @Comment("금융기관코드")
    private String fnstCd;

    @Column(name = "DPSTR_NM", length = 100)
    @Comment("예금주명")
    private String dpstrNm;

    @Column(name = "ACTNO", length = 20)
    @Comment("계좌번호")
    private String actno;

    @Column(name = "DLNG_SE_NM", length = 100)
    @Comment("거래구분명")
    private String dlngSeNm;

    @Column(name = "FNDN_YMD", columnDefinition = "CHAR(8)")
    @Comment("설립일자")
    private String fndnYmd;

    @Column(name = "CPTL", length = 15)
    @Comment("자본금")
    private int cptl;

    @Column(name = "EUSE_CNPL", length = 11)
    @Comment("비상연락처")
    private String euseCnpl;

    @Column(name = "MBL_TELNO", length = 11)
    @Comment("휴대전화번호")
    private String mblTelno;

    @Column(name = "MAIN_PRDT_NM", length = 300)
    @Comment("주요생산품명")
    private String mainPrdtNm;

    @Column(name = "RPRS_ITEM_NM", length = 100)
    @Comment("대표품목명")
    private String rprsItemNm;

    @Column(name = "RSCH_DVLP_ITEM_NM", length = 100)
    @Comment("연구개발품목명")
    private String rschDvlpItemNm;

    @Column(name = "EMP_CNT", length = 10)
    @Comment("직원수")
    private int empCnt;

    @Column(name = "FCTRY_ZIP", columnDefinition = "CHAR(5)")
    @Comment("공장우편번호")
    private String fctryZip;

    @Column(name = "FCTRY_ADDR", length = 200)
    @Comment("공장주소")
    private String fctryAddr;

    @Column(name = "FCTRY_DADDR", length = 200)
    @Comment("공장상세주소")
    private String fctryDaaddr;

    @Column(name = "RPRSV_ZIP", columnDefinition = "CHAR(5)")
    @Comment("대표자우편번호")
    private String rprsvZip;

    @Column(name = "RPRSV_ADDR", length = 320)
    @Comment("대표자주소")
    private String rprsvAddr;

    @Column(name = "RPRSV_DADDR", length = 200)
    @Comment("대표자상세주소")
    private String rprsvDaaddr;

    @Column(name = "RMRK_CN", length = 4000)
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
