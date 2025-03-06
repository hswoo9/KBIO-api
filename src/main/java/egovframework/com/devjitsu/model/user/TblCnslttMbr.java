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
@Table(name = "TBL_CNSLTT_MBR", catalog = "SCHM_BIO_MBR")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblCnslttMbr {

    @Id
    @Column(name = "USER_SN" , length = 22)
    @Comment("사용자일렬번호")
    private Long userSn;

    @Column(name = "CNSLT_ACTV", columnDefinition = "CHAR(1)")
    @Comment("컨설팅활동")
    private String cnsltActv;

    @Column(name = "OGDP_NM", length = 200)
    @Comment("소속명")
    private String ogdpNm;

    @Column(name = "JBPS_NM", length = 100)
    @Comment("직위명")
    private String jbpsNm;

    @Column(name = "CRR_PRD", length = 10)
    @Comment("경력기간")
    private int crrPrd;

    @Column(name = "CNSLT_FLD", length = 20)
    @Comment("자문분야")
    private long cnsltFld;

    @Column(name = "CNSLT_ARTCL", length = 100)
    @Comment("컨설팅항목")
    private String cnsltArtcl;

    @Column(name = "CNSLT_SLFINT", columnDefinition = "LONGTEXT")
    @Comment("컨설턴트소개")
    private String cnsltSlfint;

    @Column(name = "RMRK_CN" , length = 2000)
    @Comment("비고내용")
    private String rmrkCn;
}
