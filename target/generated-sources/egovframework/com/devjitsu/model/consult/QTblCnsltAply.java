package egovframework.com.devjitsu.model.consult;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblCnsltAply is a Querydsl query type for TblCnsltAply
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblCnsltAply extends EntityPathBase<TblCnsltAply> {

    private static final long serialVersionUID = 1316749531L;

    public static final QTblCnsltAply tblCnsltAply = new QTblCnsltAply("tblCnsltAply");

    public final StringPath actvtnYn = createString("actvtnYn");

    public final StringPath cn = createString("cn");

    public final NumberPath<Long> cnsltAplySn = createNumber("cnsltAplySn", Long.class);

    public final NumberPath<Long> cnsltFld = createNumber("cnsltFld", Long.class);

    public final NumberPath<Long> cnsltSe = createNumber("cnsltSe", Long.class);

    public final NumberPath<Long> creatrSn = createNumber("creatrSn", Long.class);

    public final DateTimePath<java.time.LocalDateTime> frstCrtDt = createDateTime("frstCrtDt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> mdfcnDt = createDateTime("mdfcnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> mdfrSn = createNumber("mdfrSn", Long.class);

    public final StringPath ttl = createString("ttl");

    public final NumberPath<Long> userSn = createNumber("userSn", Long.class);

    public QTblCnsltAply(String variable) {
        super(TblCnsltAply.class, forVariable(variable));
    }

    public QTblCnsltAply(Path<? extends TblCnsltAply> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblCnsltAply(PathMetadata metadata) {
        super(TblCnsltAply.class, metadata);
    }

}

