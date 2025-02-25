package egovframework.com.devjitsu.model.consult;

import egovframework.com.devjitsu.model.user.TblCnslttMbr;
import egovframework.com.devjitsu.model.user.TblUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ConsultingDTO {

    private long cnsltAplySn;
    private long userSn;
    private long cnslttUserSn;
    private String kornFlnm;
    private String cnslttKornFlnm;
    private LocalDateTime frstCrtDt;
    private long cnsltFld;
    private String cnsltFldNm;
    private String ogdpNm;
    private long cnsltSttsCdSn;
    private String cnsltSttsCd;
    private String cnsltSttsCdNm;
    private long dgstfnCnt;
    private String ttl;
    private Long fileCnt;

    public ConsultingDTO(Long cnsltAplySn, Long userSn, Long cnslttUserSn, String kornFlnm, String cnslttKornFlnm,
                         LocalDateTime frstCrtDt, Long cnsltFld, String ogdpNm, String cnsltSttsCd, Long dgstfnCnt, String ttl, Long fileCnt) {
        this.cnsltAplySn = cnsltAplySn;
        this.userSn = userSn;
        this.cnslttUserSn = cnslttUserSn;
        this.kornFlnm = kornFlnm;
        this.cnslttKornFlnm = cnslttKornFlnm;
        this.frstCrtDt = frstCrtDt;
        this.cnsltFld = cnsltFld;
        this.ogdpNm = ogdpNm;
        this.cnsltSttsCd = cnsltSttsCd;
        this.dgstfnCnt = dgstfnCnt;
        this.ttl = ttl;
        this.fileCnt = fileCnt;
    }


}
