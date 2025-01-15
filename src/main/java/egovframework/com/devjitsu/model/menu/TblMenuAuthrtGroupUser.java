package egovframework.com.devjitsu.model.menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_MENU_AUTHRT_GROUP_USER")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblMenuAuthrtGroupUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AUTHRT_GROUP_USER_SN", length = 22)
    @Comment("권한그룹사용자일련번호")
    private long authrtGroupUserSn;

    @Column(name = "AUTHRT_GROUP_SN", length = 22)
    @Comment("권한그룹일련번호")
    private long authrtGroupSn;

    @Column(name = "USER_SN", length = 22)
    @Comment("사용자사번")
    private long userSn;

    @Column(name = "USER_NM", length = 100)
    @Comment("사용자이름")
    private String userNm;

    @Column(name = "USER_ID", length = 100)
    @Comment("사용자아이디")
    private String userId;

    @Column(name = "USER_DEPT_NM", length = 100)
    @Comment("사용자부서이름")
    private String userDeptNm;

    @Column(name = "USER_JBTTL", length = 100)
    @Comment("사용자직책")
    private String userJbttl;

    @Column(name = "USER_JBGD", length = 100)
    @Comment("사용자직급")
    private String userJbgd;

    @Column(name = "AUTHRT_GRNT_DT")
    @Comment("권한부여일")
    private String authrtGrntDt;

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
    private Long mdfrSn;

    @Column(name = "MDFCN_DT", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Comment("수정일")
    private LocalDateTime mdfcnDt;
}
