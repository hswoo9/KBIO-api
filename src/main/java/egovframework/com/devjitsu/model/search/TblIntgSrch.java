package egovframework.com.devjitsu.model.search;

import egovframework.com.devjitsu.model.menu.TblContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TBL_INTG_SRCH", catalog = "SCHM_BIO_COM")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblIntgSrch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INTG_SRCH_SN", length = 22)
    @Comment("통합검색일련번호")
    private Long intgSrchSn;

    @Column(name = "MENU_SN", length = 22)
    @Comment("메뉴일련번호")
    private Long menuSn;

    @Column(name = "ATCH_FILE_SN", length = 22)
    @Comment("파일일련번호")
    private Long atchFileSn;

    @Column(name = "PST_SN", length = 22)
    @Comment("게시글일련번호")
    private Long pstSn;

    @Column(name = "KND", length = 20)
    @Comment("종류")
    private String knd;

    @Column(name = "URL", length = 300)
    @Comment("URL")
    private String url;

    @Column(name = "TTL", length = 256)
    @Comment("제목")
    private String ttl;

    @Column(name = "CN", columnDefinition = "LONGTEXT")
    @Comment("내용")
    private String cn;

    @Column(name = "ATCH_FILE_NM", length = 100)
    @Comment("파일명")
    private String atchFileNm;

    @Column(name = "ATCH_FILE_EXTN_NM", length = 20)
    @Comment("파일확장자")
    private String atchFileExtnNm;

    @Column(name = "CREATR_SN", columnDefinition = "INT(10)", updatable = false, nullable = false)
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
