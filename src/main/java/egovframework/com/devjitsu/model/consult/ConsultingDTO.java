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
    private String ogdpNm;
    private String cnsltSttsCd;
    private String dgstfnArtcl;
    private String ttl;

    public ConsultingDTO(Long cnsltAplySn, Long userSn, Long cnslttUserSn, String kornFlnm, String cnslttKornFlnm,
                         LocalDateTime frstCrtDt, Long cnsltFld, String ogdpNm, String cnsltSttsCd, String dgstfnArtcl, String ttl) {
        this.cnsltAplySn = cnsltAplySn;
        this.userSn = userSn;
        this.cnslttUserSn = cnslttUserSn;
        this.kornFlnm = kornFlnm;
        this.cnslttKornFlnm = cnslttKornFlnm;
        this.frstCrtDt = frstCrtDt;
        this.cnsltFld = cnsltFld;
        this.ogdpNm = ogdpNm;
        this.cnsltSttsCd = cnsltSttsCd;
        this.dgstfnArtcl = dgstfnArtcl;
        this.ttl = ttl;
    }


}
