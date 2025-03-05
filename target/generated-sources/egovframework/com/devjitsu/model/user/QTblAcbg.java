package egovframework.com.devjitsu.model.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblAcbg is a Querydsl query type for TblAcbg
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblAcbg extends EntityPathBase<TblAcbg> {

    private static final long serialVersionUID = -1158677107L;

    public static final QTblAcbg tblAcbg = new QTblAcbg("tblAcbg");

    public final NumberPath<Long> acbgSn = createNumber("acbgSn", Long.class);

    public final StringPath actvtnYn = createString("actvtnYn");

    public final NumberPath<Long> creatrSn = createNumber("creatrSn", Long.class);

    public final StringPath dgrNm = createString("dgrNm");

    public final DateTimePath<java.time.LocalDateTime> frstCrtDt = createDateTime("frstCrtDt", java.time.LocalDateTime.class);

    public final StringPath grdtnYmd = createString("grdtnYmd");

    public final DateTimePath<java.time.LocalDateTime> mdfcnDt = createDateTime("mdfcnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> mdfrSn = createNumber("mdfrSn", Long.class);

    public final StringPath mjrNm = createString("mjrNm");

    public final StringPath mtcltnYmd = createString("mtcltnYmd");

    public final StringPath rmrkCn = createString("rmrkCn");

    public final StringPath schlNm = createString("schlNm");

    public final StringPath scsbjtNm = createString("scsbjtNm");

    public final NumberPath<Long> sortSeq = createNumber("sortSeq", Long.class);

    public final NumberPath<Long> userSn = createNumber("userSn", Long.class);

    public QTblAcbg(String variable) {
        super(TblAcbg.class, forVariable(variable));
    }

    public QTblAcbg(Path<? extends TblAcbg> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblAcbg(PathMetadata metadata) {
        super(TblAcbg.class, metadata);
    }

}

