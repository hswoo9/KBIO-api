package egovframework.com.devjitsu.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_MVN_ENT_MBR", catalog = "SCHM_BIO_MBR")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblMvnEntMbr {

    @Id
    @Column(name = "USER_SN" , length = 22)
    @Comment("사용자일렬번호")
    private Long userSn;

    @Column(name = "MVN_ENT_SN" , length = 22)
    @Comment("입주기업일련번호")
    private Long mvnEntSn;

    @Column(name = "SYS_MNGR_YN" , columnDefinition = "CHAR(1)")
    @Comment("시스템관리자여부")
    private String sysMngrYn;


}
