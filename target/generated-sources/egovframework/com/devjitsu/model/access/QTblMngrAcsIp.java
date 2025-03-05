package egovframework.com.devjitsu.model.access;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblMngrAcsIp is a Querydsl query type for TblMngrAcsIp
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblMngrAcsIp extends EntityPathBase<TblMngrAcsIp> {

    private static final long serialVersionUID = 631747213L;

    public static final QTblMngrAcsIp tblMngrAcsIp = new QTblMngrAcsIp("tblMngrAcsIp");

    public final StringPath actvtnYn = createString("actvtnYn");

    public final NumberPath<Long> creatrSn = createNumber("creatrSn", Long.class);

    public final DateTimePath<java.time.LocalDateTime> frstCrtDt = createDateTime("frstCrtDt", java.time.LocalDateTime.class);

    public final StringPath ipAddr = createString("ipAddr");

    public final DateTimePath<java.time.LocalDateTime> mdfcnDt = createDateTime("mdfcnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> mdfrSn = createNumber("mdfrSn", Long.class);

    public final NumberPath<Long> mngrAcsSn = createNumber("mngrAcsSn", Long.class);

    public final StringPath plcusNm = createString("plcusNm");

    public QTblMngrAcsIp(String variable) {
        super(TblMngrAcsIp.class, forVariable(variable));
    }

    public QTblMngrAcsIp(Path<? extends TblMngrAcsIp> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblMngrAcsIp(PathMetadata metadata) {
        super(TblMngrAcsIp.class, metadata);
    }

}

