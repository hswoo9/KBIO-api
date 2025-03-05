package egovframework.com.devjitsu.model.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblCrr is a Querydsl query type for TblCrr
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblCrr extends EntityPathBase<TblCrr> {

    private static final long serialVersionUID = -1977036931L;

    public static final QTblCrr tblCrr = new QTblCrr("tblCrr");

    public final StringPath actvtnYn = createString("actvtnYn");

    public final NumberPath<Long> creatrSn = createNumber("creatrSn", Long.class);

    public final NumberPath<Long> crrSn = createNumber("crrSn", Long.class);

    public final DateTimePath<java.time.LocalDateTime> frstCrtDt = createDateTime("frstCrtDt", java.time.LocalDateTime.class);

    public final StringPath jbgdNm = createString("jbgdNm");

    public final StringPath jbpsNm = createString("jbpsNm");

    public final StringPath jncmpYmd = createString("jncmpYmd");

    public final DateTimePath<java.time.LocalDateTime> mdfcnDt = createDateTime("mdfcnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> mdfrSn = createNumber("mdfrSn", Long.class);

    public final StringPath ogdpCoNm = createString("ogdpCoNm");

    public final StringPath ogdpDeptNm = createString("ogdpDeptNm");

    public final StringPath rmrkCn = createString("rmrkCn");

    public final StringPath rsgntnYmd = createString("rsgntnYmd");

    public final NumberPath<Long> sortSeq = createNumber("sortSeq", Long.class);

    public final StringPath tkcgTaskCn = createString("tkcgTaskCn");

    public final NumberPath<Long> userSn = createNumber("userSn", Long.class);

    public QTblCrr(String variable) {
        super(TblCrr.class, forVariable(variable));
    }

    public QTblCrr(Path<? extends TblCrr> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblCrr(PathMetadata metadata) {
        super(TblCrr.class, metadata);
    }

}

