package egovframework.com.devjitsu.model.bbs;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTblPst is a Querydsl query type for TblPst
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblPst extends EntityPathBase<TblPst> {

    private static final long serialVersionUID = 1209478969L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTblPst tblPst = new QTblPst("tblPst");

    public final StringPath actvtnYn = createString("actvtnYn");

    public final NumberPath<Integer> ansStp = createNumber("ansStp", Integer.class);

    public final NumberPath<Long> ansUserSn = createNumber("ansUserSn", Long.class);

    public final NumberPath<Long> bbsSn = createNumber("bbsSn", Long.class);

    public final NumberPath<Long> creatrSn = createNumber("creatrSn", Long.class);

    public final DateTimePath<java.time.LocalDateTime> frstCrtDt = createDateTime("frstCrtDt", java.time.LocalDateTime.class);

    public final StringPath linkUrlAddr = createString("linkUrlAddr");

    public final DateTimePath<java.time.LocalDateTime> mdfcnDt = createDateTime("mdfcnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> mdfrSn = createNumber("mdfrSn", Long.class);

    public final StringPath ntcBgngDt = createString("ntcBgngDt");

    public final StringPath ntcEndDate = createString("ntcEndDate");

    public final DateTimePath<java.time.LocalDateTime> popupBgngDt = createDateTime("popupBgngDt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> popupEndDt = createDateTime("popupEndDt", java.time.LocalDateTime.class);

    public final StringPath popupUseYn = createString("popupUseYn");

    public final StringPath prvtPswd = createString("prvtPswd");

    public final NumberPath<Long> pstClsf = createNumber("pstClsf", Long.class);

    public final StringPath pstCn = createString("pstCn");

    public final NumberPath<Long> pstGroup = createNumber("pstGroup", Long.class);

    public final NumberPath<Long> pstInqCnt = createNumber("pstInqCnt", Long.class);

    public final NumberPath<Long> pstSn = createNumber("pstSn", Long.class);

    public final StringPath pstTtl = createString("pstTtl");

    public final StringPath rlsYn = createString("rlsYn");

    public final egovframework.com.devjitsu.model.user.QTblUser tblUser;

    public final StringPath upendNtcYn = createString("upendNtcYn");

    public final NumberPath<Long> upPstSn = createNumber("upPstSn", Long.class);

    public QTblPst(String variable) {
        this(TblPst.class, forVariable(variable), INITS);
    }

    public QTblPst(Path<? extends TblPst> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTblPst(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTblPst(PathMetadata metadata, PathInits inits) {
        this(TblPst.class, metadata, inits);
    }

    public QTblPst(Class<? extends TblPst> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.tblUser = inits.isInitialized("tblUser") ? new egovframework.com.devjitsu.model.user.QTblUser(forProperty("tblUser")) : null;
    }

}

