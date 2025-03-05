package egovframework.com.devjitsu.model.common;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblComCdGroup is a Querydsl query type for TblComCdGroup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblComCdGroup extends EntityPathBase<TblComCdGroup> {

    private static final long serialVersionUID = -359173533L;

    public static final QTblComCdGroup tblComCdGroup = new QTblComCdGroup("tblComCdGroup");

    public final StringPath actvtnYn = createString("actvtnYn");

    public final StringPath cdGroup = createString("cdGroup");

    public final StringPath cdGroupNm = createString("cdGroupNm");

    public final NumberPath<Long> cdGroupSn = createNumber("cdGroupSn", Long.class);

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

    public QTblComCdGroup(String variable) {
        super(TblComCdGroup.class, forVariable(variable));
    }

    public QTblComCdGroup(Path<? extends TblComCdGroup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblComCdGroup(PathMetadata metadata) {
        super(TblComCdGroup.class, metadata);
    }

}

