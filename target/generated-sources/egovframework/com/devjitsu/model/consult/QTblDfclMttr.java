package egovframework.com.devjitsu.model.consult;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblDfclMttr is a Querydsl query type for TblDfclMttr
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblDfclMttr extends EntityPathBase<TblDfclMttr> {

    private static final long serialVersionUID = 1291991489L;

    public static final QTblDfclMttr tblDfclMttr = new QTblDfclMttr("tblDfclMttr");

    public final StringPath actvtnYn = createString("actvtnYn");

    public final StringPath ansCn = createString("ansCn");

    public final DateTimePath<java.time.LocalDateTime> ansRegDt = createDateTime("ansRegDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> creatrSn = createNumber("creatrSn", Long.class);

    public final StringPath dfclMttrCn = createString("dfclMttrCn");

    public final NumberPath<Long> dfclMttrFld = createNumber("dfclMttrFld", Long.class);

    public final NumberPath<Long> dfclMttrSn = createNumber("dfclMttrSn", Long.class);

    public final DateTimePath<java.time.LocalDateTime> frstCrtDt = createDateTime("frstCrtDt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> mdfcnDt = createDateTime("mdfcnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> mdfrSn = createNumber("mdfrSn", Long.class);

    public final StringPath ttl = createString("ttl");

    public final NumberPath<Long> userSn = createNumber("userSn", Long.class);

    public QTblDfclMttr(String variable) {
        super(TblDfclMttr.class, forVariable(variable));
    }

    public QTblDfclMttr(Path<? extends TblDfclMttr> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblDfclMttr(PathMetadata metadata) {
        super(TblDfclMttr.class, metadata);
    }

}

