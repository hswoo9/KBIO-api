package egovframework.com.devjitsu.model.user;

import egovframework.com.devjitsu.model.common.TblComFile;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TblMvnEntDto {

    TblMvnEnt tblMvnEnt;
    TblComFile tblComFile;
}
