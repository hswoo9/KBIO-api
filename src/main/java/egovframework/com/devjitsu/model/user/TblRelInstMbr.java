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
@Table(name = "TBL_REL_INST_MBR", catalog = "SCHM_BIO_MBR")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblRelInstMbr {

    @Id
    @Column(name = "USER_SN" , length = 22)
    @Comment("사용자일렬번호")
    private Long userSn;

    @Column(name = "REL_INST_SN" , length = 22)
    @Comment("유관기관일련번호")
    private Long relInstSn;

    @Column(name = "SYS_MNGR_YN" , columnDefinition = "CHAR(1)")
    @Comment("시스템관리자여부")
    private String sysMngrYn = "N";

    @Column(name = "APRV_YN" , columnDefinition = "CHAR(1)")
    @Comment("승인여부")
    private String aprvYn = "N";


}
