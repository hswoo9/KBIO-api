package egovframework.com.devjitsu.model.consult;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblCnsltDsctn is a Querydsl query type for TblCnsltDsctn
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblCnsltDsctn extends EntityPathBase<TblCnsltDsctn> {

    private static final long serialVersionUID = -2127586257L;

    public static final QTblCnsltDsctn tblCnsltDsctn = new QTblCnsltDsctn("tblCnsltDsctn");

    public final StringPath actvtnYn = createString("actvtnYn");

    public final StringPath cn = createString("cn");

    public final NumberPath<Long> cnsltAplySn = createNumber("cnsltAplySn", Long.class);

    public final NumberPath<Long> cnsltDsctnSn = createNumber("cnsltDsctnSn", Long.class);

    public final NumberPath<Long> creatrSn = createNumber("creatrSn", Long.class);

    public final StringPath dsctnSe = createString("dsctnSe");

    public final DateTimePath<java.time.LocalDateTime> frstCrtDt = createDateTime("frstCrtDt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> mdfcnDt = createDateTime("mdfcnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> mdfrSn = createNumber("mdfrSn", Long.class);

    public QTblCnsltDsctn(String variable) {
        super(TblCnsltDsctn.class, forVariable(variable));
    }

    public QTblCnsltDsctn(Path<? extends TblCnsltDsctn> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblCnsltDsctn(PathMetadata metadata) {
        super(TblCnsltDsctn.class, metadata);
    }

}

