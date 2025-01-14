package egovframework.com.devjitsu.model.menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_AUTHRT_GROUP_MENU")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TblAuthrtGroupMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AUTHRT_GROUP_MENU_SN", length = 22)
    @Comment("권한그룹별메뉴일련번호")
    private long authrtGroupMenuSn;

    @Column(name = "AUTHRT_GROUP_SN", length = 22)
    @Comment("권한그룹일련번호")
    private long authrtGroupSn;

    @Column(name = "MENU_SN", length = 22)
    @Comment("메뉴일련번호")
    private long menuSn;

    @Column(name = "CREATR_SN", columnDefinition = "INT(10)", updatable=false, nullable = false)
    @Comment("생성자일련번호")
    private long creatrSn;

    @Column(name = "FRST_CRT_DT", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Comment("최초생성일시")
    private LocalDateTime frstCrtDt = LocalDateTime.now();
}
