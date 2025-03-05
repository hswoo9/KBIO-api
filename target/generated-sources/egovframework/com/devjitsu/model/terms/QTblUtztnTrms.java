package egovframework.com.devjitsu.model.terms;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblUtztnTrms is a Querydsl query type for TblUtztnTrms
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblUtztnTrms extends EntityPathBase<TblUtztnTrms> {

    private static final long serialVersionUID = -324400499L;

    public static final QTblUtztnTrms tblUtztnTrms = new QTblUtztnTrms("tblUtztnTrms");

    public final StringPath creatr = createString("creatr");

    public final NumberPath<Long> creatrSn = createNumber("creatrSn", Long.class);

    public final DateTimePath<java.time.LocalDateTime> frstCrtDt = createDateTime("frstCrtDt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> mdfcnDt = createDateTime("mdfcnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> mdfrSn = createNumber("mdfrSn", Long.class);

    public final StringPath useYn = createString("useYn");

    public final StringPath utztnTrmsCn = createString("utztnTrmsCn");

    public final StringPath utztnTrmsKnd = createString("utztnTrmsKnd");

    public final NumberPath<Long> utztnTrmsSn = createNumber("utztnTrmsSn", Long.class);

    public final StringPath utztnTrmsTtl = createString("utztnTrmsTtl");

    public QTblUtztnTrms(String variable) {
        super(TblUtztnTrms.class, forVariable(variable));
    }

    public QTblUtztnTrms(Path<? extends TblUtztnTrms> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblUtztnTrms(PathMetadata metadata) {
        super(TblUtztnTrms.class, metadata);
    }

}

