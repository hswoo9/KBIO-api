package egovframework.com.devjitsu.model.search;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblSrchKywd is a Querydsl query type for TblSrchKywd
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblSrchKywd extends EntityPathBase<TblSrchKywd> {

    private static final long serialVersionUID = 811076642L;

    public static final QTblSrchKywd tblSrchKywd = new QTblSrchKywd("tblSrchKywd");

    public final DateTimePath<java.time.LocalDateTime> frstCrtDt = createDateTime("frstCrtDt", java.time.LocalDateTime.class);

    public final StringPath kywd = createString("kywd");

    public final DateTimePath<java.time.LocalDateTime> mdfcnDt = createDateTime("mdfcnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> srchKywdSn = createNumber("srchKywdSn", Long.class);

    public final NumberPath<Long> srchNmtm = createNumber("srchNmtm", Long.class);

    public QTblSrchKywd(String variable) {
        super(TblSrchKywd.class, forVariable(variable));
    }

    public QTblSrchKywd(Path<? extends TblSrchKywd> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblSrchKywd(PathMetadata metadata) {
        super(TblSrchKywd.class, metadata);
    }

}

