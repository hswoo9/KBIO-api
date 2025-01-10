package egovframework.com.devjitsu.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_USER_SNS_CERT_INFO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblUserSnsCertInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CERT_INFO_SN", length = 22)
    @Comment("인증정보일련번호")
    private int certInfoSn;

    @Column(name = "USER_SN", length = 22, nullable = false, updatable = false)
    @Comment("사용자일련번호")
    private int userSn;

    @Column(name = "SNS_SE", length = 20, nullable = false, updatable = false)
    @Comment("SNS구분")
    private String cd;

    @Column(name = "UNQ_NO", length = 100, nullable = false, updatable = false)
    @Comment("고유번호")
    private String unqNo;

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
