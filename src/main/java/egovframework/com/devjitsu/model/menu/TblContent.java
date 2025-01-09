package egovframework.com.devjitsu.model.menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_CONTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONTS_SN", length = 22)
    @Comment("메뉴콘텐츠일련번호")
    private int contsSn;

    @Column(name = "MENU_SN", length = 22)
    @Comment("메뉴일련번호")
    private int menuSn;

    @Column(name = "CONTS_CN", length = 4000)
    @Comment("내용")
    private String contsCn;

    @Column(name = "PSTG_BGNG_DT")
    @Comment("시작일")
    private LocalDateTime pstgBgngDt;

    @Column(name = "PSTG_END_DT")
    @Comment("종료일")
    private LocalDateTime pstgEndDt;

    @Column(name = "CONTS_TTL", length = 256)
    @Comment("제목")
    private String contsTtl;

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
