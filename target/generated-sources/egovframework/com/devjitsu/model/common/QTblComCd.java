package egovframework.com.devjitsu.model.common;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblComCd is a Querydsl query type for TblComCd
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblComCd extends EntityPathBase<TblComCd> {

    private static final long serialVersionUID = 881598844L;

    public static final QTblComCd tblComCd = new QTblComCd("tblComCd");

    public final StringPath actvtnYn = createString("actvtnYn");

    public final NumberPath<Long> cdGroupSn = createNumber("cdGroupSn", Long.class);

    public final StringPath comCd = createString("comCd");

    public final StringPath comCdNm = createString("comCdNm");

    public final NumberPath<Long> comCdSn = createNumber("comCdSn", Long.class);

    public final NumberPath<Long> creatrSn = createNumber("creatrSn", Long.class);

    public final StringPath etcMttr1 = createString("etcMttr1");

    public final StringPath etcMttr2 = createString("etcMttr2");

    public final StringPath etcMttr3 = createString("etcMttr3");

    public final StringPath etcMttr4 = createString("etcMttr4");

    public final StringPath etcMttr5 = createString("etcMttr5");

    public final DateTimePath<java.time.LocalDateTime> frstCrtDt = createDateTime("frstCrtDt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> mdfcnDt = createDateTime("mdfcnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> mdfrSn = createNumber("mdfrSn", Long.class);

    public final StringPath rmrkCn = createString("rmrkCn");

    public final NumberPath<Integer> sortSeq = createNumber("sortSeq", Integer.class);

    public QTblComCd(String variable) {
        super(TblComCd.class, forVariable(variable));
    }

    public QTblComCd(Path<? extends TblComCd> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblComCd(PathMetadata metadata) {
        super(TblComCd.class, metadata);
    }

}

