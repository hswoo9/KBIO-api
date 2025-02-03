package egovframework.com.devjitsu.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_USER", catalog = "SCHM_BIO_MBR")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblUser {


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "USER_SN", length = 22)
  @Comment("사용자일련번호")
  private Long userSn;

  @Column(name = "MBR_TYPE", length = 10)
  @Comment("회원구분")
  private long mbrType;

  @Column(name = "USER_PW", length = 200)
  @Comment("비밀번호")
  private String userPw;

  @Column(name = "USER_ID", length = 20)
  @Comment("사용자ID")
  private String userId;

  @Column(name = "KORN_FLNM", length = 100)
  @Comment("한글성명")
  private String kornFlnm;

  @Column(name = "MBL_TELNO", length = 11)
  @Comment("사용자휴대전화번호")
  private String mblTelno;

  @Column(name = "EMAIL", length = 320)
  @Comment("사용자이메일")
  private String email;

  @Column(name = "ZIP", columnDefinition = "CHAR(9)")
  @Comment("사용자우편번호")
  private String zip;

  @Column(name = "ADDR", length = 200)
  @Comment("사용자주소")
  private String addr;

  @Column(name = "DADDR", length = 200)
  @Comment("사용자상세주소")
  private String daddr;

  @Column(name = "JOIN_YMD", columnDefinition = "CHAR(8)")
  @Comment("가입일시")
  private String joinYmd;

  @Column(name = "SWTC_YMD", columnDefinition = "CHAR(8)")
  @Comment("전환일")
  private String swtcYmd;

  @Column(name = "EML_RCPTN_AGRE_YN", columnDefinition = "CHAR(1)")
  @Comment("메일수신동의여부")
  private String emlRcptnAgreYn;

  @Column(name = "SMS_RCPTN_AGRE_YN", columnDefinition = "CHAR(1)")
  @Comment("SMS수신동의여부")
  private String smsRcptnAgreYn;

  @Column(name = "INFO_RLS_YN", columnDefinition = "CHAR(1)")
  @Comment("정보공개여부")
  private String infoRlsYn;

  @Column(name = "ACTVTN_YN", columnDefinition = "CHAR(1)")
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

  @Column(name = "MDFCN_DT", columnDefinition = "DATETIME ON UPDATE CURRENT_TIMESTAMP")
  @Comment("수정일")
  private LocalDateTime mdfcnDt;

}
