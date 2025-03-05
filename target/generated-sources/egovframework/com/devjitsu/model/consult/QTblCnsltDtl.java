package egovframework.com.devjitsu.model.consult;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblCnsltDtl is a Querydsl query type for TblCnsltDtl
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblCnsltDtl extends EntityPathBase<TblCnsltDtl> {

    private static final long serialVersionUID = -1481541859L;

    public static final QTblCnsltDtl tblCnsltDtl = new QTblCnsltDtl("tblCnsltDtl");

    public final StringPath actvtnYn = createString("actvtnYn");

    public final DateTimePath<java.time.LocalDateTime> cnsltAcptDt = createDateTime("cnsltAcptDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> cnsltAplySn = createNumber("cnsltAplySn", Long.class);

    public final DateTimePath<java.time.LocalDateTime> cnsltCmptnDt = createDateTime("cnsltCmptnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> cnsltDtlSn = createNumber("cnsltDtlSn", Long.class);

    public final DateTimePath<java.time.LocalDateTime> cnsltRtrcnDt = createDateTime("cnsltRtrcnDt", java.time.LocalDateTime.class);

    public final StringPath cnsltSttsCd = createString("cnsltSttsCd");

    public final DateTimePath<java.time.LocalDateTime> cnslttDsgnDt = createDateTime("cnslttDsgnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> cnslttUserSn = createNumber("cnslttUserSn", Long.class);

    public final NumberPath<Long> creatrSn = createNumber("creatrSn", Long.class);

    public final DateTimePath<java.time.LocalDateTime> frstCrtDt = createDateTime("frstCrtDt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> mdfcnDt = createDateTime("mdfcnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> mdfrSn = createNumber("mdfrSn", Long.class);

    public QTblCnsltDtl(String variable) {
        super(TblCnsltDtl.class, forVariable(variable));
    }

    public QTblCnsltDtl(Path<? extends TblCnsltDtl> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblCnsltDtl(PathMetadata metadata) {
        super(TblCnsltDtl.class, metadata);
    }

}

