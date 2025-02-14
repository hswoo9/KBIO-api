package egovframework.com.devjitsu.model.bbs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TblBbsInPstDTO {

    private Long bbsSn;
    private String bbsNm;
    private String bbsTypeNm;
    private String atchFileYn;
    private String atchFileKndNm;
    private String wrtrRlsYn;
    private String cmntPsbltyYn;
    private String ansPsbltyYn;
    private String pstCtgryYn;
    private String rmrkCn;
    private String actvtnYn;
    private Long creatrSn;
    private LocalDateTime frstCrtDt;
    private List<TblPstSimpleDTO> tblPstList;

}
