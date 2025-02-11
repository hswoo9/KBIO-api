package egovframework.com.devjitsu.model.terms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "TBL_UTZTN_TRMS", catalog = "SCHM_BIO_MBR")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblUtztnTrms {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "UTZTN_TRMS_SN" , length = 22)
    @Comment("이용약관일련번호")
    private Long utztnTrmsSn;

    @Column(name = "UTZTN_TRMS_CN", columnDefinition = "LONGTEXT")
    @Comment("이용약관내용")
    private String utztnTrmsCn;

    @Column(name = "UTZTN_TRMS_KND", columnDefinition = "CHAR(1)")
    @Comment("이용약관종류")
    private String utztnTrmsKnd;

    @Column(name = "UTZTN_TRMS_TTL", length = 256)
    @Comment("이용약관제목")
    private String utztnTrmsTtl;

    @Column(name = "USE_YN", columnDefinition = "CHAR(1) DEFALUT 'Y'")
    @Comment("사용여부")
    private String useYn;

    @Column(name = "CREATR", length = 100)
    @Comment("생성자")
    private String creatr;

    @Column(name = "CREATR_SN", columnDefinition = "INT(10)")
    @Comment("생성자일련번호")
    private long creatrSn;

    @Column(name = "MDFR_DT", columnDefinition = "INT(10)")
    @Comment("수정자일련번호")
    private long mdfrSn;

    @Column(name = "MDFCN_DT", columnDefinition = "DATETIME ON UPDATE CURRENT_TIMESTAMP")
    @Comment("수정일")
    private LocalDateTime mdfcnDt;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "FRST_CRT_DT", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP", updatable = false)
    @Comment("최초생성일시")
    private LocalDateTime frstCrtDt = LocalDateTime.now();
}
