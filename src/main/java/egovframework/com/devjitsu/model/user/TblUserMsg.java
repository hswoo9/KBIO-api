package egovframework.com.devjitsu.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_USER_MSG", catalog = "SCHM_BIO_MBR")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblUserMsg {


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "MSG_SN", length = 22)
  @Comment("메시지일련번호")
  private Long msgSn;

  @Column(name = "DSPTCH_USER_SN", length = 22, nullable = false, updatable = false)
  @Comment("발신사용자일련번호")
  private long dsptchUserSn;

  @Column(name = "RCPTN_USER_SN", length = 22, updatable = false)
  @Comment("수신사용자일련번호")
  private Long rcptnUserSn;

  @Column(name = "MSG_TTL", length = 256)
  @Comment("메시지제목")
  private String msgTtl;

  @Column(name = "MSG_CN", length = 4000)
  @Comment("메시지내용")
  private String msgCn;

  @Column(name = "SNDNG_YMD", columnDefinition = "CHAR(8)")
  @Comment("발송일자")
  private String sndngYmd;

  @Column(name = "RCPTN_IDNTY_YN", columnDefinition = "CHAR(1)")
  @Comment("수신확인여부")
  private String rcptnIdntyYn = "N";

  @Column(name = "ACTVTN_YN", columnDefinition = "CHAR(1) DEFAULT 'Y'")
  @Comment("활성여부")
  private String actvtnYn = "Y";

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

  @Transient
  private String sendType;

}
