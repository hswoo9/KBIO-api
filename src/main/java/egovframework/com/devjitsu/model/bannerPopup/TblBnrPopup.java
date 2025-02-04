package egovframework.com.devjitsu.model.bannerPopup;

import egovframework.com.devjitsu.model.common.TblComFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "TBL_BNR_POPUP", catalog = "SCHM_BIO_CMS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblBnrPopup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BNR_POPUP_SN", length = 22)
    @Comment("배너팝업일련번호")
    private Long bnrPopupSn;

    @Column(name = "BNR_POPUP_CN", length = 4000)
    @Comment("배너팝업내용")
    private String bnrPopupCn;

    @Column(name = "BNR_POPUP_KND", length = 100)
    @Comment("배너팝업종류")
    private String bnrPopupKnd;

    @Column(name = "BNR_POPUP_FRM", length = 100)
    @Comment("배너팝업형식")
    private String bnrPopupFrm;

    @Column(name = "POPUP_WDTH_SZ", columnDefinition = "INT(7)")
    @Comment("팝업가로크기")
    private Integer popupWdthSz;

    @Column(name = "POPUP_VRTC_SZ", columnDefinition = "INT(7)")
    @Comment("팝업세로크기")
    private Integer popupVrtcSz;

    @Column(name = "POPUP_PSTN_UPEND", columnDefinition = "INT(7)")
    @Comment("팝업위치상단")
    private Integer popupPstnUpend;

    @Column(name = "POPUP_PSTN_WDTH", columnDefinition = "INT(7)")
    @Comment("팝업위치가로")
    private Integer popupPstnWdth;

    @Column(name = "BNR_POPUP_URL_ADDR", length = 2000)
    @Comment("배너팝업URL주소")
    private String bnrPopupUrlAddr;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "POPUP_BGNG_DT")
    @Comment("팝업시작일시")
    private LocalDateTime popupBgngDt;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "POPUP_END_DT")
    @Comment("팝업종료일시")
    private LocalDateTime popupEndDt;

    @Column(name = "NPAG_YN", columnDefinition = "CHAR(1) DEFAULT 'Y'")
    @Comment("새창여부")
    private String npagYn;

    @Column(name = "BNR_POPUP_TTL", length = 256)
    @Comment("제목")
    private String bnrPopupTtl;

    @Column(name = "YOUTUBE_YN", columnDefinition = "CHAR(1) DEFAULT 'N'")
    @Comment("유튜브사용여부")
    private String youtubeYn;

    @Column(name = "ATCH_FILE_SN", columnDefinition = "INT(10)", updatable=false)
    @Comment("파일일련번호")
    private Long atchFileSn;

    @Column(name = "USE_YN", columnDefinition = "CHAR(1) DEFAULT 'Y'")
    @Comment("사용여부")
    private String useYn;

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
    private List<TblComFile> tblComFiles;
}
