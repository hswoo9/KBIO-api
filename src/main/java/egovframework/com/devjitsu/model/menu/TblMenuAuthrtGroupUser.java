package egovframework.com.devjitsu.model.menu;

import egovframework.com.devjitsu.model.user.TblUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_MENU_AUTHRT_GROUP_USER", catalog = "SCHM_BIO_CMS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblMenuAuthrtGroupUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AUTHRT_GROUP_USER_SN", length = 22)
    @Comment("권한그룹사용자일련번호")
    private Long authrtGroupUserSn;

    @Column(name = "AUTHRT_GROUP_SN", length = 22)
    @Comment("권한그룹일련번호")
    private long authrtGroupSn;

    @Column(name = "USER_SN", length = 22)
    @Comment("사용자일련번호")
    private long userSn;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "AUTHRT_GRNT_DT")
    @Comment("권한부여일")
    private LocalDateTime authrtGrntDt;

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

    @Column(name = "MDFCN_DT", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Comment("수정일")
    private LocalDateTime mdfcnDt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_SN", referencedColumnName = "USER_SN", insertable = false, updatable = false)
    private TblUser tblUser;

}
