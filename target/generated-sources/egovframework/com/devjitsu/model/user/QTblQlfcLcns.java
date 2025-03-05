package egovframework.com.devjitsu.model.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblQlfcLcns is a Querydsl query type for TblQlfcLcns
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblQlfcLcns extends EntityPathBase<TblQlfcLcns> {

    private static final long serialVersionUID = 22439002L;

    public static final QTblQlfcLcns tblQlfcLcns = new QTblQlfcLcns("tblQlfcLcns");

    public final StringPath acqsYmd = createString("acqsYmd");

    public final StringPath actvtnYn = createString("actvtnYn");

    public final NumberPath<Long> creatrSn = createNumber("creatrSn", Long.class);

    public final DateTimePath<java.time.LocalDateTime> frstCrtDt = createDateTime("frstCrtDt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> mdfcnDt = createDateTime("mdfcnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> mdfrSn = createNumber("mdfrSn", Long.class);

    public final StringPath pblcnInstNm = createString("pblcnInstNm");

    public final StringPath qlfcLcnsNm = createString("qlfcLcnsNm");

    public final NumberPath<Long> qlfcLcnsSn = createNumber("qlfcLcnsSn", Long.class);

    public final StringPath rmrkCn = createString("rmrkCn");

    public final NumberPath<Long> sortSeq = createNumber("sortSeq", Long.class);

    public final NumberPath<Long> userSn = createNumber("userSn", Long.class);

    public QTblQlfcLcns(String variable) {
        super(TblQlfcLcns.class, forVariable(variable));
    }

    public QTblQlfcLcns(Path<? extends TblQlfcLcns> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblQlfcLcns(PathMetadata metadata) {
        super(TblQlfcLcns.class, metadata);
    }

}

