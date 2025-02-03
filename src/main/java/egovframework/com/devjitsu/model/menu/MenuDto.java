package egovframework.com.devjitsu.model.menu;

import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MenuDto {

    private long menuSn;
    private long upperMenuSn;
    private long menuSeq;
    private String menuNm;
    private String menuNmPath;
    private String menuSnPath;
    private String menuWholPath;
    private String menutype;
    private Long bbsSn;
    private String menuPathNm;
    private String lwrMenuEn;
}
