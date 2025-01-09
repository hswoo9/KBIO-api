package egovframework.com.devjitsu.model.menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_MENU")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENU_SN", length = 22)
    @Comment("메뉴일련번호")
    private int menuSn;

    @Column(name = "UPPER_MENU_SN", length = 22, nullable = false)
    @Comment("상위메뉴일련번호")
    private int upperMenuSn;

    @Column(name = "MENU_NM", length = 200, nullable = false)
    @Comment("메뉴명")
    private String menuNm;

    @Column(name = "MENU_TYPE", length = 20, nullable = false)
    @Comment("메뉴유형")
    private String menuType;

    @Column(name = "MENU_SORT_SEQ", length = 10, nullable = false)
    @Comment("메뉴정렬")
    private int menuSortseq;

    @Column(name = "MENU_SEQ", length = 10, nullable = false)
    @Comment("뎁스")
    private int menuSeq;

    @Column(name = "MENU_PATH_NM", length = 300, nullable = false)
    @Comment("메뉴경로")
    private String menuPathNm;

    @Column(name = "LTR", length = 20)
    @Comment("언어")
    private String ltr;

    @Column(name = "LWR_MENU_EN", columnDefinition = "CHAR(1)")
    @Comment("하위메뉴여부")
    private String lwrMenuEn;

    @Column(name = "MENU_NM_PATH", length = 300)
    @Comment("메뉴이름경로")
    private String menuNmPath;

    @Column(name = "MENU_SN_PATH", length = 300)
    @Comment("메뉴일련번호경로")
    private String menuSnPath;

    @Column(name = "MENU_WHOL_PATH", length = 300)
    @Comment("메뉴전체경로")
    private String menuWholPath;

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
