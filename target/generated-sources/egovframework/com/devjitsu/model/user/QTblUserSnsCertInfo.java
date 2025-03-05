package egovframework.com.devjitsu.model.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblUserSnsCertInfo is a Querydsl query type for TblUserSnsCertInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblUserSnsCertInfo extends EntityPathBase<TblUserSnsCertInfo> {

    private static final long serialVersionUID = 711495769L;

    public static final QTblUserSnsCertInfo tblUserSnsCertInfo = new QTblUserSnsCertInfo("tblUserSnsCertInfo");

    public final StringPath actvtnYn = createString("actvtnYn");

    public final NumberPath<Long> creatrSn = createNumber("creatrSn", Long.class);

    public final DateTimePath<java.time.LocalDateTime> frstCrtDt = createDateTime("frstCrtDt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> mdfcnDt = createDateTime("mdfcnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> mdfrSn = createNumber("mdfrSn", Long.class);

    public final StringPath snsClsf = createString("snsClsf");

    public final StringPath snsUnqNo = createString("snsUnqNo");

    public final NumberPath<Long> userSn = createNumber("userSn", Long.class);

    public final NumberPath<Long> userSnsCertInfoSn = createNumber("userSnsCertInfoSn", Long.class);

    public QTblUserSnsCertInfo(String variable) {
        super(TblUserSnsCertInfo.class, forVariable(variable));
    }

    public QTblUserSnsCertInfo(Path<? extends TblUserSnsCertInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblUserSnsCertInfo(PathMetadata metadata) {
        super(TblUserSnsCertInfo.class, metadata);
    }

}

