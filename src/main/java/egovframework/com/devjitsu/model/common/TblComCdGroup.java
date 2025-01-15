package egovframework.com.devjitsu.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_COM_CD_GROUP")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblComCdGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GROUP_CD_SN", length = 22)
    @Comment("그룹코드일련번호")
    private int groupCdSn;

    @Column(name = "GROUP_CD", length = 20, nullable = false, updatable = false)
    @Comment("그룹코드")
    private String groupCd;

    @Column(name = "GROUP_CD_NM", length = 100, nullable = false)
    @Comment("그룹코드명")
    private String groupCdNm;

    @Column(name = "ETC_MTTR1", length = 2000)
    @Comment("기타사항1")
    private String etcMttr1;

    @Column(name = "ETC_MTTR2", length = 2000)
    @Comment("기타사항2")
    private String etcMttr2;

    @Column(name = "ETC_MTTR3", length = 2000)
    @Comment("기타사항3")
    private String etcMttr3;

    @Column(name = "ETC_MTTR4", length = 2000)
    @Comment("기타사항4")
    private String etcMttr4;

    @Column(name = "ETC_MTTR5", length = 2000)
    @Comment("기타사항5")
    private String etcMttr5;

    @Column(name = "RMRK_CN", length = 2000)
    @Comment("비고내용")
    private String rmrkCn;

    @Column(name = "SORT_SEQ", columnDefinition = "INT(10)")
    @Comment("정렬순서")
    private Integer sortSeq;

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
    private Long mdfrSn;

    @Column(name = "MDFCN_DT", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Comment("수정일")
    private LocalDateTime mdfcnDt;
}
