package egovframework.com.devjitsu.model.common;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblAtchFileDwnldCnt is a Querydsl query type for TblAtchFileDwnldCnt
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblAtchFileDwnldCnt extends EntityPathBase<TblAtchFileDwnldCnt> {

    private static final long serialVersionUID = -1436604144L;

    public static final QTblAtchFileDwnldCnt tblAtchFileDwnldCnt = new QTblAtchFileDwnldCnt("tblAtchFileDwnldCnt");

    public final StringPath actvtnYn = createString("actvtnYn");

    public final NumberPath<Long> atchFileDwnldCnt = createNumber("atchFileDwnldCnt", Long.class);

    public final NumberPath<Long> dwnldSn = createNumber("dwnldSn", Long.class);

    public final DateTimePath<java.time.LocalDateTime> frstCrtDt = createDateTime("frstCrtDt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> mdfcnDt = createDateTime("mdfcnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> trgtSn = createNumber("trgtSn", Long.class);

    public final StringPath trgtTblNm = createString("trgtTblNm");

    public QTblAtchFileDwnldCnt(String variable) {
        super(TblAtchFileDwnldCnt.class, forVariable(variable));
    }

    public QTblAtchFileDwnldCnt(Path<? extends TblAtchFileDwnldCnt> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblAtchFileDwnldCnt(PathMetadata metadata) {
        super(TblAtchFileDwnldCnt.class, metadata);
    }

}

