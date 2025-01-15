package egovframework.com.devjitsu.model.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lettnemplyrinfo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LettnemplyrinfoVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_SN", length = 22)
    private int userSn;

    @Column(name = "EMPLYR_ID", length = 20)
    private String emplyrId;

    @Column(name = "ORGNZT_ID", length = 20)
    private String orgnztId;

    @Column(name = "USER_NM", length = 60)
    private String userNm;

    @Column(name = "PASSWORD", length = 200)
    private String password;

    @Column(name = "EMPL_NO", length = 20)
    private String emplNo;

    @Column(name = "IHIDNUM", length = 200)
    private String ihidnum;

    @Column(name = "SEXDSTN_CODE", length = 1)
    private String sexdstnCode;

    @Column(name = "BRTHDY", length = 20)
    private String brthdy;

    @Column(name = "FXNUM", length = 20)
    private String fxnum;

    @Column(name = "HOUSE_ADRES", length = 100)
    private String houseAdres;

    @Column(name = "PASSWORD_HINT", length = 100)
    private String passwordHint;

    @Column(name = "PASSWORD_CNSR", length = 100)
    private String passwordCnsr;

    @Column(name = "HOUSE_END_TELNO", length = 4)
    private String houseEndTelno;

    @Column(name = "AREA_NO", length = 4)
    private String areaNo;

    @Column(name = "DETAIL_ADRES", length = 200)
    private String detailAdres;

    @Column(name = "ZIP", length = 6)
    private String zip;

    @Column(name = "OFFM_TELNO", length = 20)
    private String offmTelno;

    @Column(name = "MBTLNUM", length = 20)
    private String mbtlnum;

    @Column(name = "EMAIL_ADRES", length = 50)
    private String emailAdres;

    @Column(name = "OFCPS_NM", length = 60)
    private String ofcpsNm;

    @Column(name = "HOUSE_MIDDLE_TELNO", length = 4)
    private String houseMiddleTelno;

    @Column(name = "GROUP_ID", length = 20)
    private String groupId;

    @Column(name = "PSTINST_CODE", length = 8)
    private String pstinstCode;

    @Column(name = "EMPLYR_STTUS_CODE", length = 15)
    private String emplyrSttusCode;

    @Column(name = "ESNTL_ID", length = 20)
    private String esntlId;

    @Column(name = "CRTFC_DN_VALUE", length = 20)
    private String crtfcDnValue;

    @Column(name = "SBSCRB_DE", length = 20)
    private String sbscrbDe;


}
