package egovframework.com.devjitsu.model.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TblIntgSrchDTO {

    private Long intgSrchSn;
    private Long menuSn;
    private Long atchFileSn;
    private Long pstSn;
    private String knd;
    private String url;
    private String ttl;
    private String cn;
    private String atchFileNm;
    private String atchFileExtnNm;
    private String menuNmPath;
    private LocalDateTime frstCrtDt;
}
