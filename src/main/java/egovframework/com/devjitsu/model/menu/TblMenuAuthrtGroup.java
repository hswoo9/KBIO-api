package egovframework.com.devjitsu.model.menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TBL_MENU_AUTHRT_GROUP")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblMenuAuthrtGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AUTHRT_GROUP_SN", length = 22)
    @Comment("권한그룹일련번호")
    private long authrtGroupSn;

    @Column(name = "AUTHRT_GROUP_NM", length = 200)
    @Comment("권한그룹명")
    private String authrtGroupNm;

    @Column(name = "AUTHRT_TYPE", length = 20)
    @Comment("권한구분")
    private String authrtType;

    @Column(name = "INQ_AUTHRT", columnDefinition = "CHAR(1)")
    @Comment("읽기권한")
    private String inqAuthrt;

    @Column(name = "WRT_AUTHRT", columnDefinition = "CHAR(1)")
    @Comment("작성권한")
    private String wrtAuthrt;

    @Column(name = "MDFCN_AUTHRT", columnDefinition = "CHAR(1)")
    @Comment("수정권한")
    private String mdfcnAuthrt;

    @Column(name = "DEL_AUTHRT", columnDefinition = "CHAR(1)")
    @Comment("삭제권한")
    private String delAuthrt;

    @Column(name = "ACTVTN_YN", columnDefinition = "CHAR(1) DEFAULT 'Y'")
    @Comment("활성여부")
    private String actvtnYn;

    @Column(name = "CREATR_SN", columnDefinition = "INT(10)", updatable=false, nullable = false)
    @Comment("생성자일련번호")
    private long creatrSn;

    @Column(name = "FRST_CRT_DT", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Comment("최초생성일시")
    private LocalDateTime frstCrtDt = LocalDateTime.now();

    @Column(name = "MDFR_SN", columnDefinition = "INT(10)", insertable = false)
    @Comment("수정자일련번호")
    private Integer mdfrSn;

    @Column(name = "MDFCN_DT", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Comment("수정일")
    private LocalDateTime mdfcnDt;

    @Transient
    private List<TblMenu> allowAccessMenu = new ArrayList<>();
}
