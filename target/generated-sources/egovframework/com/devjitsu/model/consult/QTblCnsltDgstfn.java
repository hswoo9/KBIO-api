package egovframework.com.devjitsu.model.consult;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblCnsltDgstfn is a Querydsl query type for TblCnsltDgstfn
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblCnsltDgstfn extends EntityPathBase<TblCnsltDgstfn> {

    private static final long serialVersionUID = -1541270261L;

    public static final QTblCnsltDgstfn tblCnsltDgstfn = new QTblCnsltDgstfn("tblCnsltDgstfn");

    public final StringPath actvtnYn = createString("actvtnYn");

    public final StringPath chcScr = createString("chcScr");

    public final NumberPath<Long> cnsltAplySn = createNumber("cnsltAplySn", Long.class);

    public final NumberPath<Long> cnsltDgstfnSn = createNumber("cnsltDgstfnSn", Long.class);

    public final NumberPath<Long> creatrSn = createNumber("creatrSn", Long.class);

    public final StringPath dgstfnArtcl = createString("dgstfnArtcl");

    public final DateTimePath<java.time.LocalDateTime> frstCrtDt = createDateTime("frstCrtDt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> mdfcnDt = createDateTime("mdfcnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> mdfrSn = createNumber("mdfrSn", Long.class);

    public QTblCnsltDgstfn(String variable) {
        super(TblCnsltDgstfn.class, forVariable(variable));
    }

    public QTblCnsltDgstfn(Path<? extends TblCnsltDgstfn> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblCnsltDgstfn(PathMetadata metadata) {
        super(TblCnsltDgstfn.class, metadata);
    }

}

